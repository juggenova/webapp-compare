package net.ghezzi.jugg.wcp.web;

import static net.yadaframework.components.YadaUtil.messageSource;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import net.ghezzi.jugg.wcp.components.PollUtil;
import net.ghezzi.jugg.wcp.components.WcpEmailService;
import net.ghezzi.jugg.wcp.core.WcpConfiguration;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;
import net.ghezzi.jugg.wcp.persistence.repository.PollDao;
import net.ghezzi.jugg.wcp.persistence.repository.UserProfileDao;
import net.ghezzi.jugg.wcp.persistence.repository.VoteDao;
import net.yadaframework.components.YadaNotify;
import net.yadaframework.components.YadaWebUtil;
import net.yadaframework.core.YadaLocalePathChangeInterceptor;
import net.yadaframework.security.YadaSecurityConfig;
import net.yadaframework.security.components.YadaSecurityUtil;
import net.yadaframework.web.YadaViews;

@Controller
public class HomeController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private YadaWebUtil yadaWebUtil;
	@Autowired private YadaSecurityUtil yadaSecurityUtil;
	@Autowired private YadaNotify yadaNotify;
	@Autowired private WcpConfiguration config;
	@Autowired private PollUtil pollUtil;
	@Autowired private PollDao pollDao;
	@Autowired private VoteDao voteDao;
	@Autowired private UserProfileDao userProfileDao;
	@Autowired private WcpEmailService wcpEmailService;
	
	@Scheduled(cron = "0 59 23 * * ?")
	private void checkDeadlines() {
		log.debug("Checking deadlines...");
		Poll defaultPoll = pollDao.findDefault();
		if (!defaultPoll.isClosed()) {
			Date deadline = defaultPoll.getDeadline();
			Date now = new Date();
			if (!deadline.after(now)) {
				defaultPoll = pollUtil.closePoll(defaultPoll);
				sendEmails(defaultPoll);
			}
		}
	}
	
	/**
	 * Send an email to all users that voted in the poll
	 * @param defaultPoll
	 */
	public void sendEmails(Poll defaultPoll) {
		List<UserProfile> voters = pollDao.findVoters(defaultPoll);
		for (UserProfile userProfile : voters) {
			wcpEmailService.notifyPollClosed(defaultPoll, userProfile.getEmail(), userProfile.getLocale());
		}
	}
	
	
	@GetMapping("/loginPost")
	public String invalidLoginGet(Locale locale) {
		// Login with GET will redirect to home
		return yadaWebUtil.redirectString("/", locale);
	}

	public String goHome(Model model) {
		return "/home";
	}

	@RequestMapping("/")
	public String home(HttpServletRequest request, Model model, Locale locale) {
		boolean loggedIn = yadaSecurityUtil.isLoggedIn();
		if (loggedIn) {
			return yadaWebUtil.redirectString("/user/poll", locale);
		}
		if (!config.isLocalePathVariableEnabled()
			|| YadaLocalePathChangeInterceptor.localePathRequested(request) 
			|| yadaWebUtil.isErrorPage(request)
			|| model.containsAttribute("login")) {
			// Fetch the user if any, based on the uuid cookie
			return goHome(model);
		}
		// The locale is missing so set it explicitly with a redirect. 
		// The "locale" variable has already been normalized by YadaWebConfig.localeResolver()
		// to either an accepted locale or the platform default.
		return yadaWebUtil.redirectString("/", locale); // Moved temporarily
	}

	/**
	 * Normal request login page. Also called when a protected page is requested.
	 */
	@RequestMapping(YadaSecurityConfig.DEFAULT_LOGIN_URL) // "/login"
	public String login(HttpServletRequest request, Model model, Locale locale) {
		if (yadaWebUtil.isAjaxRequest()) {
			return loginAjax();
		}
//		boolean loggedIn = yadaSecurityUtil.isLoggedIn();
//		if (!loggedIn) {
//			model.addAttribute("login", "login");
//		}
		// The login page is the home page
		return home(request, model, locale);
	}
	
	/**
	 * Ajax request login page (modal)
	 */
	@RequestMapping(YadaSecurityConfig.DEFAULT_LOGIN_URL_AJAX) // "/ajaxLogin"
	public String loginAjax() {
		boolean loggedIn = yadaSecurityUtil.isLoggedIn();
		if (!loggedIn) {
			return "/modalLogin";
		} else {
			return YadaViews.AJAX_SUCCESS; // Do nothing
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// I metodi che seguono sono stati autogenerati e serviranno in futuro
	//////////////////////////////////////////////////////////////////////

	@RequestMapping("/errorPage")
	public String errorPage(Model model, Locale locale) {
		return "/errorPage";
	}

	/**
	 * Called after session timeout
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping("/timeout")
	public String timeout(Model model, Locale locale) {
		String title = messageSource.getMessage("session.expired.title", null, "Session Expired", locale);
		String text = messageSource.getMessage("session.expired.message", null, "Session expired for inactivity, please login again", locale);
		yadaNotify.title(title, model).message(text).info().redirectOnClose("/").add();
		return "/home";
	}

	/**
	 * This method should be called when the user clicks on a login link explicitly
	 */
	@RequestMapping("/openLogin")
	public String openLogin(HttpServletRequest request, Model model, Locale locale) {
		// When the user explicitly clicks on the login button, any previously saved request
		// must be reset or he will end up in there after login
		yadaSecurityUtil.clearAnySavedRequest();
		if (yadaWebUtil.isAjaxRequest()) {
			return loginAjax();
		}
		return login(request, model, locale);
	}
	
}

package net.ghezzi.jugg.wcp.web;

import static net.yadaframework.components.YadaUtil.messageSource;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import net.ghezzi.jugg.wcp.core.WcpConfiguration;
import net.ghezzi.jugg.wcp.persistence.entity.ChoiceEnum;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;
import net.ghezzi.jugg.wcp.persistence.entity.Vote;
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

	private @Autowired PollDao pollDao;
	private @Autowired VoteDao voteDao;
	private @Autowired UserProfileDao userProfileDao;
	
	
	@RequestMapping("/castVote")
	public String castVote(Integer index, ChoiceEnum voted, Model model) {
		List<Vote> sortedVotes = voteDao.findVotes(null, null);
		Vote toChange = null;
		if (index>=sortedVotes.size()) {
			toChange = new Vote();
			Calendar calendar = Calendar.getInstance();
			calendar.set(2024, Calendar.NOVEMBER, index+1, 0, 0, 0);
			toChange.setDay(calendar.getTime());
		} else {
			toChange = sortedVotes.get(index);
		}
		toChange.setChoice(voted);
		toChange = voteDao.save(toChange);
		return goHome(model);
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
	
	private String goHome(Model model) {
		insertPollData(model);
		return "/home";
	}

	@RequestMapping("/")
	public String home(HttpServletRequest request, Model model, Locale locale) {
		// You get here either when the url is just "/" or when it is "en/" with localepath configured.
		// We need to insert the locale path when missing
		// NOTE: any 404 NOT FOUND error opens the home page and gets here too.
		if (!config.isLocalePathVariableEnabled() 
			|| YadaLocalePathChangeInterceptor.localePathRequested(request) 
			|| yadaWebUtil.isErrorPage(request)
			|| model.containsAttribute("login")) {
			return goHome(model);
		}
		// The locale is missing so set it explicitly with a redirect. 
		// The "locale" variable has already been normalized by YadaWebConfig.localeResolver()
		// to either an accepted locale or the platform default.
		return yadaWebUtil.redirectString("/", locale); // Moved temporarily
	}
	
	/**
	 * Aggiunge al model i voti eventualmente già dati
	 * @param model
	 */
	private void insertPollData(Model model) {
		Poll poll = null;
		UserProfile currentUser = null;
		List<Vote> sortedVotes = voteDao.findVotes(currentUser, poll);
		/* Parte dinamica per ora rimossa
		Poll poll = pollDao.findDefault(); // Per ora c'è solo un Poll cablato
		// Per ora uso l'utente di default
		UserProfile currentUser = userProfileDao.findUserProfileByUsername("admin@ghezzi.net");
		List<Vote> sortedVotes = voteDao.findVotes(currentUser, poll);
		model.addAttribute("poll", poll);
		*/
		model.addAttribute("sortedVotes", sortedVotes);
	}

	//////////////////////////////////////////////////////////////////////
	// I metodi che seguono sono stati autogenerati e serviranno in futuro
	//////////////////////////////////////////////////////////////////////

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
	
	/**
	 * Normal request login page. Also called when a protected page is requested.
	 */
	@RequestMapping(YadaSecurityConfig.DEFAULT_LOGIN_URL) // "/login"
	public String login(HttpServletRequest request, Model model, Locale locale) {
		if (yadaWebUtil.isAjaxRequest()) {
			return loginAjax();
		}
		boolean loggedIn = yadaSecurityUtil.isLoggedIn();
		if (!loggedIn) {
			model.addAttribute("login", "login");
		}
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

}

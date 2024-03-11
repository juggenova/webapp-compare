package net.ghezzi.jugg.wcp.web;

import static net.yadaframework.components.YadaUtil.messageSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
	
	@RequestMapping("/closePoll")
	public String closePollWeb(Long id, Model model, Locale locale) {
		Poll poll = pollDao.find(id);
		closePoll(poll);
		return yadaWebUtil.redirectString("/", locale);
	}
	
	private void closePoll(Poll poll) {
		try {
			Date winner = pollDao.getPollResult(poll);
			poll.setChosenDay(winner);
			pollDao.save(poll);
			log.debug("Poll {} closed now", poll);
		} catch (Exception e) {
			log.error("Can't close poll {}", poll, e);
		}
	}
	
	@Scheduled(cron = "0 59 23 * * ?")
	private void checkDeadlines() {
		log.debug("Checking deadlines...");
		Poll defaultPoll = pollDao.findDefault();
		if (defaultPoll.getChosenDay()==null) {
			Date deadline = defaultPoll.getDeadline();
			Date now = new Date();
			if (!deadline.after(now)) {
				closePoll(defaultPoll);
			}
		}
	}
	
	private List<Vote> createAllVotes(UserProfile currentUser) {
		Poll defaultPoll = pollDao.findDefault();
		List<Vote> result = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < 7; i++) {
			Vote vote = new Vote();
			calendar.set(2024, Calendar.NOVEMBER, i+1, 0, 0, 0);
			vote.setDay(calendar.getTime());
			vote.setVoter(currentUser);
			vote.setChoice(ChoiceEnum.NO);
			vote.setPoll(defaultPoll);
			vote = voteDao.save(vote);
			result.add(vote);
		}
		return result;
	}
	
	@RequestMapping("/castVote")
	public String castVote(String uuid, Integer index, ChoiceEnum voted, Model model) {
		UserProfile currentUser = userProfileDao.ensureUser(uuid);
		Poll defaultPoll = pollDao.findDefault();
		List<Vote> sortedVotes = voteDao.findVotes(currentUser, defaultPoll);
		// Al primo voto inizializzo tutti i voti al default
		if (sortedVotes.isEmpty()) {
			sortedVotes = createAllVotes(currentUser);
		}
		Vote toChange = sortedVotes.get(index);
		toChange.setChoice(voted);
		toChange = voteDao.save(toChange);
		return goHome(currentUser, model);
	}
	
	private String goHome(UserProfile currentUser, Model model) {
		insertPollData(currentUser, model);
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
			// Fetch the user if any, based on the uuid cookie
			UserProfile currentUser = null;
			String uuid = yadaWebUtil.getCookieValue(request, "uuid");
			if (uuid!=null) {
				currentUser = userProfileDao.ensureUser(uuid);
			}
			return goHome(currentUser, model);
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
	private void insertPollData(UserProfile currentUser, Model model) {
		Poll poll = pollDao.findDefault();
		if (!poll.isClosed()) {
			List<Vote> sortedVotes = voteDao.findVotes(currentUser, poll);
			if (currentUser!=null) {
				model.addAttribute("uuid", currentUser.getUuid());
			}
			model.addAttribute("sortedVotes", sortedVotes);
		}
		model.addAttribute("poll", poll);
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

package net.ghezzi.jugg.wcp.web;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.ghezzi.jugg.wcp.components.UserSession;
import net.ghezzi.jugg.wcp.core.WcpConfiguration;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;
import net.ghezzi.jugg.wcp.persistence.repository.PollDao;
import net.yadaframework.components.YadaNotify;
import net.yadaframework.components.YadaUtil;
import net.yadaframework.persistence.YadaDataTableDao;
import net.yadaframework.persistence.YadaSql;
import net.yadaframework.web.YadaDatatablesRequest;
import net.yadaframework.web.YadaViews;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private WcpConfiguration config;
	@Autowired private PollDao pollDao;
	@Autowired private YadaNotify yadaNotify;
	@Autowired private YadaDataTableDao yadaDataTableDao;
	@Autowired private UserSession userSession;
	
	public final static int MAX_DAYS = 64; // Max interval a poll can cover
	
	@ModelAttribute("poll")
	public Poll addPoll(@RequestParam(value="pollId", required=false) Long pollId, Model model) {
		Poll toEdit = null;
		Exception exception = null;
		if (pollId!=null) {
			UserProfile currentUser = userSession.getCurrentUserProfile();
			try {
				toEdit = pollDao.findForUser(currentUser, pollId);
			} catch (Exception e) {
				exception = e;
			}
			if (toEdit==null) {
				log.error("Can't find Poll with id={} for user {} - (creating new)", pollId, currentUser, exception);
			} else if (log.isDebugEnabled()) {
				log.debug("Poll {} fetched from DB as ModelAttribute", pollId);
			}
		}
		if (toEdit==null) {
			toEdit = new Poll();
		}
		return toEdit;
	}

	@RequestMapping("/dashboard")
	public String dashboard(Model model, Locale locale) {
		return "/admin/dashboard";
	}

	@RequestMapping("/ajaxDeletePoll")
	public String ajaxDeletePoll(Poll poll, Model model, Locale locale) {
		// Poll is not null only if the current user is the owner
		String title = poll!=null?poll.getTitle():"";
		boolean deleted = pollDao.delete(poll);
		if (deleted) {
			// TODO i18n
			return yadaNotify.title("Delete Poll", model).ok().message("Poll with title '{}' deleted", title).add();
		}
		return YadaViews.AJAX_RELOAD;
	}
	
	/**
	 * Show the poll form in a modal
	 * @param poll
	 * @param model
	 * @param locale
	 */
	@RequestMapping("/ajaxPollForm")
	public String ajaxPollForm(Poll poll, Model model, Locale locale) {
		return "/admin/pollForm";
	}
	
	/**
	 * Handle poll form submission
	 * @param poll
	 * @param model
	 * @param locale
	 */
	@RequestMapping("/ajaxAddOrUpdatePoll")
	public String ajaxAddOrUpdatePoll(Poll poll, BindingResult pollBinding, Model model, Locale locale) {
		boolean addPoll = poll.getId()==null;
		UserProfile currentUser = userSession.getCurrentUserProfile();
		poll.setOwner(currentUser);
		ValidationUtils.rejectIfEmptyOrWhitespace(pollBinding, "title", "validation.value.empty");
		Date startDay = poll.getStartDay();
		Date endDay = poll.getEndDay();
		Date deadlineTime = poll.getDeadline();
		Date now = new Date();
		ValidationUtils.rejectIfEmpty(pollBinding, "startDay", "poll.validation.notadate");
		ValidationUtils.rejectIfEmpty(pollBinding, "endDay", "poll.validation.notadate");
		ValidationUtils.rejectIfEmpty(pollBinding, "deadline", "poll.validation.notadate");
		if (!pollBinding.hasErrors()) {
			if (startDay.after(endDay)) {
				pollBinding.rejectValue("endDay", "poll.validation.endbeforestart");
			}
			Date startDayTime = YadaUtil.roundBackToMidnight(startDay, TimeZone.getDefault());
			if (deadlineTime.after(startDayTime) || deadlineTime.before(now)) {
				pollBinding.rejectValue("deadline", "poll.validation.deadlineinvalid");
			}
			// Check max interval
			long diffInMillies = Math.abs(endDay.getTime() - startDay.getTime());
			long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			if (diffInDays > MAX_DAYS) {
				pollBinding.rejectValue("endDay", "poll.validation.toomanydays", new Object[]{diffInDays, MAX_DAYS}, "Too many days");
			}
		}
		if (pollBinding.hasErrors()) {
			return ajaxPollForm(poll, model, locale);
		}
		poll = pollDao.save(poll);
		if (addPoll) {
			return yadaNotify.title("New Poll", model).ok().message("Poll with title '{}' created", poll.getTitle()).add();
		} else {
			return yadaNotify.title("Edit Poll", model).ok().message("Poll saved").add();
		}
	}
	
	@RequestMapping(value ="/pollTablePage", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public Map<String, Object> pollTablePage(YadaDatatablesRequest yadaDatatablesRequest, Locale locale) {
		UserProfile currentUser = userSession.getCurrentUserProfile();
		// Condizione per mostrare solo i Poll dell'utente corrente
		YadaSql yadaSql = yadaDatatablesRequest.getYadaSql();
		yadaSql.join("join e.owner up");
		yadaSql.where("up=:userProfile").and();
		yadaSql.setParameter("userProfile", currentUser);
		//
		Map<String, Object> result = yadaDataTableDao.getConvertedJsonPage(yadaDatatablesRequest, Poll.class, locale);
		return result;
	}

}

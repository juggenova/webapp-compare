package net.ghezzi.jugg.wcp.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import net.ghezzi.jugg.wcp.components.PollUtil;
import net.ghezzi.jugg.wcp.components.UserSession;
import net.ghezzi.jugg.wcp.core.WcpConfiguration;
import net.ghezzi.jugg.wcp.persistence.entity.ChoiceEnum;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;
import net.ghezzi.jugg.wcp.persistence.entity.Vote;
import net.ghezzi.jugg.wcp.persistence.repository.PollDao;
import net.ghezzi.jugg.wcp.persistence.repository.UserProfileDao;
import net.ghezzi.jugg.wcp.persistence.repository.VoteDao;
import net.yadaframework.components.YadaWebUtil;
import net.yadaframework.web.YadaViews;

@Controller
@RequestMapping("/user")
public class UserController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private WcpConfiguration config;
	@Autowired private HomeController homeController;
	@Autowired private PollUtil pollUtil;
	@Autowired private PollDao pollDao;
	@Autowired private VoteDao voteDao;
	@Autowired private UserProfileDao userProfileDao;
	@Autowired private UserSession userSession;
	@Autowired private YadaWebUtil yadaWebUtil;

	/**
	 * Mostra tutti i poll
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping("/poll")
	public String poll(Model model, Locale locale) {
		UserProfile currentUser = userSession.getCurrentUserProfile();
		insertPollData(currentUser, model);
		return "/user/poll";
	}
	
	@RequestMapping("/castVote") // Ajax call
	public String castVote(Long voteId, ChoiceEnum choice, Model model) {
		UserProfile currentUser = userSession.getCurrentUserProfile();
		Vote toChange = voteDao.findForUser(currentUser, voteId);
		toChange.setChoice(choice);
		toChange = voteDao.save(toChange);
		return YadaViews.AJAX_SUCCESS;
	}

	/**
	 * Aggiunge al model i voti eventualmente già dati
	 * @param model
	 */
	private void insertPollData(UserProfile currentUser, Model model) {
		List<Poll> polls = pollDao.findAll();
		Map<Poll, List<Vote>> pollToVote = new HashMap<>();
		for (Poll poll : polls) {
			if (!poll.isClosed()) {
				List<Vote> sortedVotes = voteDao.findVotes(currentUser, poll);
				if (sortedVotes.isEmpty()) {
					// Inizializzo tutti i voti al default
					sortedVotes = pollUtil.createAllVotes(poll, currentUser);
				}
				pollToVote.put(poll, sortedVotes);
			}
		}
		model.addAttribute("polls", polls);
		model.addAttribute("pollToVote", pollToVote);
	}
	
	@RequestMapping("/closePoll")
	public String closePollWeb(Long id, Model model, Locale locale) {
		UserProfile currentUser = userSession.getCurrentUserProfile();
		Poll poll = pollDao.findForUser(currentUser, id);
		poll = pollUtil.closePoll(poll);
		return poll(model, locale);
	}

}

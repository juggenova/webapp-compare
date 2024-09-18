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

	@RequestMapping("/poll")
	public String poll(Model model, Locale locale) {
		UserProfile currentUser = userSession.getCurrentUserProfile();
		insertPollData(currentUser, model);
		return "/user/poll";
	}
	
	@RequestMapping("/castVote")
	public String castVote(Long pollId, Integer index, ChoiceEnum voted, Model model) {
		UserProfile currentUser = userSession.getCurrentUserProfile();
		Poll poll = pollDao.find(pollId);
		List<Vote> sortedVotes = voteDao.findVotes(currentUser, poll);
		// Al primo voto inizializzo tutti i voti al default
		if (sortedVotes.isEmpty()) {
			sortedVotes = pollUtil.createAllVotes(poll, currentUser);
		}
		Vote toChange = sortedVotes.get(index);
		toChange.setChoice(voted);
		toChange = voteDao.save(toChange);
		return homeController.goHome(model);
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
				pollToVote.put(poll, sortedVotes);
			}
		}
		model.addAttribute("polls", polls);
		model.addAttribute("pollToVote", pollToVote);
	}
	
	@RequestMapping("/closePoll")
	@Deprecated // Temporaneo
	public String closePollWeb(Long id, Model model, Locale locale) {
		Poll poll = pollDao.find(id);
		poll = pollUtil.closePoll(poll);
		homeController.sendEmails(poll);
		return poll(model, locale);
	}

}

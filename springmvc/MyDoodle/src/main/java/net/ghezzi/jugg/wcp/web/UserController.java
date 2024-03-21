package net.ghezzi.jugg.wcp.web;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import net.ghezzi.jugg.wcp.components.PollUtil;
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
	public String castVote(Integer index, ChoiceEnum voted, Model model) {
		UserProfile currentUser = userSession.getCurrentUserProfile();
		Poll defaultPoll = pollDao.findDefault();
		List<Vote> sortedVotes = voteDao.findVotes(currentUser, defaultPoll);
		// Al primo voto inizializzo tutti i voti al default
		if (sortedVotes.isEmpty()) {
			sortedVotes = pollUtil.createAllVotes(currentUser);
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
		Poll poll = pollDao.findDefault();
		if (!poll.isClosed()) {
			List<Vote> sortedVotes = voteDao.findVotes(currentUser, poll);
			model.addAttribute("sortedVotes", sortedVotes);
		}
		model.addAttribute("poll", poll);
	}

	
	@RequestMapping("/closePoll")
	@Deprecated // Temporaneo
	public String closePollWeb(Long id, Model model, Locale locale) {
		Poll poll = pollDao.find(id);
		pollUtil.closePoll(poll);
		return poll(model, locale);
	}

}

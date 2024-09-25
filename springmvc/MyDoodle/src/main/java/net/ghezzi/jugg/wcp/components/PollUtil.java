package net.ghezzi.jugg.wcp.components;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.ghezzi.jugg.wcp.persistence.entity.ChoiceEnum;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;
import net.ghezzi.jugg.wcp.persistence.entity.Vote;
import net.ghezzi.jugg.wcp.persistence.repository.PollDao;
import net.ghezzi.jugg.wcp.persistence.repository.VoteDao;
import net.ghezzi.jugg.wcp.web.AdminController;
import net.yadaframework.exceptions.YadaInvalidUsageException;

@Component
public class PollUtil {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private PollDao pollDao;
	@Autowired private VoteDao voteDao;
	@Autowired private WcpEmailService wcpEmailService;

	/**
	 * Close the poll and send notification emails to voters
	 * @param poll
	 * @return
	 */
	public Poll closePoll(Poll poll) {
		if (poll!=null && !poll.isClosed()) {
			try {
				Date winner = pollDao.getPollResult(poll);
				poll.setChosenDay(winner);
				poll = pollDao.save(poll);
				log.debug("Poll {} closed now", poll);
			} catch (Exception e) {
				log.error("Can't close poll {}", poll, e);
			}
			sendEmails(poll);
		}
		return poll;
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

	/**
	 * Set the default vote on the poll for the current user
	 * @param poll
	 * @param currentUser
	 * @return
	 */
	public List<Vote> createAllVotes(Poll poll, UserProfile currentUser) {
		List<Vote> result = new ArrayList<>();
		Date startDay = poll.getStartDay();
		Date endDay = poll.getEndDay();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDay);
		int loopPrevention = AdminController.MAX_DAYS*2;
		while (calendar.getTime().before(endDay) || calendar.getTime().equals(endDay)) {
			Vote vote = new Vote();
			vote.setDay(calendar.getTime());
			vote.setVoter(currentUser);
			vote.setChoice(ChoiceEnum.NO); // Default is NO
			vote.setPoll(poll);
			vote = voteDao.save(vote);
			result.add(vote);
			calendar.add(Calendar.DATE, 1); // Move to the next day
			if (--loopPrevention<0) {
				// Should never happen because the poll is validated at creation
				throw new YadaInvalidUsageException("Too many days in the poll"); 
			}
		}
		return result;
	}

}

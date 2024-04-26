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

@Component
public class PollUtil {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private PollDao pollDao;
	@Autowired private VoteDao voteDao;

	public Poll closePoll(Poll poll) {
		try {
			Date winner = pollDao.getPollResult(poll);
			poll.setChosenDay(winner);
			poll = pollDao.save(poll);
			log.debug("Poll {} closed now", poll);
		} catch (Exception e) {
			log.error("Can't close poll {}", poll, e);
		}
		return poll;
	}

	public List<Vote> createAllVotes(UserProfile currentUser) {
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

}

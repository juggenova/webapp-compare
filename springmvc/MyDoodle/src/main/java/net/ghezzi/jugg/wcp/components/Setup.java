package net.ghezzi.jugg.wcp.components;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.ghezzi.jugg.wcp.core.WcpConfiguration;
import net.ghezzi.jugg.wcp.persistence.entity.ChoiceEnum;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;
import net.ghezzi.jugg.wcp.persistence.entity.Vote;
import net.ghezzi.jugg.wcp.persistence.repository.PollDao;
import net.ghezzi.jugg.wcp.persistence.repository.UserProfileDao;
import net.ghezzi.jugg.wcp.persistence.repository.VoteDao;
import net.yadaframework.security.components.YadaUserSetup;

@Component
public class Setup extends YadaUserSetup<UserProfile> {
	private @Autowired PollDao pollDao;
	private @Autowired VoteDao voteDao;
	private @Autowired UserProfileDao userProfileDao;
	private @Autowired WcpConfiguration config;
	
	
	 @Override
	 protected void setupApplication() {
		 // Aggiungo il Poll se non esiste già
		 if (pollDao.count()==0) {
			 Calendar calendar = Calendar.getInstance();
			 calendar.set(2024, Calendar.OCTOBER, 20, 23, 59, 0);
 			 Date deadline = calendar.getTime();
 			 calendar.set(2024, Calendar.NOVEMBER, 1, 0, 0, 0);
 			 Date start = calendar.getTime();
 			 calendar.set(2024, Calendar.NOVEMBER, 7, 0, 0, 0);
 			 Date end = calendar.getTime();
			 //
			 Poll poll = new Poll();
			 poll.setDeadline(deadline);
			 poll.setTitle("Prossima riunione JUG");
			 poll.setDescription("L'argomento dell'incontro sarà \"JavaOne\" in cui i veterani, tra"
			 	+ " una birretta e una grappetta, ricorderanno i bei tempi andati.");
			 poll.setStartDay(start);
			 poll.setEndDay(end);
			 poll = pollDao.save(poll);
		 }
	 }
}


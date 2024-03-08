package net.ghezzi.jugg.wcp.components;

import java.util.List;

import org.springframework.stereotype.Component;

import net.ghezzi.jugg.wcp.persistence.entity.Choice;
import net.ghezzi.jugg.wcp.persistence.entity.Vote;

@Component
public class WcpUtil {

	/**
	 * Check if for the given position in the votes array the value of the choice is
	 * as passed
	 * 
	 * @param votes
	 * @param pos
	 * @param toCheck
	 * @return true or false
	 */
	private boolean checkVote(List<Vote> votes, int pos, Choice toCheck) {
		if (pos >= votes.size()) {
			// Quando non ci sono element, preselezioniamo il NO
			boolean defaultResult = Choice.NO == toCheck ? true : false; 
			return defaultResult;
		}
		Vote vote = votes.get(pos);
		return vote.getChoice() == toCheck;
	}

	public boolean checkYes(List<Vote> votes, int pos) {
		return checkVote(votes, pos, Choice.YES);
	}
	
	public boolean checkNo(List<Vote> votes, int pos) {
		return checkVote(votes, pos, Choice.NO);
	}
	
	public boolean checkMaybe(List<Vote> votes, int pos) {
		return checkVote(votes, pos, Choice.MAYBE);
	}

}

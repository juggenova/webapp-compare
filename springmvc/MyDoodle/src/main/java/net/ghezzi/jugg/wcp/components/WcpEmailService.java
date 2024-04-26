package net.ghezzi.jugg.wcp.components;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ghezzi.jugg.wcp.core.WcpConfiguration;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.yadaframework.components.YadaEmailService;

@Service
public class WcpEmailService {

	@Autowired private YadaEmailService yadaEmailService;
	@Autowired private WcpConfiguration config;
	
	public void notifyPollClosed(Poll poll, String targetEmail, Locale locale) {
		final String emailName = "pollClosed";
		final String[] toEmail = new String[] { targetEmail };
		final String[] replyEmail = config.getEmailFrom();

		final Map<String, Object> templateParams = new HashMap<>();
		templateParams.put("poll", poll);

		String[] subjectParams = new String[] {poll.getTitle()};

		Map<String, String> inlineResources = new HashMap<>();
		// inlineResources.put("logosmall", config.getEmailLogoImage());

		yadaEmailService.sendHtmlEmail(replyEmail, toEmail, replyEmail[0], emailName, subjectParams, templateParams, inlineResources, null, locale, false);
		
	}
}

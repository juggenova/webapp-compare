package net.ghezzi.jugg.wcp.components;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ghezzi.jugg.wcp.core.WcpConfiguration;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.yadaframework.components.YadaEmailBuilder;
import net.yadaframework.components.YadaEmailService;

@Service
public class WcpEmailService {

	@Autowired private YadaEmailService yadaEmailService;
	@Autowired private WcpConfiguration config;
	
	public void notifyPollClosed(Poll poll, String targetEmail, Locale locale) {
		YadaEmailBuilder.instance("pollClosed", locale, yadaEmailService)
			.to(targetEmail)
			.from(config.getEmailFrom())
			.addModelAttribute("poll", poll)
			.subjectParams(poll.getTitle())
			.send();
	}
}

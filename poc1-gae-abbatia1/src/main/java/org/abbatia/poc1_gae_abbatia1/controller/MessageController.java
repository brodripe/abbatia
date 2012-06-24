package org.abbatia.poc1_gae_abbatia1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.ui.Model; 

import java.util.Collection;

import org.abbatia.poc1_gae_abbatia1.model.MessageSQL;
import org.abbatia.poc1_gae_abbatia1.server.MessageRepositorySQL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/messages")
public class MessageController {
	private static final Logger log = LoggerFactory
			.getLogger(MessageController.class);
	
	private MessageRepositorySQL messageRepository = new MessageRepositorySQL();
	
	@RequestMapping(method = RequestMethod.GET)
	public String getMessages(@RequestParam(required = false, value = "id") String id, Model model) {
		System.out.println("Get");
		
		if (log.isDebugEnabled()) {
			log.debug("getMessages");
		}
		
		// delete
		if (id != null) {
			deleteMessage(id);
			
			return "redirect:messages";
		}
		
		MessageSQL message = new MessageSQL();
		model.addAttribute("messageForm", message);

		// get
		Collection<MessageSQL> messages = messageRepository.getAll();
		model.addAttribute("messages", messages);
		
		if (log.isDebugEnabled()) {
			log.debug("messages: " + messages);
		}

		return "index";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	protected String postMessage(@ModelAttribute("messageForm") MessageSQL message) {
		
		if (log.isDebugEnabled()) {
			log.debug("postMessage");
		}
		
		// create
		createMessage(message);
		
		return "redirect:messages";
	}

	protected void createMessage(MessageSQL message) {
		if (log.isDebugEnabled()) {
			log.debug("creating message with text: " + message.getText());
		}

		messageRepository.create(message);
	}

	protected void deleteMessage(String strId) {
		Long id = Long.valueOf(strId);
		if (log.isDebugEnabled()) {
			log.debug("deleting message with id: " + id);
		}
		messageRepository.deleteById(id);
	}

}

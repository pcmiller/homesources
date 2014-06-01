package org.philco.iTunes;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class byItunesController {
	
	private static final Logger logger = LoggerFactory.getLogger(byItunesController.class);
	
	/**
	 * Selects the byItunes view to render by returning its name.
	 */
	@RequestMapping(value = "/byItunes", method = RequestMethod.GET)
	public String byItunes(Locale locale, Model model) {
		logger.info("Welcome to byItunes! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "byItunes";
	}
	
}

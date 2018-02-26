package org.pryos.SimpleGuildBotForDiscord;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;
import org.pryos.SimpleGuildBotForDiscord.Module.VoiceStatusAnounce.VoiceStatusAnounceController;

/**
 * Hello world!
 *
 */
public class App {

	private static final Logger LOGGER = Logger.getLogger(App.class);

	public static void main(String[] args) {

		ApplicationController oApp = new ApplicationController();
		try {
			oApp.loadModule(VoiceStatusAnounceController.class);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			LOGGER.error(e.getMessage(), e);
		}
		oApp.login();

	}

}

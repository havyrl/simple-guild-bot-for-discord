package org.pryos.SimpleGuildBotForDiscord;

import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;
import org.pryos.SimpleGuildBotForDiscord.Module.DiscordEventLogModule;
import org.pryos.SimpleGuildBotForDiscord.Module.VoiceStatusAnounce.VoiceStatusAnounceModule;

/**
 * Hello world!
 *
 */
public class App {

	private static final Logger LOGGER = Logger.getLogger(App.class);

	public static void main(String[] args) {

		ApplicationController oApp = new ApplicationController();
		if (LOGGER.isDebugEnabled()) {
			oApp.loadModule(DiscordEventLogModule.class);
		}
		oApp.loadModule(VoiceStatusAnounceModule.class);
		oApp.login();

	}

}

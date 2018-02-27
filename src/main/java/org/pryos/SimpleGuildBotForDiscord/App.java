package org.pryos.SimpleGuildBotForDiscord;

import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;
import org.pryos.SimpleGuildBotForDiscord.Module.BotRenameModule;
import org.pryos.SimpleGuildBotForDiscord.Module.DiscordEventLogModule;
import org.pryos.SimpleGuildBotForDiscord.Module.VoiceStatusAnounce.VoiceStatusAnounceModule;

/**
 * Hello world!
 *
 */
public class App {

	private static final Logger LOGGER = Logger.getLogger(App.class);

	public static void main(String[] args) {

		ApplicationController oApp = null;
		try {
			oApp = new ApplicationController();
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage(), e);
		}
		if (oApp != null) {
			if (ApplicationController.isLocal() && LOGGER.isDebugEnabled()) {
				oApp.loadModule(DiscordEventLogModule.class);
			}
			oApp.loadModule(BotRenameModule.class);
			oApp.loadModule(VoiceStatusAnounceModule.class);
			oApp.login();
		}
	}

}
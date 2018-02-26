package org.pryos.SimpleGuildBotForDiscord.Module;

import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;

import sx.blah.discord.api.IDiscordClient;

public class BotControlModule extends AbstractBotModule {

	protected BotControlModule(ApplicationController oApp, IDiscordClient oDiscordApi) {
		super(oApp, oDiscordApi);
	}

}

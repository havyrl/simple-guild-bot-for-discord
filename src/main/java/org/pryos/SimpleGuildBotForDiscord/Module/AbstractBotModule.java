package org.pryos.SimpleGuildBotForDiscord.Module;

import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

public abstract class AbstractBotModule {

	protected ApplicationController oApp;
	protected IDiscordClient oDiscordApi;

	protected AbstractBotModule(ApplicationController oApp, IDiscordClient oDiscordApi) {
		this.oApp = oApp;
		this.oDiscordApi = oDiscordApi;
		EventDispatcher oDispatcher = oDiscordApi.getDispatcher();
		oDispatcher.registerListener(this);
	}

}

package org.pryos.SimpleGuildBotForDiscord.Module;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

public abstract class AbstractBotModule {

	protected IDiscordClient oDiscordApi;

	protected AbstractBotModule(IDiscordClient oDiscordApi) {
		this.oDiscordApi = oDiscordApi;
		EventDispatcher oDispatcher = oDiscordApi.getDispatcher();
		oDispatcher.registerListener(this);
	}

}

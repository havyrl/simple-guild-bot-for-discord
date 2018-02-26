package org.pryos.SimpleGuildBotForDiscord.Module;

import sx.blah.discord.api.IDiscordClient;

public abstract class AbstractBotModule {

	private IDiscordClient oDiscordApi;

	protected AbstractBotModule(IDiscordClient oDiscordApi) {
		this.oDiscordApi = oDiscordApi;
	}

}

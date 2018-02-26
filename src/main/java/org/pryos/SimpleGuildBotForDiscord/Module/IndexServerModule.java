package org.pryos.SimpleGuildBotForDiscord.Module;

import org.apache.log4j.Logger;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IGuild;

public class IndexServerModule extends AbstractBotModule {

	private final Logger oLogger = Logger.getLogger(getClass());

	public IndexServerModule(IDiscordClient oDiscordApi) {
		super(oDiscordApi);
	}

	@EventSubscriber
	public void handle(GuildCreateEvent oEvent) {
		IGuild oGuild = oEvent.getGuild();

	}

}

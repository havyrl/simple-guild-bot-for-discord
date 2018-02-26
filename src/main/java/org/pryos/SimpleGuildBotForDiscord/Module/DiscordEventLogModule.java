package org.pryos.SimpleGuildBotForDiscord.Module;

import org.apache.log4j.Logger;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildEvent;
import sx.blah.discord.handle.impl.events.module.ModuleEvent;
import sx.blah.discord.handle.impl.events.shard.ShardEvent;
import sx.blah.discord.handle.impl.events.user.UserEvent;
import sx.blah.discord.util.audio.events.AudioPlayerEvent;

public class DiscordEventLogModule extends AbstractBotModule {

	private final Logger oLogger = Logger.getLogger(getClass());

	public DiscordEventLogModule(IDiscordClient oDiscordApi) {
		super(oDiscordApi);
	}

	@EventSubscriber
	public void handle(ReadyEvent oEvent) {
		oLogger.debug(ReadyEvent.class.getSimpleName() + ": " + oEvent.getClass().getName());
	}

	@EventSubscriber
	public void handle(GuildEvent oEvent) {
		oLogger.debug(GuildEvent.class.getSimpleName() + ": " + oEvent.getClass().getName());
	}

	@EventSubscriber
	public void handle(ModuleEvent oEvent) {
		oLogger.debug(ModuleEvent.class.getSimpleName() + ": " + oEvent.getClass().getName());
	}

	@EventSubscriber
	public void handle(ShardEvent oEvent) {
		oLogger.debug(ShardEvent.class.getSimpleName() + ": " + oEvent.getClass().getName());
	}

	@EventSubscriber
	public void handle(UserEvent oEvent) {
		oLogger.debug(UserEvent.class.getSimpleName() + ": " + oEvent.getClass().getName());
	}

	@EventSubscriber
	public void handle(AudioPlayerEvent oEvent) {
		oLogger.debug(AudioPlayerEvent.class.getSimpleName() + ": " + oEvent.getClass().getName());
	}

}

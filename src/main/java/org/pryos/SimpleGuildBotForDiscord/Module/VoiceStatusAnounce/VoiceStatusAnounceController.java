package org.pryos.SimpleGuildBotForDiscord.Module.VoiceStatusAnounce;

import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Module.AbstractBotModule;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildEvent;
import sx.blah.discord.handle.impl.events.module.ModuleEvent;
import sx.blah.discord.handle.impl.events.shard.ShardEvent;
import sx.blah.discord.handle.impl.events.user.UserEvent;
import sx.blah.discord.util.audio.events.AudioPlayerEvent;

public class VoiceStatusAnounceController extends AbstractBotModule {

	private final Logger oLogger = Logger.getLogger(getClass());

	public VoiceStatusAnounceController(IDiscordClient oDiscordApi) {
		super(oDiscordApi);
		EventDispatcher oDispatcher = oDiscordApi.getDispatcher();
		oDispatcher.registerListener(this);
	}

	@EventSubscriber
	public void handle(ReadyEvent event) {
		oLogger.info(event);
	}

	@EventSubscriber
	public void handle(GuildEvent event) {
		oLogger.info(event);
	}

	@EventSubscriber
	public void handle(ModuleEvent event) {
		oLogger.info(event);
	}

	@EventSubscriber
	public void handle(ShardEvent event) {
		oLogger.info(event);
	}

	@EventSubscriber
	public void handle(UserEvent event) {
		oLogger.info(event);
	}

	@EventSubscriber
	public void handle(AudioPlayerEvent event) {
		oLogger.info(event);
	}

}

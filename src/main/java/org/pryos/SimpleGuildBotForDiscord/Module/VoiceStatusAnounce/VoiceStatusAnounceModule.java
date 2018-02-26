package org.pryos.SimpleGuildBotForDiscord.Module.VoiceStatusAnounce;

import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Module.AbstractBotModule;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildEvent;
import sx.blah.discord.handle.impl.events.module.ModuleEvent;
import sx.blah.discord.handle.impl.events.shard.ShardEvent;
import sx.blah.discord.handle.impl.events.user.UserEvent;
import sx.blah.discord.util.audio.events.AudioPlayerEvent;

public class VoiceStatusAnounceModule extends AbstractBotModule {

	private final Logger oLogger = Logger.getLogger(getClass());

	public VoiceStatusAnounceModule(IDiscordClient oDiscordApi) {
		super(oDiscordApi);
	}

	@EventSubscriber
	public void handle(ReadyEvent event) {

	}

	@EventSubscriber
	public void handle(GuildEvent event) {

	}

	@EventSubscriber
	public void handle(ModuleEvent event) {

	}

	@EventSubscriber
	public void handle(ShardEvent event) {

	}

	@EventSubscriber
	public void handle(UserEvent event) {

	}

	@EventSubscriber
	public void handle(AudioPlayerEvent event) {

	}

}

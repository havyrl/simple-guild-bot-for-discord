package org.pryos.SimpleGuildBotForDiscord.Module;

import java.util.EnumSet;

import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;
import org.pryos.SimpleGuildBotForDiscord.Application.GuildConfiguration;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelUpdateEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.PermissionOverride;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.cache.LongMap;

public class IndexServerModule extends AbstractBotModule {

	private final Logger oLogger = Logger.getLogger(getClass());

	public IndexServerModule(ApplicationController oApp, IDiscordClient oDiscordApi) {
		super(oApp, oDiscordApi);
	}

	@EventSubscriber
	public void handle(GuildCreateEvent oEvent) {
		reindexChannels(oEvent.getGuild());
	}

	@EventSubscriber
	public void handle(GuildUpdateEvent oEvent) {
		reindexChannels(oEvent.getGuild());
	}

	@EventSubscriber
	public void handle(GuildLeaveEvent oEvent) {
		reindexChannels(oEvent.getGuild());
	}

	@EventSubscriber
	public void handle(ChannelCreateEvent oEvent) {
		reindexChannels(oEvent.getGuild());
	}

	@EventSubscriber
	public void handle(ChannelUpdateEvent oEvent) {
		reindexChannels(oEvent.getGuild());
	}

	@EventSubscriber
	public void handle(ChannelDeleteEvent oEvent) {
		reindexChannels(oEvent.getGuild());
	}

	private void reindexChannels(IGuild oGuild) {
		GuildConfiguration oGuildConfig = oApp.getGuildSettings(oGuild);

		oGuildConfig.setDefaultTextChannel(null);
		oGuildConfig.setDefaultVoiceChannel(null);

		// default channel
		IChannel oDefaultChannel = oGuild.getDefaultChannel();
		EnumSet<Permissions> oDefaultChannelPermissions = oDefaultChannel
				.getModifiedPermissions(oDiscordApi.getOurUser());
		if (oDefaultChannel instanceof IVoiceChannel) {
			if (oDefaultChannelPermissions.contains(Permissions.VOICE_SPEAK)) {
				IVoiceChannel oVoiceChannel = (IVoiceChannel) oDefaultChannel;
				oGuildConfig.setDefaultVoiceChannel(oVoiceChannel);
				oLogger.info("voice: " + oVoiceChannel.getName());
			}
		} else {
			if (oDefaultChannelPermissions.contains(Permissions.SEND_MESSAGES)) {
				oGuildConfig.setDefaultTextChannel(oDefaultChannel);
				oLogger.info("text: " + oDefaultChannel.getName());
			}
		}

		// specific channel
		for (IChannel oChannel : oGuild.getChannels()) {
			LongMap<PermissionOverride> mapOverrides = oChannel.getUserOverrides();
			PermissionOverride oOverride = mapOverrides.get(oDiscordApi.getOurUser().getLongID());
			if (oOverride == null) {
				continue;
			}
			if (oChannel instanceof IVoiceChannel) {
				IVoiceChannel oVoiceChannel = (IVoiceChannel) oChannel;
				if (oOverride.allow().contains(Permissions.VOICE_SPEAK)) {
					oGuildConfig.setDefaultVoiceChannel(oVoiceChannel);
				}
			} else {
				if (oOverride.allow().contains(Permissions.SEND_MESSAGES)) {
					oGuildConfig.setDefaultTextChannel(oChannel);
				}
			}
		}
	}

}

package org.pryos.SimpleGuildBotForDiscord.Module.VoiceStatusAnounce;

import java.util.EnumSet;

import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;
import org.pryos.SimpleGuildBotForDiscord.Module.AbstractBotModule;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.obj.Permissions;

public class VoiceStatusAnounceModule extends AbstractBotModule {

	// private static final String JOIN_MESSAGE = "%s has joind %s.";
	// private static final String MOVE_MESSAGE = "%s has moved from %s to %s.";
	// private static final String LEAVE_MESSAGE = "%s has left %s.";

	private static final String JOIN_MESSAGE = "%s ist dem Kanal %s beigetreten.";
	private static final String MOVE_MESSAGE = "%s ist von Kanal %s zu Kanal %s gewechselt.";
	private static final String LEAVE_MESSAGE = "%s hat den Kanal %s verlassen.";

	public VoiceStatusAnounceModule(ApplicationController oApp, IDiscordClient oDiscordApi) {
		super(oApp, oDiscordApi);
	}

	@EventSubscriber
	public void handle(UserVoiceChannelJoinEvent oEvent) {
		String sNick = oEvent.getUser().getNicknameForGuild(oEvent.getGuild());
		EnumSet<Permissions> setPermissions = oEvent.getVoiceChannel().getModifiedPermissions(oDiscordApi.getOurUser());
		if (setPermissions.contains(Permissions.VOICE_CONNECT)) {
			oApp.sendMessage(oEvent.getGuild(), String.format(JOIN_MESSAGE, sNick, oEvent.getVoiceChannel().getName()));
		}
	}

	@EventSubscriber
	public void handle(UserVoiceChannelMoveEvent oEvent) {
		String sNick = oEvent.getUser().getNicknameForGuild(oEvent.getGuild());
		EnumSet<Permissions> setOldPermissions = oEvent.getOldChannel()
				.getModifiedPermissions(oDiscordApi.getOurUser());
		EnumSet<Permissions> setNewPermissions = oEvent.getNewChannel()
				.getModifiedPermissions(oDiscordApi.getOurUser());
		if (setOldPermissions.contains(Permissions.VOICE_CONNECT)
				&& setNewPermissions.contains(Permissions.VOICE_CONNECT)) {
			oApp.sendMessage(oEvent.getGuild(), String.format(MOVE_MESSAGE, sNick, oEvent.getOldChannel().getName(),
					oEvent.getNewChannel().getName()));
		} else if (!setOldPermissions.contains(Permissions.VOICE_CONNECT)
				&& setNewPermissions.contains(Permissions.VOICE_CONNECT)) {
			oApp.sendMessage(oEvent.getGuild(), String.format(JOIN_MESSAGE, sNick, oEvent.getNewChannel().getName()));
		} else if (setOldPermissions.contains(Permissions.VOICE_CONNECT)
				&& !setNewPermissions.contains(Permissions.VOICE_CONNECT)) {
			oApp.sendMessage(oEvent.getGuild(), String.format(LEAVE_MESSAGE, sNick, oEvent.getOldChannel().getName()));
		}

	}

	@EventSubscriber
	public void handle(UserVoiceChannelLeaveEvent oEvent) {
		String sNick = oEvent.getUser().getNicknameForGuild(oEvent.getGuild());
		EnumSet<Permissions> setPermissions = oEvent.getVoiceChannel().getModifiedPermissions(oDiscordApi.getOurUser());
		if (setPermissions.contains(Permissions.VOICE_CONNECT)) {
			oApp.sendMessage(oEvent.getGuild(),
					String.format(LEAVE_MESSAGE, sNick, oEvent.getVoiceChannel().getName()));
		}
	}
}

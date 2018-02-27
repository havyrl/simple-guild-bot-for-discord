package org.pryos.SimpleGuildBotForDiscord.Application;

import org.apache.log4j.Logger;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class GuildConfiguration {

	private final Logger oLogger = Logger.getLogger(getClass());

	private IGuild guild;
	private IChannel defaultTextChannel;
	private IVoiceChannel defaultVoiceChannel;

	public GuildConfiguration(IGuild oGuild) {
		setGuild(oGuild);
	}

	public IGuild getGuild() {
		return guild;
	}

	public void setGuild(IGuild oGuild) {
		this.guild = oGuild;
	}

	public long getId() {
		return getGuild().getLongID();
	}

	public IChannel getDefaultTextChannel() {
		return defaultTextChannel;
	}

	public GuildConfiguration setDefaultTextChannel(IChannel defaultTextChannel) {
		this.defaultTextChannel = defaultTextChannel;
		return this;
	}

	public IVoiceChannel getDefaultVoiceChannel() {
		return defaultVoiceChannel;
	}

	public GuildConfiguration setDefaultVoiceChannel(IVoiceChannel defaultVoiceChannel) {
		this.defaultVoiceChannel = defaultVoiceChannel;
		return this;
	}

	public void sendMessage(String sMessage) {
		if (getDefaultTextChannel() != null) {
			getDefaultTextChannel().sendMessage(sMessage);
		} else {
			oLogger.warn(
					"missing default text channel for server: " + getGuild().getName() + "|" + getGuild().getLongID());
		}
	}

}

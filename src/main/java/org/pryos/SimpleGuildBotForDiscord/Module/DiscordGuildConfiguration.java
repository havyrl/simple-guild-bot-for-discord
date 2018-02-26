package org.pryos.SimpleGuildBotForDiscord.Module;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class DiscordGuildConfiguration {

	private IGuild guild;
	private IChannel defaultTextChannel;
	private IVoiceChannel defaultVoiceChannel;

	public DiscordGuildConfiguration(IGuild oGuild) {
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

	public DiscordGuildConfiguration setDefaultTextChannel(IChannel defaultTextChannel) {
		this.defaultTextChannel = defaultTextChannel;
		return this;
	}

	public IVoiceChannel getDefaultVoiceChannel() {
		return defaultVoiceChannel;
	}

	public DiscordGuildConfiguration setDefaultVoiceChannel(IVoiceChannel defaultVoiceChannel) {
		this.defaultVoiceChannel = defaultVoiceChannel;
		return this;
	}
}

package org.pryos.SimpleGuildBotForDiscord.Module;

import java.util.EnumSet;

import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserRoleUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.role.RoleUpdateEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class BotRenameModule extends AbstractBotModule {

	public BotRenameModule(ApplicationController oApp, IDiscordClient oDiscordApi) {
		super(oApp, oDiscordApi);
	}

	@EventSubscriber
	public void handle(GuildCreateEvent oEvent) {
		checkBotName(oEvent.getGuild());
	}

	@EventSubscriber
	public void handle(GuildUpdateEvent oEvent) {
		checkBotName(oEvent.getGuild());
	}

	@EventSubscriber
	public void handle(UserRoleUpdateEvent oEvent) {
		checkBotName(oEvent.getGuild());
	}

	@EventSubscriber
	public void handle(RoleUpdateEvent oEvent) {
		checkBotName(oEvent.getGuild());
	}

	private void checkBotName(IGuild oGuild) {
		IUser oBot = oDiscordApi.getOurUser();
		String sGuildName = oGuild.getName();
		String sBotName = oBot.getNicknameForGuild(oGuild);

		if (!sGuildName.equals(sBotName)) {
			EnumSet<Permissions> setPermissions = oBot.getPermissionsForGuild(oGuild);
			if (setPermissions.contains(Permissions.CHANGE_NICKNAME)) {
				oGuild.setUserNickname(oBot, sGuildName);
			}
		}
	}

}

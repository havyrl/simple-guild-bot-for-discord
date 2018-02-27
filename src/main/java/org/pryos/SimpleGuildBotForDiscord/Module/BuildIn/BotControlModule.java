package org.pryos.SimpleGuildBotForDiscord.Module.BuildIn;

import java.util.EnumSet;
import java.util.List;

import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;
import org.pryos.SimpleGuildBotForDiscord.Application.ICommandHandler;
import org.pryos.SimpleGuildBotForDiscord.Module.AbstractBotModule;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class BotControlModule extends AbstractBotModule implements ICommandHandler {

	private enum Command {
		PING, LEAVE, RESTART
	}

	private static final String MESSAGE_RESTART = "Restarting. See you soon";
	private static final String MESSAGE_LEAVE = "Good bye :-(";
	private static final String MESSAGE_PING = "PONG!";

	public BotControlModule(ApplicationController oApp, IDiscordClient oDiscordApi) {
		super(oApp, oDiscordApi);
		for (Command eCommand : Command.values()) {
			oApp.registerCommand(eCommand.name(), this);
		}
	}

	private void leaveServer(IMessage oMessage) {
		oMessage.reply(String.format(MESSAGE_LEAVE));
		oMessage.getGuild().leave();
	}

	private void ping(IUser oUser) {
		IPrivateChannel oPM = oUser.getOrCreatePMChannel();
		oPM.sendMessage(String.format(MESSAGE_PING));
	}

	private void restartBot() {
		oApp.sendMessage2All(String.format(MESSAGE_RESTART));
		Thread.currentThread().interrupt();
		return;
	}

	@Override
	public void handle(String sCommand, List<String> lstArguments, MessageReceivedEvent oEvent) {
		IGuild oGuild = oEvent.getGuild();
		EnumSet<Permissions> lstPermissions = oEvent.getAuthor().getPermissionsForGuild(oGuild);
		boolean isAdmin = lstPermissions.contains(Permissions.ADMINISTRATOR);
		if (isAdmin) {
			Command eCommand = Command.valueOf(sCommand.toUpperCase());
			switch (eCommand) {
			case LEAVE:
				leaveServer(oEvent.getMessage());
				break;
			case PING:
				ping(oEvent.getAuthor());
				break;
			case RESTART:
				restartBot();
				return;
			}
		}
	}

}

package org.pryos.SimpleGuildBotForDiscord.Module.BuildIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Application.ApplicationController;
import org.pryos.SimpleGuildBotForDiscord.Module.AbstractBotModule;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class BotCommandlModule extends AbstractBotModule {

	private static final String BOT_PREFIX = "!";
	private final Logger oLogger = Logger.getLogger(getClass());

	public BotCommandlModule(ApplicationController oApp, IDiscordClient oDiscordApi) {
		super(oApp, oDiscordApi);
	}

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent oEvent) {

		// Note for error handling, you'll probably want to log failed commands with a
		// logger or sout
		// In most cases it's not advised to annoy the user with a reply incase they
		// didn't intend to trigger a
		// command anyway, such as a user typing ?notacommand, the bot should not say
		// "notacommand" doesn't exist in
		// most situations. It's partially good practise and partially developer
		// preference

		// Given a message "/test arg1 arg2", argArray will contain ["/test", "arg1",
		// "arg"]
		String[] argArray = oEvent.getMessage().getContent().split(" ");

		// First ensure at least the command and prefix is present, the arg length can
		// be handled by your command func
		if (argArray.length == 0)
			return;

		// Check if the first arg (the command) starts with the prefix defined in the
		// utils class
		if (!argArray[0].startsWith(BOT_PREFIX))
			return;

		// Extract the "command" part of the first arg out by just ditching the first
		// character
		String sCommand = argArray[0].substring(1);

		// Load the rest of the args in the array into a List for safer access
		List<String> lstArguments = new ArrayList<>(Arrays.asList(argArray));
		lstArguments.remove(0); // Remove the command

		// Begin the switch to handle the string to command mappings. It's likely wise
		// to pass the whole event or
		// some part (IChannel) to the command handling it
		if (oLogger.isTraceEnabled()) {
			oLogger.trace(String.format("doCommand: %s", sCommand));
		}
		oApp.doCommand(sCommand, lstArguments, oEvent);

	}

}

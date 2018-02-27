package org.pryos.SimpleGuildBotForDiscord.Application;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public interface ICommandHandler {

	void handle(String sCommand, List<String> lstArguments, MessageReceivedEvent oEvent);

}

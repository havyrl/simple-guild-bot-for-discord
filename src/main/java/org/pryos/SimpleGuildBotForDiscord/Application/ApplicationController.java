package org.pryos.SimpleGuildBotForDiscord.Application;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Module.AbstractBotModule;
import org.pryos.SimpleGuildBotForDiscord.Module.BuildIn.BotCommandlModule;
import org.pryos.SimpleGuildBotForDiscord.Module.BuildIn.BotControlModule;
import org.pryos.SimpleGuildBotForDiscord.Module.BuildIn.IndexServerModule;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;

public class ApplicationController {

	private static final boolean IS_LOCAL = ApplicationController.class.getProtectionDomain().getCodeSource()
			.getLocation().getFile().contains(".jar");
	private final String CONFIG_FILE = "/config.properties";
	private final String CONFIG_FILE_TEST = "config-local.properties";

	private final Logger oLogger = Logger.getLogger(getClass());
	private final Properties oConfig = new Properties();
	private final IDiscordClient oDiscordApi;
	private final Map<IGuild, GuildConfiguration> mapGuildConfig = new HashMap<>();
	private final Map<Class<? extends AbstractBotModule>, AbstractBotModule> mapModules = new HashMap<>();
	private final Map<String, List<? extends ICommandHandler>> mapHandler = new HashMap<>();

	public ApplicationController() throws URISyntaxException {
		// load config
		loadConfig();

		String sToken = (String) oConfig.get("auth.token");
		if (StringUtils.isAnyEmpty(sToken)) {
			throw new IllegalArgumentException("token must be not empty");
		}
		oDiscordApi = new ClientBuilder().withToken(sToken).build();

		// index servers
		loadModule(IndexServerModule.class);
		loadModule(BotCommandlModule.class);
		loadModule(BotControlModule.class);
	}

	public void doCommand(String sCommand, List<String> lstArguments, MessageReceivedEvent oEvent) {
		List<ICommandHandler> lstHandlers = getHandlers(sCommand, true);
		for (ICommandHandler oHandler : lstHandlers) {
			oHandler.handle(sCommand, lstArguments, oEvent);
		}
	}

	public <L extends ICommandHandler> void registerCommand(String sCommand, L oHandler) {
		List<ICommandHandler> lstHandlers = getHandlers(sCommand, true);
		if (!lstHandlers.contains(oHandler)) {
			lstHandlers.add(oHandler);
		}
	}

	private <L extends ICommandHandler> List<L> getHandlers(String sCommand, boolean bAdd) {
		@SuppressWarnings("unchecked")
		List<L> lstHandlers = (List<L>) mapHandler.get(sCommand.toLowerCase());
		if (lstHandlers == null) {
			lstHandlers = new ArrayList<>();
			if (bAdd) {
				mapHandler.put(sCommand.toLowerCase(), lstHandlers);
			}
		}
		return lstHandlers;
	}

	public <M extends AbstractBotModule> M loadModule(Class<M> oModuleClass) {
		@SuppressWarnings("unchecked")
		M oModule = (M) mapModules.get(oModuleClass);
		if (oModule == null) {
			Constructor<M> oModuleConstrcutor;
			try {
				oModuleConstrcutor = oModuleClass.getDeclaredConstructor(ApplicationController.class,
						IDiscordClient.class);
				oModule = oModuleConstrcutor.newInstance(this, oDiscordApi);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				oLogger.error(e.getMessage(), e);
			}
			if (oModule != null) {
				mapModules.put(oModuleClass, oModule);
			}
		}
		return oModule;
	}

	private void loadConfig() {
		try {
			// config in jar
			if (isLocal()) {
				oLogger.info(String.format("read resource %s", CONFIG_FILE));
				oConfig.load(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			} else { // test config in target dir
				File oFile = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI())
						.resolve(CONFIG_FILE_TEST).toFile();
				oLogger.info(String.format("read file %s", oFile.getAbsolutePath()));
				oConfig.load(new FileInputStream(oFile));
			}
		} catch (Exception e) {
			oLogger.error(e.getMessage(), e);
		}
	}

	public void login() {
		if (!oDiscordApi.isLoggedIn()) {
			oDiscordApi.login();
		}
	}

	public GuildConfiguration getGuildSettings(IGuild oGuild) {
		GuildConfiguration oServer = mapGuildConfig.get(oGuild);
		if (oServer == null) {
			oServer = new GuildConfiguration(oGuild);
			mapGuildConfig.put(oGuild, oServer);
		}
		return oServer;
	}

	public Collection<GuildConfiguration> getGuilds() {
		return mapGuildConfig.values();
	}

	public void sendMessage(IGuild oGuild, String sMessage) {
		GuildConfiguration oServer = getGuildSettings(oGuild);
		oServer.sendMessage(sMessage);
	}

	public void sendMessage2All(String sMessage) {
		for (GuildConfiguration oGuildConfig : getGuilds()) {
			oGuildConfig.sendMessage(sMessage);
		}
	}

	public static boolean isLocal() {
		return IS_LOCAL;
	}

}

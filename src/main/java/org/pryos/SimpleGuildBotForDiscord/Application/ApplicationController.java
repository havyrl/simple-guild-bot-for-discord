package org.pryos.SimpleGuildBotForDiscord.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Module.AbstractBotModule;
import org.pryos.SimpleGuildBotForDiscord.Module.DiscordGuildConfiguration;
import org.pryos.SimpleGuildBotForDiscord.Module.IndexServerModule;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

public class ApplicationController {

	private final String CONFIG_FILE = "config.properties";
	private final Path oPath;
	private final Logger oLogger = Logger.getLogger(getClass());
	private final Properties oConfig = new Properties();
	private final IDiscordClient oDiscordApi;
	private final Map<IGuild, DiscordGuildConfiguration> mapGuildConfig = new HashMap<>();
	private final Map<Class<? extends AbstractBotModule>, AbstractBotModule> mapModules = new HashMap<>();

	public ApplicationController() throws URISyntaxException {
		oPath = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		loadConfig();

		String sToken = (String) oConfig.get("auth.token");
		if (StringUtils.isAnyEmpty(sToken)) {
			throw new IllegalArgumentException("token must be not empty");
		}
		oDiscordApi = new ClientBuilder().withToken(sToken).build();

		// index servers
		loadModule(IndexServerModule.class);
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
		Path oFilePath = oPath.resolve(CONFIG_FILE);
		File oFile = oFilePath.toFile();
		if (oFile.exists()) {
			try {
				FileInputStream oStream = new FileInputStream(oFile);
				oConfig.load(oStream);
			} catch (IOException e) {
				oLogger.error(e.getMessage(), e);
			}
		} else {
			oConfig.setProperty("auth.token", StringUtils.EMPTY);
			try {
				oConfig.store(new FileOutputStream(oFile), StringUtils.EMPTY);
			} catch (IOException e) {
				oLogger.error(e.getMessage(), e);
			}
		}
	}

	public void login() {
		if (!oDiscordApi.isLoggedIn()) {
			oDiscordApi.login();
		}
	}

	public DiscordGuildConfiguration getGuildSettings(IGuild oGuild) {
		DiscordGuildConfiguration oServer = mapGuildConfig.get(oGuild);
		if (oServer == null) {
			oServer = new DiscordGuildConfiguration(oGuild);
			mapGuildConfig.put(oGuild, oServer);
		}
		return oServer;
	}

	public void sendMessage(IGuild oGuild, String sMessage) {
		DiscordGuildConfiguration oServer = getGuildSettings(oGuild);
		if (oServer.getDefaultTextChannel() != null) {
			oServer.getDefaultTextChannel().sendMessage(sMessage);
		} else {
			oLogger.warn("missing default text channel for server: " + oGuild.getName() + "|" + oGuild.getLongID());
		}
	}

}

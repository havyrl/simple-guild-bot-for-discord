package org.pryos.SimpleGuildBotForDiscord.Application;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Module.AbstractBotModule;
import org.pryos.SimpleGuildBotForDiscord.Module.IndexServerModule;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

public class ApplicationController {

	private final String CONFIG_FILE = "/config.properties";
	private final String CONFIG_FILE_TEST = "config-local.properties";
	private final Logger oLogger = Logger.getLogger(getClass());
	private final Properties oConfig = new Properties();
	private final IDiscordClient oDiscordApi;
	private final Map<IGuild, GuildConfiguration> mapGuildConfig = new HashMap<>();
	private final Map<Class<? extends AbstractBotModule>, AbstractBotModule> mapModules = new HashMap<>();

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
		URL oLocation = getClass().getProtectionDomain().getCodeSource().getLocation();
		try {
			// config in jar
			if (oLocation.getFile().contains(".jar")) {
				oLogger.info(String.format("read resource %s", CONFIG_FILE));
				oConfig.load(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			} else { // test config in target dir
				File oFile = Paths.get(oLocation.toURI()).resolve(CONFIG_FILE_TEST).toFile();
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

	public void sendMessage(IGuild oGuild, String sMessage) {
		GuildConfiguration oServer = getGuildSettings(oGuild);
		if (oServer.getDefaultTextChannel() != null) {
			oServer.getDefaultTextChannel().sendMessage(sMessage);
		} else {
			oLogger.warn("missing default text channel for server: " + oGuild.getName() + "|" + oGuild.getLongID());
		}
	}

}

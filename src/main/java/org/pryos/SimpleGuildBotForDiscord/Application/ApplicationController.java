package org.pryos.SimpleGuildBotForDiscord.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pryos.SimpleGuildBotForDiscord.Module.AbstractBotModule;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class ApplicationController {

	private final String CONFIG_FILE = "config.properties";
	private final Logger oLogger = Logger.getLogger(getClass());
	private final Properties oConfig = new Properties();
	private final IDiscordClient oDiscordApi;

	public ApplicationController() {

		loadConfig();

		String sToken = (String) oConfig.get("auth.token");
		if (StringUtils.isAnyEmpty(sToken)) {
			throw new IllegalArgumentException("token must be not empty");
		}
		ClientBuilder oDiscordApiBuilder = new ClientBuilder();
		oDiscordApiBuilder.withToken(sToken);
		oDiscordApi = oDiscordApiBuilder.build();

	}

	public AbstractBotModule loadModule(Class<? extends AbstractBotModule> oModuleClass)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Constructor<? extends AbstractBotModule> oModuleConstrcutor = oModuleClass
				.getDeclaredConstructor(IDiscordClient.class);
		return oModuleConstrcutor.newInstance(oDiscordApi);
	}

	private void loadConfig() {
		File oFile = new File(CONFIG_FILE);
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
}

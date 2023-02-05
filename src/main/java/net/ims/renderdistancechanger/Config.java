package net.ims.renderdistancechanger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

/**
 * A class dedicated to storing the config values of shaderpacks. Right now it only stores the path to the current shaderpack
 */
public class Config {
	private static final String COMMENT =
		"This file stores configuration options for RD Changer";

	/**
	 * The path to the current shaderpack. Null if the internal shaderpack is being used.
	 */
	public int daytimeSwitch;
	public int nighttimeSwitch;
	public int daytimeRd;
	public int nighttimeRd;
	public boolean tellWhenChanged;

	private final Path propertiesPath;

	public Config(Path propertiesPath) {
		daytimeSwitch = 1000;
		nighttimeSwitch = 12000;
		daytimeRd = 8;
		nighttimeRd = 4;
		tellWhenChanged = true;
		this.propertiesPath = propertiesPath;
	}

	/**
	 * Initializes the configuration, loading it if it is present and creating a default config otherwise.
	 *
	 * @throws IOException file exceptions
	 */
	public void initialize() throws IOException {
		load();
		if (!Files.exists(propertiesPath)) {
			save();
		}
	}

	/**
	 * loads the config file and then populates the string, int, and boolean entries with the parsed entries
	 *
	 * @throws IOException if the file cannot be loaded
	 */

	public void load() throws IOException {
		if (!Files.exists(propertiesPath)) {
			return;
		}

		Properties properties = new Properties();
		// NB: This uses ISO-8859-1 with unicode escapes as the encoding
		try (InputStream is = Files.newInputStream(propertiesPath)) {
			properties.load(is);
		}
		daytimeSwitch = Integer.parseInt(properties.getProperty("daytimeSwitch"));
		nighttimeSwitch = Integer.parseInt(properties.getProperty("nighttimeSwitch"));
		daytimeRd = Integer.parseInt(properties.getProperty("daytimeRd"));
		nighttimeRd = Integer.parseInt(properties.getProperty("nighttimeRd"));
		tellWhenChanged = Boolean.parseBoolean(properties.getProperty("tellWhenChanged"));
	}

	/**
	 * Serializes the config into a file. Should be called whenever any config values are modified.
	 *
	 * @throws IOException file exceptions
	 */
	public void save() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("daytimeSwitch", String.valueOf(daytimeSwitch));
		properties.setProperty("nighttimeSwitch", String.valueOf(nighttimeSwitch));
		properties.setProperty("daytimeRd", String.valueOf(daytimeRd));
		properties.setProperty("nighttimeRd", String.valueOf(nighttimeSwitch));
		properties.setProperty("tellWhenChanged", String.valueOf(tellWhenChanged));
		// NB: This uses ISO-8859-1 with unicode escapes as the encoding
		try (OutputStream os = Files.newOutputStream(propertiesPath)) {
			properties.store(os, COMMENT);
		}
	}
}

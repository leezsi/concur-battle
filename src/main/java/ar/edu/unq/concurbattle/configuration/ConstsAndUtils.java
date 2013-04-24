package ar.edu.unq.concurbattle.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import ar.edu.unq.concurbattle.comunication.Utils;
import ar.edu.unq.concurbattle.exception.ConcurBattleException;

public class ConstsAndUtils {

	public static final String CONFIG_FILE = "concurbattles.properties";
	public static String MOVE_STRATEGY;
	public static long DEFAULT_SLEEP;
	public static int GUI_SERVER_CHANNEL;
	public static PropertiesConfiguration CONFIGS;
	public static int GUI_SEND_CHANNEL;
	public static int GUI_RECEIVE_CHANNEL;
	public static int NEXT_CLIENT_CHANNEL;
	public static int SERVER_CHANNEL;
	public static int LOCK_CHANNEL;
	public static int CLIENT_CHANNEL;

	static {
		try {
			ConstsAndUtils.CONFIGS = new PropertiesConfiguration(
					Utils.resourceURL(ConstsAndUtils.CONFIG_FILE));

			ConstsAndUtils.GUI_SEND_CHANNEL = //
			ConstsAndUtils.CONFIGS.getInt("gui.channel.send");

			ConstsAndUtils.GUI_RECEIVE_CHANNEL = //
			ConstsAndUtils.CONFIGS.getInt("gui.channel.receive");

			ConstsAndUtils.CLIENT_CHANNEL = //
			ConstsAndUtils.CONFIGS.getInt("server.channel.client");

			ConstsAndUtils.SERVER_CHANNEL = //
			ConstsAndUtils.CONFIGS.getInt("server.channel.server");

			ConstsAndUtils.LOCK_CHANNEL = //
			ConstsAndUtils.CONFIGS.getInt("server.channel.lock");

			ConstsAndUtils.NEXT_CLIENT_CHANNEL = //
			ConstsAndUtils.CONFIGS.getInt("clients.channel.first");

			ConstsAndUtils.GUI_SERVER_CHANNEL = //
			ConstsAndUtils.CONFIGS.getInt("gui.channel.server");

			ConstsAndUtils.DEFAULT_SLEEP = //
			ConstsAndUtils.CONFIGS.getInt("move.delay");
			ConstsAndUtils.MOVE_STRATEGY = //
			ConstsAndUtils.CONFIGS.getString("person.move.strategy");
		} catch (final ConfigurationException e) {
			throw new ConcurBattleException(e);
		}
	}

	public static int getLockID() {
		return ConstsAndUtils.NEXT_CLIENT_CHANNEL++;

	}

	private ConstsAndUtils() {

	}

}

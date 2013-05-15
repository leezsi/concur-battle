package ar.edu.unq.concurbattle.model.map;

import static ar.edu.unq.concurbattle.model.Side.gold;
import static ar.edu.unq.concurbattle.model.Side.silver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.unq.concurbattle.comunication.ChannelManager;
import ar.edu.unq.concurbattle.configuration.ConstsAndUtils;
import ar.edu.unq.concurbattle.model.buildings.Castle;
import ar.edu.unq.concurbattle.model.buildings.Town;
import ar.edu.unq.concurbattle.model.person.Warrior;
import ar.edu.unq.tpi.concurbattles.ConcurBattles;
import ar.edu.unq.tpi.pconc.Channel;

public class GameMap implements Runnable {

	public static void main(final String[] args) {

		new Thread(new GameMap()).start();
	}

	private Castle goldCastle;
	private Castle silverCastle;
	private final List<Town> cities = new ArrayList<Town>();

	private final Channel<String> guiChannel;

	public GameMap() {
		new Thread() {
			@Override
			public void run() {
				new ConcurBattles();
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				final Channel<String> channel = new Channel<String>(
						ConstsAndUtils.GUI_RECEIVE_CHANNEL);
				final Channel<String> serverChannel = new Channel<String>(
						ConstsAndUtils.GUI_SERVER_CHANNEL);
				while (true) {
					final String maps = channel.receive();
					serverChannel.send(maps);
				}
			}
		}.start();

		this.guiChannel = new Channel<String>(ConstsAndUtils.GUI_SEND_CHANNEL);
	}

	private void createCastles(final Map<String, Town> cities,
			final String[] castleData) {
		final String[] castles = castleData[0].split(":");
		this.goldCastle = new Castle(gold, castles[0], this);
		cities.put(castles[0], this.goldCastle);
		this.silverCastle = new Castle(silver, castles[1], this);
		cities.put(castles[1], this.silverCastle);
		this.cities.add(this.goldCastle);
		this.cities.add(this.silverCastle);
	}

	private void createCities(final Map<String, Town> cities,
			final String[] citiesToParse) {
		for (final String toCreate : citiesToParse) {
			final String[] data = toCreate.split(":");
			final String id = data[0];
			if (!cities.containsKey(id)) {
				final Town town = new Town(id, this);
				cities.put(id, town);
				this.cities.add(town);
			}
		}
	}

	public void createMap(final String mapString) {
		// "1:5=1:2,5;2:3,4;3:4;4:5;5"
		final Map<String, Town> cities = new HashMap<String, Town>();

		final String[] castleData = mapString.split("=");
		this.createCastles(cities, castleData);

		final String[] citiesToParse = castleData[1].split(";");

		this.createCities(cities, citiesToParse);

		for (final String toCreate : citiesToParse) {
			final String[] data = toCreate.split(":");
			final String id = data[0];
			if (data.length > 1) {
				final String[] paths = data[1].split(",");
				for (final String path : paths) {
					cities.get(id).pathTo(cities.get(path));
				}
			}
		}
	}

	public void die() {
		if (this.goldCastle != null) {
			this.goldCastle.killWarriors();
			this.silverCastle.killWarriors();
		}
	}

	public void gameOver(final Town castle) {
		System.out.println("Gano " + castle.getSide());
		this.die();

	}

	public void killWarrior(final Warrior warrior) {
		final String guiId = warrior.getGUIId();
		System.out.println("MURIO " + guiId);
		this.guiChannel.send(guiId);
	}

	public void moveWarrior(final Warrior person, final Town building) {
		this.guiChannel.send(person.getGUIId() + " " + building.getId());
	}

	public void newWarrior(final Warrior warrior) {
		this.guiChannel.send(warrior.getGUIId() + " " + warrior.getCastleId());
	}

	@Override
	public void run() {
		ChannelManager.getServer(ConstsAndUtils.SERVER_RECEIVE_CHANNEL,
				ConstsAndUtils.SERVER_SEND_CHANNEL,
				ConstsAndUtils.SERVER_LOCK_CHANNEL,
				ConstsAndUtils.FIRST_CLIENT_CHANNEL);

		final Channel<String> serverChannel = new Channel<String>(
				ConstsAndUtils.GUI_SERVER_CHANNEL);
		while (true) {
			final String maps = serverChannel.receive();
			this.die();
			this.createMap(maps);
			this.start();
		}
	}

	public void start() {
		System.out.println("Juego nuevo");
		this.goldCastle.startGame();
		this.silverCastle.startGame();
	}

}

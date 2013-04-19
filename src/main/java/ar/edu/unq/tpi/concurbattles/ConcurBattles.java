package ar.edu.unq.tpi.concurbattles;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;

import ar.edu.unq.concurbattle.comunication.Channel;
import buoy.event.CommandEvent;
import buoy.event.WindowClosingEvent;
import buoy.widget.BFrame;
import buoy.widget.BLabel;
import buoy.widget.BMenu;
import buoy.widget.BMenuBar;
import buoy.widget.BMenuItem;
import buoy.widget.ExplicitContainer;

/**
 * This class holds the graphical program which shows the game progress. This
 * acts as an independent process that receives messages from the game
 * simulation program through a channel. The channel must be configured using a
 * command line argument. Messages have the following format: <unit name> <unit
 * destination> The unit name can be any string without white spaces and the
 * destination must be a town name declared in the map configuration.
 */
public class ConcurBattles {

	// TODO: standardize background images to the same size

	/** This class represents a coordinate in the map for locating towns. */
	public static class Coordinate {

		public final int x;
		public final int y;

		/** Constructor for a coordinate. */
		public Coordinate(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

	}

	/**
	 * Entry point of the program. Requires a command line argument with the id
	 * of the channel to be used.
	 */
	public static void main(final String[] args) throws InterruptedException {
		if (args.length < 2) {
			System.err.println("Usage error. Concur Battles GUI requires the"
					+ " input and output channel ids as arguments.");
		}

		final Channel<String> inputChannel = new Channel<String>(
				Integer.parseInt(args[0]));
		final Channel<String> outputChannel = new Channel<String>(
				Integer.parseInt(args[1]));
		final Map<String, Coordinate> map = new HashMap<String, ConcurBattles.Coordinate>();
		final Map<String, BLabel> units = new HashMap<String, BLabel>();

		// window
		final BFrame window = new BFrame("Concur Battles");
		window.setResizable(false);
		window.addEventLink(WindowClosingEvent.class, new Object() {
			@SuppressWarnings("unused")
			void processEvent() {
				System.exit(0);
			}
		});
		window.setBounds(new Rectangle(0, 0, 550, 550));

		final ExplicitContainer container = new ExplicitContainer();
		window.setContent(container);

		final BMenuBar menu = new BMenuBar();
		final BMenu scenarioMenu = new BMenu("Scenario");

		final BMenuItem avalon = new BMenuItem("Avalon");
		avalon.addEventLink(CommandEvent.class, new Object() {
			@SuppressWarnings("unused")
			void processEvent() {
				ConcurBattles.setBackground(container, "resources/Avalon.png");
				units.clear();
				ConcurBattles
						.parseCities(map,
								" 1 235 345 \n 2 290 185 \n 3 355 115 \n 4 220 180 \n 5 165 225 \n");
				outputChannel
						.send(" 1 2 5 \n 2 1 3 4 \n 3 2 \n 4 2 5 \n 5 1 4 \n");
				window.pack();
			}
		});
		scenarioMenu.add(avalon);

		final BMenuItem diaspola = new BMenuItem("Diaspola");
		diaspola.addEventLink(CommandEvent.class, new Object() {
			@SuppressWarnings("unused")
			void processEvent() {
				ConcurBattles
						.setBackground(container, "resources/Diaspola.png");
				units.clear();
				ConcurBattles
						.parseCities(
								map,
								" 1 390 265 \n 2 355 220 \n 3 320 315 \n 4 410 100 \n "
										+ "5 260 130 \n 6 225 305 \n 7 50 360 \n 8 130 225 \n 9 100 120 \n");
				outputChannel
						.send(" 1 2 3 \n 2 1 4 5 \n 3 1 6 \n 4 2 \n 5 2 9 \n "
								+ "6 7 8 \n 7 6 8 \n 8 6 7 9 \n 9 5 8 \n");
				window.pack();
			}
		});
		scenarioMenu.add(diaspola);

		final BMenuItem sharom = new BMenuItem("Sharom");
		sharom.addEventLink(CommandEvent.class, new Object() {
			@SuppressWarnings("unused")
			void processEvent() {
				ConcurBattles.setBackground(container, "resources/Sharom.png");
				units.clear();
				ConcurBattles
						.parseCities(
								map,
								" 1 400 50 \n 2 450 140 \n 3 345 145 \n 4 195 100 \n "
										+ "5 465 265 \n 6 230 305 \n 7 130 170 \n 8 290 385 \n 9 320 465 \n "
										+ "10 20 385 \n 11 125 405 \n");
				outputChannel
						.send(" 1 2 3 4 \n 2 1 5 \n 3 1 6 \n 4 1 7 \n 5 2 8 9 \n "
								+ "6 3 11 \n 7 4 11 \n 8 5 9 11 \n 9 5 8 11 \n 10 11 \n 11 6 7 8 9 10 \n");
				window.pack();
			}
		});
		scenarioMenu.add(sharom);

		// BMenuItem zenobia = new BMenuItem("Zenobia");
		// zenobia.addEventLink(CommandEvent.class, new Object() {
		// @SuppressWarnings("unused")
		// void processEvent() {
		// setBackground(container, "resources/Zenobia.png");
		// units.clear();
		// parseCities(map,
		// "1 235 345 \n 2 290 185 \n 3 355 115 \n 4 220 180 \n 5 165 225 \n");
		// outputChannel.send("1 2 5 \n 2 1 3 4 \n 3 2 \n 4 2 5 \n 5 1 4 \n");
		// window.pack();
		// }
		// });
		// scenarioMenu.add(zenobia);

		menu.add(scenarioMenu);
		window.setMenuBar(menu);

		window.setVisible(true);

		ConcurBattles.startGame(container, inputChannel, map, units);
	}

	/**
	 * Moves a unit's label to a given coordinate.
	 * 
	 * @param container
	 *            the container where the label is located (or to be added).
	 * @param label
	 *            the unit representation on the screen.
	 * @param coordinate
	 *            position in the container where to place the unit's label.
	 */
	public static void move(final ExplicitContainer container,
			final BLabel label, final Coordinate coordinate, float factor) {
		if ((factor < 0) || (factor > 1)) {
			System.out.println("Invalid movement factor.");
			factor = 1;
		}

		int x = label.getBounds().x;
		x = (int) (x + (factor * (coordinate.x - x)));

		int y = label.getBounds().y;
		y = (int) (y + (factor * (coordinate.y - y)));

		container.add(label,
				new Rectangle(x, y, label.getIcon().getIconWidth(), label
						.getIcon().getIconHeight()));
		container.getComponent().setComponentZOrder(label.getComponent(), 0);
	}

	/**
	 * Parses a configuration string with the map information.
	 * 
	 * @param conf
	 *            configuration string with multiple lines with the following
	 *            format: <town name> <x coordinate> <y coordinate>
	 * @return a mapping of town names with board coordinates.
	 */
	private static void parseCities(final Map<String, Coordinate> map,
			final String conf) {
		map.clear();
		final String[] lines = conf.split("\n");
		for (final String line : lines) {
			final String[] words = line.trim().split(" ");
			if (words.length == 3) {
				map.put(words[0], new Coordinate(Integer.parseInt(words[1]),
						Integer.parseInt(words[2])));
			}
		}
	}

	/**
	 * Parses a configuration string with information about the paths.
	 * 
	 * @param conf
	 *            a multiple line String where each line has the following
	 *            format: <origin town name> <destination town name> ...
	 *            <destination town name> Each line states the destination towns
	 *            reachable from the origin town. Town names are string without
	 *            white spaces.
	 * @return a mapping from origin towns to a set of destination towns.
	 */
	public static Map<String, Set<String>> parsePaths(final String conf) {
		final Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		final String[] lines = conf.split("\n");
		for (final String line : lines) {
			final String[] words = line.split(" ");
			if (words.length == 0) {
				continue;
			}
			final Set<String> set = new HashSet<String>();
			result.put(words[0], set);
			for (int i = 1; i < words.length; ++i) {
				set.add(words[i]);
			}
		}
		return result;
	}

	/**
	 * Sets the scenario background.
	 * 
	 * @param container
	 *            of widgets that compose the board.
	 * @param file
	 *            path to the image for the background.
	 */
	public static void setBackground(final ExplicitContainer container,
			final String file) {
		container.removeAll();
		final BLabel background = new BLabel(new ImageIcon(file), BLabel.CENTER);
		container.add(background, new Rectangle(0, 0, background.getIcon()
				.getIconWidth(), background.getIcon().getIconHeight()));
		container.getComponent().setComponentZOrder(background.getComponent(),
				0);
	}

	/** Starts the game loop. */
	public static void startGame(final ExplicitContainer container,
			final Channel<String> channel, final Map<String, Coordinate> map,
			final Map<String, BLabel> units) {
		final ImageIcon goldIcon = new ImageIcon("resources/gold.png");
		final ImageIcon silverIcon = new ImageIcon("resources/silver.png");

		while (true) {
			final String message = channel.receive();
			final String[] arguments = message.split(" ");
			if (arguments.length == 0) {
				System.err.println("Invalid message.");
				continue;

			} else if (arguments.length == 1) {
				final String unitId = arguments[0];
				final BLabel unit = units.get(unitId);
				units.remove(unit);
				container.remove(unit);
				container.repaint();

			} else {
				final String unitId = arguments[0];
				final String destination = arguments[1];
				final String rate = arguments.length > 2 ? arguments[2] : "1.0";

				if (map.get(destination) == null) {
					System.err.println("Unknown destination \"" + destination
							+ "\" for unit " + unitId);
					continue;
				}

				BLabel unit = units.get(unitId);
				if (unit == null) {
					unit = new BLabel(unitId.startsWith("g") ? goldIcon
							: silverIcon, BLabel.CENTER);
					units.put(unitId, unit);
					System.out.println("Added unit: " + unitId);
				}

				float factor;
				try {
					factor = Float.parseFloat(rate);
				} catch (final NumberFormatException e) {
					System.err.println("Invalid movement factor: " + rate);
					factor = 1.0f;
				}

				ConcurBattles.move(container, unit, map.get(destination),
						factor);
				if (factor == 1.0f) {
					System.out.println(unitId + " arrived to " + destination);
				}
			}
		}
	}

}

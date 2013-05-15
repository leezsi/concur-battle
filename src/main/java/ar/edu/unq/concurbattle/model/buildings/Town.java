package ar.edu.unq.concurbattle.model.buildings;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.concurbattle.model.Entity;
import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.map.GameMap;
import ar.edu.unq.concurbattle.model.person.Warrior;

public class Town extends Entity {

	private final String id;
	private final List<Town> paths = new ArrayList<Town>();
	private Side side;
	private final GameMap gameMap;
	private final List<Warrior> warriors = new ArrayList<Warrior>();

	public Town(final String id, final GameMap gameMap) {
		this.id = id;
		this.gameMap = gameMap;
	}

	private void addPath(final Town town) {
		this.paths.add(town);
	}

	public void addWarrior(final Warrior warrior) {
		this.lock();
		this.warriors.add(warrior);
		this.release();
	}

	protected GameMap getGameMap() {
		return this.gameMap;
	}

	public String getId() {
		return this.id;
	}

	public List<Town> getPaths() {
		return this.paths;
	}

	public Side getSide() {
		return this.side;
	}

	private boolean isCapturedBy(final Warrior warrior) {
		if (this.getSide() == null) {
			return true;
		} else {
			return !warrior.capture(this);
		}

	}

	public void pathTo(final Town town) {
		this.addPath(town);
		town.addPath(this);

	}

	public void removeWarrior(final Warrior warrior) {
		this.lock();
		this.warriors.remove(warrior);
		this.release();
	}

	protected void setSide(final Side side) {
		this.side = side;
	}

	public void warriorArrived(final Warrior warrior) {
		this.lock();
		for (final Warrior current : this.warriors) {
			current.figthWith(warrior);
		}
		this.release();
		if (warrior.isAlive()) {
			if (this.isCapturedBy(warrior)) {
				warrior.createPartner();
			}
			warrior.setCurrentPosition(this);
		}
	}

}

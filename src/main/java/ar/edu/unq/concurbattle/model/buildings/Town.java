package ar.edu.unq.concurbattle.model.buildings;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.edu.unq.concurbattle.model.Entity;
import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.person.Warrior;

public class Town extends Entity {
	private static Logger LOG = Logger.getLogger(Town.class);
	private final String id;
	private List<Warrior> goldPopulation = new ArrayList<Warrior>();
	private List<Warrior> silverPopulation = new ArrayList<Warrior>();
	private final List<Town> paths = new ArrayList<Town>();
	private Side side;

	public Town(final String id) {
		this(id, Side.neutral);
	}

	protected Town(final String id, final Side side) {
		this.id = id;
		this.side = side;
	}

	public void addGoldWarrior(final Warrior warrior) {
		this.goldPopulation.add(warrior);
	}

	public void addSilverWarrior(final Warrior warrior) {
		this.silverPopulation.add(warrior);
	}

	public void addWarrior(final Warrior warrior) {
		this.lock();
		warrior.addIn(this);
		this.release();
	}

	private void checkFight(final Warrior warrior) {
		this.lock();
		final List<Warrior> warriors = warrior.getOppositeWarriors(this);
		for (final Warrior opponent : warriors) {
			warrior.fightWith(opponent);
		}
		this.release();
	}

	public void gameOver() {
		this.goldPopulation = new ArrayList<Warrior>();
		this.silverPopulation = new ArrayList<Warrior>();
	}

	public List<Warrior> getGoldPopulation() {
		return this.goldPopulation;
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

	public List<Warrior> getSilverPopulation() {
		return this.silverPopulation;
	}

	public boolean isCastle() {
		return false;
	}

	public boolean isOfMyOwn(final Warrior warrior) {
		this.lock();
		final boolean ret = this.getSide().equals(warrior.getSide());
		this.release();
		return ret;
	}

	public void moveDone(final Warrior warrior) {
		if (!this.getSide().equals(Side.neutral)) {
			this.checkFight(warrior);
		}
		if (warrior.isAlive()) {
			warrior.setCurrentPosition(this);
		}
	}

	public void moving(final Warrior warrior) {
		if (!this.getSide().equals(Side.neutral)) {
			this.checkFight(warrior);
		}
	}

	public void pathTo(final Town building) {
		this.paths.add(building);
		building.paths.add(this);

	}

	public void removeGoldWarrior(final Warrior warrior) {
		this.getGoldPopulation().remove(warrior);
	}

	public void removeSilverWarrior(final Warrior warrior) {
		this.getSilverPopulation().remove(warrior);
	}

	public void setSide(final Side side) {
		this.lock();
		this.side = side;
		Town.LOG.debug(this.getId() + " cambie a " + this.getSide());
		this.release();
	}

}

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
	private final List<Warrior> goldWarriors = new ArrayList<Warrior>();
	private final List<Warrior> silverWarriors = new ArrayList<Warrior>();

	public Town(final String id, final GameMap gameMap) {
		this.id = id;
		this.gameMap = gameMap;
	}

	public void addGoldWarrior(final Warrior warrior) {
		this.goldWarriors.add(warrior);
	}

	private void addPath(final Town town) {
		this.paths.add(town);
	}

	public void addSilverWarrior(final Warrior warrior) {
		this.silverWarriors.add(warrior);
	}

	protected void addWarrior(final Warrior warrior) {
		warrior.addTo(this);
		if (this.isCapturedBy(warrior)) {
			warrior.createPartner();
			this.setSide(warrior.getSide());
		}
		warrior.setCurrentPosition(this);
	}

	public Warrior getFirstGold() {
		return this.goldWarriors.get(0);
	}

	public Warrior getFirstSilver() {
		return this.silverWarriors.get(0);
	}

	protected GameMap getGameMap() {
		return this.gameMap;
	}

	public List<Warrior> getGoldWarriors() {
		return this.goldWarriors;
	}

	public String getId() {
		return this.id;
	}

	private List<Warrior> getOpponents(final Warrior warrior) {
		return warrior.getOpponentFroms(this);
	}

	public List<Town> getPaths() {
		return this.paths;
	}

	public Side getSide() {
		return this.side;
	}

	public List<Warrior> getSilverWarriors() {
		return this.silverWarriors;
	}

	public boolean hasGoldWarrior() {
		return !this.goldWarriors.isEmpty();
	}

	public boolean hasSilverWarrior() {
		return !this.silverWarriors.isEmpty();
	}

	private boolean isCapturedBy(final Warrior warrior) {
		if (this.getSide() == null) {
			return true;
		} else {
			return warrior.is(this.getSide());
		}

	}

	public void pathTo(final Town town) {
		this.addPath(town);
		town.addPath(this);

	}

	public void removeGoldWarrior(final Warrior warrior) {
		this.goldWarriors.remove(warrior);
	}

	public void removeSilverWarrior(final Warrior warrior) {
		this.silverWarriors.remove(warrior);
	}

	public void removeWarrior(final Warrior warrior) {
		warrior.removeFrom(this);
	}

	protected void setSide(final Side side) {
		this.side = side;
	}

	public void warriorArrived(final Warrior warrior) {
		final List<Warrior> opponents = this.getOpponents(warrior);
		for (final Warrior opponent : opponents) {
			if (warrior.isAlive()) {
				warrior.figthWith(opponent);
			}
		}
		if (warrior.isAlive()) {
			this.addWarrior(warrior);
		}

	}

}

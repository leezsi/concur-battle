package ar.edu.unq.concurbattle.model.person;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import ar.edu.unq.concurbattle.comunication.Utils;
import ar.edu.unq.concurbattle.configuration.ConstsAndUtils;
import ar.edu.unq.concurbattle.model.Entity;
import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.buildings.Castle;
import ar.edu.unq.concurbattle.model.buildings.Town;
import ar.edu.unq.concurbattle.model.map.GameMap;

public class Warrior extends Entity implements Runnable {

	private static Logger LOG = Logger.getLogger(Warrior.class);

	private final Castle castle;
	private final String id;
	private final Side side;
	private boolean isAlive = true;
	private final MoveStrategy moveStrategy;
	private Town currentPosition;
	private final GameMap map;
	private long level;
	private int battleWins;

	private int fibonacci2;

	private int fibonacci1;

	public Warrior(final Castle castle, final Integer id, final GameMap map) {
		this.castle = castle;
		this.side = castle.getSide();
		this.id = id.toString();
		this.moveStrategy = MoveStrategy.valueOf(ConstsAndUtils.MOVE_STRATEGY);
		this.currentPosition = castle;
		this.map = map;
		this.level = 1;
		this.fibonacci1 = 1;
		this.fibonacci2 = 1;
		this.battleWins = 0;
	}

	public void addIn(final Town town) {
		this.side.addIn(this, town);

	}

	public void battleWin() {
		this.battleWins++;
		final int tmp = this.fibonacci1 + this.fibonacci2;
		if ((this.fibonacci1 + this.fibonacci1) <= this.battleWins) {
			this.level++;
			this.fibonacci1 = this.fibonacci2;
			this.fibonacci2 = tmp;
		}
	}

	public void die() {
		this.isAlive = false;
		System.out.println("murio " + this.getGUIId() + " level " + this.level);
		this.map.killWarrior(this);
		if (this.level > 1) {
			this.castle.createWarrior();
		}
	}

	public void fightWith(final Warrior otherWarrior) {
		final Random rnd = new Random();
		final long offset = Utils.calculateOffset(this.getLevel(),
				otherWarrior.getLevel());
		if (rnd.nextFloat() > offset) {
			this.die();
			otherWarrior.battleWin();
		} else {
			otherWarrior.die();
			this.battleWin();
		}
	}

	public Castle getCastle() {
		return this.castle;
	}

	public Town getCurrentPosition() {
		return this.currentPosition;
	}

	public String getGUIId() {
		return this.getSide() + this.getId();
	}

	private String getId() {
		return this.id;
	}

	private long getLevel() {

		return this.level;

	}

	public List<Warrior> getOppositeWarriors(final Town town) {
		return this.getSide().oponents(town);
	}

	public Side getSide() {
		return this.side;
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public boolean opositeTo(final Warrior warrior) {
		return !this.side.equals(warrior.side);
	}

	public void removeFrom(final Town town) {
		this.getSide().removeFrom(this, town);
	}

	@Override
	public void run() {

		while (this.isAlive) {
			Utils.sleep(ConstsAndUtils.DEFAULT_SLEEP);
			final Town path = this.selectPath();
			path.moving(this);
			if (this.isAlive()) {
				path.moveDone(this);
			}
		}
		this.castle.removeWarrior(this);
	}

	private Town selectPath() {
		return this.moveStrategy.getPath(this.getCurrentPosition());
	}

	public void setCurrentPosition(final Town town) {
		this.currentPosition = town;
		if (!town.isOfMyOwn(this)) {
			this.getCastle().createWarrior();
		}
		town.setSide(this.getSide());
		this.map.moveWarrior(this, town);
	}

}

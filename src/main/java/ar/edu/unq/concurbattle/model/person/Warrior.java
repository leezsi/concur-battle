package ar.edu.unq.concurbattle.model.person;

import java.util.Random;

import ar.edu.unq.concurbattle.comunication.Utils;
import ar.edu.unq.concurbattle.configuration.ConstsAndUtils;
import ar.edu.unq.concurbattle.model.Entity;
import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.buildings.Castle;
import ar.edu.unq.concurbattle.model.buildings.Town;
import ar.edu.unq.concurbattle.model.map.GameMap;

public class Warrior extends Entity implements Runnable {

	private final int id;
	private final Side side;
	private final Castle castle;
	private boolean isAlive = true;
	private final MoveStrategy moveStrategy;
	private Town currentPosition;
	private long level = 1;
	private int battles;
	private int fibonacci1 = 1;
	private int fibonacci2 = 1;

	private final GameMap map;

	public Warrior(final Castle castle, final int id, final GameMap map) {
		this.castle = castle;
		this.currentPosition = castle;
		this.side = castle.getSide();
		this.id = id;
		this.moveStrategy = MoveStrategy.valueOf(ConstsAndUtils.MOVE_STRATEGY);
		this.map = map;
		map.newWarrior(this);
	}

	private void battleWin() {
		if ((this.fibonacci1 + this.fibonacci2) <= ++this.battles) {
			this.level++;
			final int tmp = this.fibonacci2;
			this.fibonacci2 = this.fibonacci1 + tmp;
			this.fibonacci1 = tmp;
		}
	}

	public boolean capture(final Town town) {
		return !this.getSide().capture(town);
	}

	public void createPartner() {
		this.getCastle().createWarrior();
	}

	private void die() {
		this.isAlive = false;
		if ((this.level > 1) && !this.map.isGameOver()) {
			this.createPartner();
		}
	}

	public void figthWith(final Warrior opponent) {
		if (!this.isPartner(opponent)) {// si no es compañero peleo
			final long offset = Utils.calculateOffset(this.level,
					opponent.level);
			final Random rnd = new Random();
			if (rnd.nextLong() <= offset) {
				this.battleWin();
				opponent.die();
			} else {
				opponent.battleWin();
				this.die();
			}
		}

	}

	public Castle getCastle() {
		return this.castle;
	}

	public String getCastleId() {
		return this.getCastle().getId();
	}

	private Town getCurrentPosition() {
		return this.currentPosition;
	}

	public String getGUIId() {
		return this.side.toString() + this.id;
	}

	public Side getSide() {
		return this.side;
	}

	private Town getTownToMove() {
		return this.moveStrategy.getPath(this.getCurrentPosition());
	}

	public boolean is(final Side aSide) {
		return this.getSide().equals(aSide);
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	private boolean isPartner(final Warrior opponent) {
		return this.getSide().equals(opponent.getSide());
	}

	@Override
	public void run() {
		while (this.isAlive) {
			this.lock();
			this.getCurrentPosition().removeWarrior(this);
			final Town town = this.getTownToMove();
			if (!this.map.isGameOver()) {
				town.warriorArrived(this);
			}
			this.release();
		}
		this.map.killWarrior(this);
		if (!this.map.isGameOver()) {
			final Town town = this.currentPosition;
			town.removeWarrior(this);
		}
	}

	public void setCurrentPosition(final Town town) {
		this.currentPosition.removeWarrior(this);
		this.currentPosition = town;
		town.addWarrior(this);
		this.map.moveWarrior(this, town);
	}

}

package ar.edu.unq.concurbattle.model.person;

import java.util.List;
import java.util.Random;

import ar.edu.unq.concurbattle.comunication.Utils;
import ar.edu.unq.concurbattle.configuration.ConstsAndUtils;
import ar.edu.unq.concurbattle.model.Entity;
import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.buildings.Castle;
import ar.edu.unq.concurbattle.model.buildings.Town;

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

	public Warrior(final Castle castle, final int id) {
		this.castle = castle;
		this.side = castle.getSide();
		this.id = id;
		this.moveStrategy = MoveStrategy.valueOf(ConstsAndUtils.MOVE_STRATEGY);
	}

	public void addTo(final Town town) {
		this.getSide().addWarriorTo(this, town);
	}

	private void battleWin() {
		if ((this.fibonacci1 + this.fibonacci2) <= ++this.battles) {
			this.level++;
			final int tmp = this.fibonacci2;
			this.fibonacci2 = this.fibonacci1 + tmp;
			this.fibonacci1 = tmp;
		}
	}

	public void createPartner() {
		this.getCastle().createWarrior();
	}

	private void die() {
		this.isAlive = false;
		if (this.level > 1) {
			this.getCastle().createWarrior();
		}
	}

	public void figthWith(final Warrior opponent) {
		final long offset = Utils.calculateOffset(this.level, opponent.level);
		final Random rnd = new Random();
		if (rnd.nextLong() <= offset) {
			this.battleWin();
			opponent.die();
		} else {
			opponent.battleWin();
			this.die();
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

	public List<Warrior> getOpponentFroms(final Town town) {
		return this.getSide().getOpponentFroms(town);
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

	public void removeFrom(final Town town) {
		this.getSide().removeFrom(this, town);
	}

	@Override
	public void run() {
		while (this.isAlive) {
			this.getCurrentPosition().removeWarrior(this);
			final Town town = this.getTownToMove();
			// town.lock();
			town.warriorArrived(this);
			// town.release();

			Utils.sleep(ConstsAndUtils.DEFAULT_SLEEP);
		}
		final Town town = this.currentPosition;
		if (town != null) {
			town.lock();
			town.removeWarrior(this);
			town.release();
		}
		this.getCastle().killMe(this);
	}

	public void setCurrentPosition(final Town town) {
		this.currentPosition = town;
	}

}

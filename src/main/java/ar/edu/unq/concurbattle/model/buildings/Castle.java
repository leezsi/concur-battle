package ar.edu.unq.concurbattle.model.buildings;

import java.util.List;

import org.apache.log4j.Logger;

import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.map.GameMap;
import ar.edu.unq.concurbattle.model.person.Warrior;

public class Castle extends Town {
	private static Logger LOG = Logger.getLogger(Castle.class);

	private final GameMap map;

	private int nextWarriorId = 0;

	public Castle(final Side side, final String id, final GameMap map) {
		super(id, side);
		this.map = map;
	}

	public void createWarrior() {
		final Warrior warrior = new Warrior(this, this.nextWarriorId++,
				this.map);
		this.addWarrior(warrior);
		this.map.newWarrior(warrior);
		this.lock();
		Castle.LOG.debug("warrior " + warrior + " creado");
		new Thread(warrior).start();
		this.release();
	}

	@Override
	public void gameOver() {
		this.killWarriors();
		super.gameOver();
		this.map.gameOver(this);
	}

	public GameMap getMap() {
		return this.map;
	}

	@Override
	public boolean isCastle() {
		return true;
	}

	public void killWarriors() {
		this.lock();
		final List<Warrior> warriors = this.getSide().getWarriors(this);
		for (final Warrior warrior : warriors) {
			warrior.die();
		}
		this.release();
	}

	public void removeWarrior(final Warrior warrior) {
		warrior.removeFrom(this);
	}

	@Override
	public void setSide(final Side side) {
		if (!side.equals(this.getSide())) {
			this.gameOver();
		}
	}

	public void startGame() {
		this.createWarrior();
	}

}

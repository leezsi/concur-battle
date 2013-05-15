package ar.edu.unq.concurbattle.model.buildings;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.map.GameMap;
import ar.edu.unq.concurbattle.model.person.Warrior;

public class Castle extends Town {

	private int warriorId = 0;
	private List<Warrior> myWarriors = new ArrayList<Warrior>();

	public Castle(final Side side, final String id, final GameMap gameMap) {
		super(id, gameMap);
		this.setSide(side);
	}

	@Override
	public void addWarrior(final Warrior warrior) {
		if (warrior.capture(this)) {
			this.getGameMap().gameOver(warrior.getCastle());
		} else {
			super.addWarrior(warrior);
		}
	}

	public void createWarrior() {
		final Warrior warrior = new Warrior(this, this.getWarriorId(),
				this.getGameMap());
		warrior.setCurrentPosition(this);
		this.addWarrior(warrior);
		this.myWarriors.add(warrior);
		new Thread(warrior).start();
	}

	private int getWarriorId() {
		return this.warriorId++;
	}

	public void kill(final Warrior warrior) {
		this.myWarriors.remove(warrior);

	}

	public void killWarriors() {
		for (final Warrior warrior : this.myWarriors) {
			warrior.gameOver();
		}
		this.myWarriors = new ArrayList<Warrior>();
	}

	public void startGame() {
		this.createWarrior();
	}

}

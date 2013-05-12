package ar.edu.unq.concurbattle.model.buildings;

import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.map.GameMap;
import ar.edu.unq.concurbattle.model.person.Warrior;

public class Castle extends Town {

	private int warriorId = 0;

	public Castle(final Side side, final String id, final GameMap gameMap) {
		super(id, gameMap);
		this.setSide(side);
	}

	public void createWarrior() {
		final Warrior warrior = new Warrior(this, this.getWarriorId());
		warrior.setCurrentPosition(this);
		this.addWarrior(warrior);
		this.getGameMap().newWarrior(warrior);
		new Thread(warrior).start();
	}

	private int getWarriorId() {
		return this.warriorId++;
	}

	public void killMe(final Warrior warrior) {
		this.getGameMap().killWarrior(warrior);

	}

	public void killWarriors() {
		// TODO Auto-generated method stub

	}

	public void startGame() {
		this.createWarrior();
	}

}

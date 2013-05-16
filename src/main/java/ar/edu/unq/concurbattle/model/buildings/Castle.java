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

	@Override
	public void addWarrior(final Warrior warrior) {
		if (warrior.capture(this)) {
			this.getGameMap().gameOver(warrior.getCastle());
		} else {
			super.addWarrior(warrior);
		}
	}

	public void createWarrior() {
		if (!this.getGameMap().isGameOver()) {
			final Warrior warrior = new Warrior(this, this.getWarriorId(),
					this.getGameMap());
			this.addWarrior(warrior);
			new Thread(warrior).start();
		}
	}

	private int getWarriorId() {
		return this.warriorId++;
	}

	public void startGame() {
		this.createWarrior();
	}

}

package ar.edu.unq.concurbattle.model.person;

import java.util.Random;

import ar.edu.unq.concurbattle.comunication.Utils;
import ar.edu.unq.concurbattle.model.buildings.AbstractBuilding;

public class Person extends AbstractBuilding {

	public static enum Side {
		GOO, BAD
	}

	private static final long serialVersionUID = -6081297445914196767L;
	private int battleWins;
	private int level;

	public Person(final Side side) {
		this.battleWins = 1;
	}

	public void battleWin() {
		this.level = Utils.fibonacci(this.battleWins++);
	}

	@Override
	protected void doLoop() {
		// TODO Auto-generated method stub

	}

	public void figth(final Person enemy) {
		final long probability = Utils.calculateOffset(this.level, enemy.level);
		if (new Random().nextGaussian() < probability) {
			this.battleWin();
			enemy.die();
		} else {
			this.die();
		}
	}

}

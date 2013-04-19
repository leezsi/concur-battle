package ar.edu.unq.concurbattle.model.person;

import java.util.List;

import ar.edu.unq.concurbattle.model.Entity;
import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.buildings.Path;

public class Person extends Entity {

	private static final long serialVersionUID = -6081297445914196767L;
	private PersonMoveStrategy moveStrategy;
	private final Side side;

	public Person(final Side side) {
		this.side = side;
	}

	@Override
	protected void doLoop() {
		// TODO Auto-generated method stub

	}

	public Side getSide() {
		return this.side;
	}

	public void move(final List<Path> paths) {
		final Path path = this.moveStrategy.select(paths);
		path.personMove(this);
	}

}

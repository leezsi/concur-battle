package ar.edu.unq.concurbattle.model.buildings;

import ar.edu.unq.concurbattle.model.Entity;
import ar.edu.unq.concurbattle.model.person.Person;

public class Path extends Entity {

	private static final long serialVersionUID = -621226687427193804L;

	private AbstractBuilding corner1;

	private AbstractBuilding corner2;

	public Path() {
		super();
	}

	public Path corner1(final AbstractBuilding building) {
		this.corner1 = building;
		return this;
	}

	public Path corner2(final AbstractBuilding building) {
		this.corner2 = building;
		return this;
	}

	@Override
	protected void doLoop() {
		// TODO Auto-generated method stub

	}

	public AbstractBuilding getCorner1() {
		return this.corner1;
	}

	public AbstractBuilding getCorner2() {
		return this.corner2;
	}

	public void personMove(final Person person) {
		// TODO Auto-generated method stub

	}

}

package ar.edu.unq.concurbattle.model.person;

import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.buildings.AbstractBuilding;

public class Person extends AbstractBuilding {

	private static final long serialVersionUID = -6081297445914196767L;

	public Person(final Side side) {
		super(side);
	}

	@Override
	protected void doLoop() {
		// TODO Auto-generated method stub

	}

}

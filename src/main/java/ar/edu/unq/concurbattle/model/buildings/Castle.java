package ar.edu.unq.concurbattle.model.buildings;

import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.person.Person;

public class Castle extends AbstractBuilding {
	private static final long serialVersionUID = -2889521192177910267L;

	public Castle(final Side side) {
		super(side);
		this.createPerson();
	}

	public void createPerson() {
		this.addPerson(new Person(this.getSide()));
	}

	@Override
	protected void doLoop() {
		// TODO Auto-generated method stub

	}

}

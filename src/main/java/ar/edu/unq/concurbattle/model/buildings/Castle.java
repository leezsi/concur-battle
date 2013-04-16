package ar.edu.unq.concurbattle.model.buildings;

import ar.edu.unq.concurbattle.model.person.Person;
import ar.edu.unq.concurbattle.model.person.Person.Side;

public class Castle extends AbstractBuilding {
	private static final long serialVersionUID = -2889521192177910267L;

	public Castle() {

	}

	public void createPerson(final Side side) {
		this.addPerson(new Person(side));
	}

	@Override
	protected void doLoop() {
		// TODO Auto-generated method stub

	}

}

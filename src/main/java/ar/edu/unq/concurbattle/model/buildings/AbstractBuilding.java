package ar.edu.unq.concurbattle.model.buildings;

import java.util.ArrayList;

import ar.edu.unq.concurbattle.model.Entity;
import ar.edu.unq.concurbattle.model.person.Person;

public abstract class AbstractBuilding extends Entity {

	private static final long serialVersionUID = 7237814974119191471L;
	private final ArrayList<Person> population;

	public AbstractBuilding() {
		this.population = new ArrayList<Person>();
	}

	protected void addPerson(final Person person) {
		this.population.add(person);
	}
}

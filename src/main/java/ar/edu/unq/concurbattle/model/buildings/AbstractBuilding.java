package ar.edu.unq.concurbattle.model.buildings;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.concurbattle.model.Entity;
import ar.edu.unq.concurbattle.model.Side;
import ar.edu.unq.concurbattle.model.person.Person;

public abstract class AbstractBuilding extends Entity {

	private static final long serialVersionUID = 7237814974119191471L;
	private final List<Person> population = new ArrayList<Person>();
	private Side side;
	private final List<Path> paths = new ArrayList<>();

	public AbstractBuilding(final Side side) {
		this.side = side;
	}

	protected void addPath(final Path path) {
		this.paths.add(path);

	}

	protected void addPerson(final Person person) {
		this.population.add(person);
	}

	protected void changeSide() {
		this.side = this.side.rival();
	}

	public void createPathTo(final Town town) {
		final Path path = new Path().corner1(this).corner2(town);
		this.addPath(path);
		town.addPath(path);
	}

	protected Side getSide() {
		return this.side;
	}

	protected void removePerson(final Person person) {
		this.population.remove(person);
	}

}

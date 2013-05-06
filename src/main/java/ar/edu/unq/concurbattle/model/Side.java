package ar.edu.unq.concurbattle.model;

import java.util.List;

import ar.edu.unq.concurbattle.model.buildings.Town;
import ar.edu.unq.concurbattle.model.person.Warrior;

public enum Side {

	gold {
		@Override
		public void addIn(final Warrior warrior, final Town town) {
			town.addGoldWarrior(warrior);

		}

		@Override
		public List<Warrior> getWarriors(final Town town) {
			return town.getGoldPopulation();
		}

		@Override
		public Side opposite() {
			return silver;

		}

		@Override
		public void removeFrom(final Warrior warrior, final Town town) {
			town.removeGoldWarrior(warrior);

		}
	},
	silver {
		@Override
		public void addIn(final Warrior warrior, final Town town) {
			town.addSilverWarrior(warrior);
		}

		@Override
		public List<Warrior> getWarriors(final Town town) {
			return town.getSilverPopulation();
		}

		@Override
		public Side opposite() {
			return gold;

		}

		@Override
		public void removeFrom(final Warrior warrior, final Town town) {
			town.removeSilverWarrior(warrior);
		}
	};

	public abstract void addIn(Warrior warrior, Town town);

	public abstract List<Warrior> getWarriors(Town town);

	public List<Warrior> oponents(final Town town) {
		return this.opposite().getWarriors(town);
	}

	public abstract Side opposite();

	public abstract void removeFrom(Warrior warrior, Town town);
}

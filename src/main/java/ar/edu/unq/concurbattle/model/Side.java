package ar.edu.unq.concurbattle.model;

import java.util.List;

import ar.edu.unq.concurbattle.model.buildings.Town;
import ar.edu.unq.concurbattle.model.person.Warrior;

public enum Side {

	gold {

		@Override
		public void addWarriorTo(final Warrior warrior, final Town town) {
			town.addGoldWarrior(warrior);
		}

		@Override
		public List<Warrior> getOpponentFroms(final Town town) {
			return town.getSilverWarriors();
		}

		@Override
		public void removeFrom(final Warrior warrior, final Town town) {
			town.removeGoldWarrior(warrior);
		}

	},
	silver {

		@Override
		public void addWarriorTo(final Warrior warrior, final Town town) {
			town.addSilverWarrior(warrior);
		}

		@Override
		public List<Warrior> getOpponentFroms(final Town town) {
			return town.getGoldWarriors();
		}

		@Override
		public void removeFrom(final Warrior warrior, final Town town) {
			town.removeSilverWarrior(warrior);

		}

	};

	public abstract void addWarriorTo(Warrior warrior, Town town);

	public abstract List<Warrior> getOpponentFroms(Town town);

	public abstract void removeFrom(Warrior warrior, Town town);

}

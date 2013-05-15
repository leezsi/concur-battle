package ar.edu.unq.concurbattle.model;

import ar.edu.unq.concurbattle.model.buildings.Town;

public enum Side {

	gold, silver;

	public boolean capture(final Town town) {
		return town.getSide().equals(this);
	}
}

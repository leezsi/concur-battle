package ar.edu.unq.concurbattle.model.person;

import java.util.List;
import java.util.Random;

import ar.edu.unq.concurbattle.model.buildings.Town;

public enum MoveStrategy {
	random {
		@Override
		Town getPath(final Town building) {
			final List<Town> paths = building.getPaths();
			return paths.get(new Random().nextInt(paths.size()));
		}
	},
	first {
		@Override
		Town getPath(final Town building) {
			return building.getPaths().get(0);
		}
	};

	abstract Town getPath(Town building);

}

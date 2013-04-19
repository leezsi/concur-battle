package ar.edu.unq.concurbattle.model.person;

import java.util.List;
import java.util.Random;

import ar.edu.unq.concurbattle.model.buildings.Path;

public enum PersonMoveStrategy {
	RANDOM {
		@Override
		public Path select(final List<Path> paths) {
			final Random rnd = new Random();
			return paths.get(rnd.nextInt(paths.size() + 1));
		}
	};

	public abstract Path select(List<Path> paths);

}

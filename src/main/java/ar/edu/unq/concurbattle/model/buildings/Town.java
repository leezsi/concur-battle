package ar.edu.unq.concurbattle.model.buildings;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.concurbattle.model.Side;

public class Town extends AbstractBuilding {

	private static final long serialVersionUID = -6645511197344962484L;
	private final List<Path> paths;

	public Town(final Side side) {
		super(side);
		this.paths = new ArrayList<Path>();
	}

	public void addPath(final Path path) {
		this.paths.add(path);
	}

	@Override
	protected void doLoop() {
		// TODO Auto-generated method stub

	}

}

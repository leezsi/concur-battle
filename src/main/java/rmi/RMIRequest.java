package rmi;

import java.io.Serializable;

public class RMIRequest implements Serializable {

	private static final long serialVersionUID = 7259334396897144747L;
	private final Serializable data;
	private final RMICommand command;

	public RMIRequest(final RMICommand command, final Serializable data) {
		this.command = command;
		this.data = data;
	}

	public RMICommand getCommand() {
		return this.command;
	}

	public Serializable getData() {
		return this.data;
	}
}

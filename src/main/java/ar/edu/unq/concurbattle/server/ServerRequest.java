package ar.edu.unq.concurbattle.server;

import java.io.Serializable;

public class ServerRequest implements Serializable {
	private static final long serialVersionUID = -8002960498246569975L;
	private final ServerProtocol command;
	private final Serializable data;

	public ServerRequest(final ServerProtocol command, final Serializable data) {
		this.command = command;
		this.data = data;
	}

	public ServerProtocol getCommand() {
		return this.command;
	}

	public Serializable getData() {
		return this.data;
	}

}

package ar.edu.unq.concurbattle.server;

import java.io.Serializable;

public class ServerProtocol implements Serializable {

	public static enum ServerRequestCommand {
		SEND, GENERATE_CHANNEL, NOOP
	}

	private static final long serialVersionUID = -4710916503927495643L;
	private final ServerRequestCommand command;
	private final Serializable object;

	public ServerProtocol(final ServerRequestCommand command) {
		this(command, null);
	}

	public ServerProtocol(final ServerRequestCommand command,
			final Serializable obj) {
		this.command = command;
		this.object = obj;
	}

	public ServerRequestCommand getCommand() {
		return this.command;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T) this.object;
	}

}

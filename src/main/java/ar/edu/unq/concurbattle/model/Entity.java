package ar.edu.unq.concurbattle.model;

import java.io.Serializable;

import ar.edu.unq.concurbattle.comunication.Channel;
import ar.edu.unq.concurbattle.server.GameServer;
import ar.edu.unq.concurbattle.server.ServerProtocol;
import ar.edu.unq.concurbattle.server.ServerProtocol.ServerRequestCommand;

public abstract class Entity implements Serializable, Runnable {
	private static final long serialVersionUID = 8519330767655233323L;
	private final Channel<ServerProtocol> serverChannel;
	private final Channel<Boolean> serverLock;
	private Channel<ServerProtocol> channel;
	private boolean isAlive = true;

	public Entity() {
		this.serverChannel = new Channel<>(GameServer.CHANNEL_SERVER);
		this.serverLock = new Channel<>(GameServer.LOCK_CHANNEL_SERVER);
	}

	private Channel<ServerProtocol> createChannelFromServer() {
		final ServerRequestCommand command = ServerRequestCommand.GENERATE_CHANNEL;
		this.sendServerCommand(new ServerProtocol(command));
		final ServerProtocol answer = this.receiveServerMessage();
		final Integer id = answer.<Integer> getData();
		return new Channel<ServerProtocol>(id);
	}

	protected void die() {
		this.isAlive = false;
	}

	protected abstract void doLoop();

	protected void lockServer() {
		this.serverLock.receive();
	}

	protected ServerProtocol receiveServerMessage() {
		return this.serverChannel.receive();
	}

	protected void releaseServer() {
		this.serverLock.send(true);
	}

	@Override
	public void run() {
		this.lockServer();
		this.channel = this.createChannelFromServer();
		this.releaseServer();
		while (!this.isAlive) {
			this.doLoop();
		}
	}

	protected void sendServerCommand(final ServerProtocol command) {
		this.serverChannel.send(command);
	}

}

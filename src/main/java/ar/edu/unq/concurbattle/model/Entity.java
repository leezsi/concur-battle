package ar.edu.unq.concurbattle.model;

import static ar.edu.unq.concurbattle.server.ServerProtocol.GET_CHANNEL_PACKAGE;

import java.io.Serializable;

import ar.edu.unq.concurbattle.comunication.DualChannel;
import ar.edu.unq.concurbattle.server.ChannelPackage;
import ar.edu.unq.concurbattle.server.GameServer;
import ar.edu.unq.concurbattle.server.ServerProtocol;
import ar.edu.unq.concurbattle.server.ServerRequest;

public abstract class Entity implements Serializable, Runnable, Channeable {
	private static final long serialVersionUID = 8519330767655233323L;
	private final DualChannel<ServerRequest> serverChannel;
	private ChannelPackage channelPackage;

	public Entity() {
		this.serverChannel = GameServer.createServerCommunicationChannel();
		this.createChannelPackage();
	}

	private void createChannelPackage() {
		this.serverLock();
		this.sendToServer(GET_CHANNEL_PACKAGE, null);
		this.channelPackage = (ChannelPackage) this.receiveFromServer()
				.getData();
		this.serverRelease();
	}

	protected abstract void doLoop();

	@Override
	public ChannelPackage getChannelPackage() {
		return this.channelPackage;
	}

	protected void initialize() {

	};

	protected ServerRequest receiveFromServer() {
		return this.serverChannel.getFromServer();
	}

	@Override
	public void run() {
		this.initialize();
		while (true) {
			this.doLoop();
		}
	}

	protected void sendToServer(final ServerProtocol command,
			final Serializable data) {
		this.serverChannel.sendToServer(new ServerRequest(command, data));
	}

	protected void serverLock() {
		this.serverChannel.lock();
	}

	protected void serverRelease() {
		this.serverChannel.release();
	}
}

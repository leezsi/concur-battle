package ar.edu.unq.concurbattle.comunication;

import java.io.Serializable;

public class DualChannel<T extends Serializable> {

	private Channel<T> clientChannel;
	private Channel<T> serverChannel;
	private boolean sync;

	public DualChannel() {
		this(false);
	}

	public DualChannel(final boolean sync) {
		this.sync = sync;
	}

	public T getFromClient() {
		return this.serverChannel.receive();
	}

	public T getFromServer() {
		return this.clientChannel.receive();
	}

	public void sendToClient(final T data) {
		this.clientChannel.send(data);
	}

	public void sendToServer(final T data) {
		this.serverChannel.send(data);
	}

	public DualChannel<T> setClientChannel(final int channelId) {
		this.clientChannel = new Channel<T>(channelId, this.sync);
		return this;
	}

	public DualChannel<T> setServerChannel(final int channelId) {
		this.serverChannel = new Channel<T>(channelId, this.sync);
		return this;
	}
}

package ar.edu.unq.concurbattle.comunication;

import java.io.Serializable;

public class DualChannel<T extends Serializable> {

	private static final int DEFAULT_LOCK_CHANNEL = 10000;
	private Channel<T> clientChannel;
	private Channel<T> serverChannel;
	private boolean sync;
	private Channel<Boolean> lockChannel;

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

	private Channel<Boolean> getLockChannel() {
		return this.lockChannel == null ? new Channel<Boolean>(
				DualChannel.DEFAULT_LOCK_CHANNEL) : this.lockChannel;
	}

	public void lock() {
		this.getLockChannel().receive();
	}

	public void release() {
		this.getLockChannel().send(true);
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

	public DualChannel<T> setLockChannel(final int channelId) {
		this.lockChannel = new Channel<Boolean>(channelId, this.sync);
		return this;
	}

	public DualChannel<T> setServerChannel(final int channelId) {
		this.serverChannel = new Channel<T>(channelId, this.sync);
		return this;
	}
}

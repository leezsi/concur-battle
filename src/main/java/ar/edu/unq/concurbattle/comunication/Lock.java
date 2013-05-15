package ar.edu.unq.concurbattle.comunication;

import ar.edu.unq.tpi.pconc.Channel;

public class Lock {

	private final Channel<Boolean> lock;

	public Lock() {
		this.lock = ChannelManager.getChannel();
		this.release();

	}

	public void lock() {
		this.lock.receive();
	}

	public void release() {
		this.lock.send(true);
	}
}

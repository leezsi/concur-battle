package ar.edu.unq.concurbattle.comunication;

import ar.edu.unq.concurbattle.configuration.ConstsAndUtils;
import ar.edu.unq.tpi.pconc.Channel;

public class Lock {

	private final Channel<Boolean> lock;

	public Lock() {
		this.lock = ChannelManager.getChannel(
				ConstsAndUtils.SERVER_SEND_CHANNEL,
				ConstsAndUtils.SERVER_RECEIVE_CHANNEL,
				ConstsAndUtils.SERVER_LOCK_CHANNEL);
		this.release();

	}

	public void lock() {
		this.lock.receive();
	}

	public void release() {
		this.lock.send(true);
	}
}

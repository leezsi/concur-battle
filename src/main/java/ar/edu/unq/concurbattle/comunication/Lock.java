package ar.edu.unq.concurbattle.comunication;

import ar.edu.unq.tpi.pconc.Channel;

public class Lock {

	private final Channel<Boolean> lock;
	private Object target;

	public Lock(final int channel) {
		this.lock = new Channel<Boolean>(channel);
		this.release();

	}

	public void lock() {
		this.lock.receive();
	}

	public void lock(final Object target) {
		if (!target.equals(this.target)) {
			this.lock();
			this.target = target;
		}

	}

	public void release() {
		this.target = null;
		this.lock.send(true);
	}
}

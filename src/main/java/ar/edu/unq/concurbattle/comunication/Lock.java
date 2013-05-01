package ar.edu.unq.concurbattle.comunication;

import ar.edu.unq.concurbattle.exception.ExceptionInterceptor;
import ar.edu.unq.tpi.pconc.Channel;

public class Lock {

	private final Channel<Boolean> lock;

	public Lock(final int channel) {
		this.lock = new Channel<Boolean>(channel);
		this.release();

	}

	public void lock() {
		try {
			this.lock.receive();
		} catch (final Exception e) {
			throw new ExceptionInterceptor(e);
		}
	}

	public void release() {
		this.lock.send(true);
	}
}

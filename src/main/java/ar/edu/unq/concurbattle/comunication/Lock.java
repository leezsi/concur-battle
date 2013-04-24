package ar.edu.unq.concurbattle.comunication;

public class Lock {

	private final Channel<Boolean> lock;
	private final Channel<Boolean> release;

	public Lock(final int index) {
		this.lock = new Channel<Boolean>(index);
		this.release = new Channel<Boolean>(index);
		this.release();
	}

	public void lock() {
		this.lock.receive();
	}

	public void release() {
		this.release.send(true);
	}
}

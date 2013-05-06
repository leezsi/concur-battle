package ar.edu.unq.concurbattle.model;

import ar.edu.unq.concurbattle.comunication.Lock;
import ar.edu.unq.concurbattle.configuration.ConstsAndUtils;

public abstract class Entity {
	private final Lock lock;

	public Entity() {
		this.lock = new Lock(ConstsAndUtils.getLockID());
	}

	public void lock() {
		this.lock.lock();
	}

	public void lock(final Object target) {
		this.lock.lock(target);
	}

	public void release() {
		this.lock.release();
	}
}

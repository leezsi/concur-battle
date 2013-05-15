package ar.edu.unq.concurbattle.model;

import ar.edu.unq.concurbattle.comunication.Lock;

public abstract class Entity {
	private final Lock lock;

	public Entity() {
		this.lock = new Lock();
		this.lock.release();
	}

	public void lock() {
		this.lock.lock();
	}

	public void release() {
		this.lock.release();
	}

}

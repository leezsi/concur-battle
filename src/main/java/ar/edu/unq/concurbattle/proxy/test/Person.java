package ar.edu.unq.concurbattle.proxy.test;

import rmi.RMIInvoker;

public class Person implements IPerson {

	private Integer value2;
	private String value1;

	public Person() {
		new RMIInvoker(this, 1, 2).start();
	}

	@Override
	public String getValue1() {
		return this.value1;
	}

	@Override
	public Integer getValue2() {
		return this.value2;
	}

	@Override
	public void setValue(final String value) {
		this.value1 = value;
	}

	@Override
	public void setValue(final String value1, final Integer value2) {
		this.value1 = value1;
		this.value2 = value2;

	}

}

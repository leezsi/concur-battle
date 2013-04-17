package ar.edu.unq.concurbattle.proxy.test;

import rmi.RMIInvoker;
import rmi.RMIRequest;
import ar.edu.unq.concurbattle.comunication.DualChannel;

public class Person implements IPerson {

	private Integer value2;
	private String value1;

	public Person() {
		final DualChannel<RMIRequest> channel = new DualChannel<RMIRequest>();
		channel.setClientChannel(1).setServerChannel(2).setLockChannel(3);
		new RMIInvoker(channel).on(this);
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

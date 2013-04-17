package ar.edu.unq.concurbattle.proxy.test;

import java.io.InputStream;

import org.apache.log4j.PropertyConfigurator;

import rmi.DynamicProxy;
import rmi.RMIRequest;
import ar.edu.unq.concurbattle.comunication.DualChannel;

public class TestRMI {

	public static void main(final String[] args) {
		final InputStream stream = ClassLoader
				.getSystemResourceAsStream("log4j.properties");
		PropertyConfigurator.configure(stream);
		TestRMI.test();
	}

	public static void test() {
		final Thread t1 = new Thread() {
			@Override
			public void run() {
				final DualChannel<RMIRequest> channel = new DualChannel<RMIRequest>();
				channel.setClientChannel(1).setServerChannel(2)
						.setLockChannel(3);
				final IPerson proxy = DynamicProxy.getProxy(IPerson.class,
						channel);
				int index = 0;
				while (true) {
					System.out.println("mandando " + index++);
					proxy.setValue("hola" + index, index);
					System.out.println(proxy.getValue1() + " "
							+ proxy.getValue2());
				}

			}
		};

		for (int i = 0; i < 50; i++) {
			new Thread() {
				@Override
				public void run() {
					new Person();
				};
			}.start();
		}
		t1.start();

	}

}

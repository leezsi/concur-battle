package ar.edu.unq.concurbattle.proxy.test;

import java.io.InputStream;

import org.apache.log4j.PropertyConfigurator;

import rmi.DynamicProxy;

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
				final IPerson proxy = DynamicProxy
						.getProxy(IPerson.class, 1, 2);
				int index = 0;
				while (true) {
					proxy.setValue("hola" + index, index++);
					System.out.println(proxy.getValue1() + " "
							+ proxy.getValue2());
				}

			}
		};

		final Thread t2 = new Thread() {
			@Override
			public void run() {
				new Person();
			};
		};

		t1.start();
		t2.start();
	}

}

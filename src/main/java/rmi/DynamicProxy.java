package rmi;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import org.apache.log4j.Logger;

import ar.edu.unq.concurbattle.comunication.DualChannel;

public class DynamicProxy implements InvocationHandler {

	private static Logger LOG = Logger.getLogger(DynamicProxy.class);

	/**
	 * {@code Iperson personProxy= DynamicProxy.getProxy(IPerson.class,1,2);}
	 * 
	 * @author Leandro Silvestri
	 * @param targetInterface
	 *            interface of proxy target
	 * @param channel1
	 *            one way channel id
	 * @param channel2
	 *            other way channel id
	 * @return RMI Proxy object
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxy(final Class<T> targetInterface,
			final int channel1, final int channel2) {
		final ClassLoader loader = ClassLoader.getSystemClassLoader();
		final Class<?>[] interfaces = new Class<?>[] { targetInterface };
		final InvocationHandler handler = new DynamicProxy(channel1, channel2);
		return (T) Proxy.newProxyInstance(loader, interfaces, handler);
	}

	private final DualChannel<RMIRequest> stream;

	private DynamicProxy(final int channel1, final int channel2) {
		this.stream = new DualChannel<RMIRequest>()//
				.setClientChannel(channel1)//
				.setServerChannel(channel2);

	}

	private void ackOkOrBoom() {
		final RMIRequest retrive = this.getFromServer();
		switch (retrive.getCommand()) {
		case ACK:
			DynamicProxy.LOG.debug("reciving ACK");
			break;
		case ERROR:
			final Throwable exception = (Throwable) retrive.getData();
			DynamicProxy.LOG.debug("ERROR " + exception.getMessage());
			throw new RMIProxyRuntimeException(exception);
		default:
			System.out.println("ERROR  [" + retrive.getCommand() + "]");
			break;
		}
	}

	private RMIRequest getFromServer() {
		return this.stream.getFromServer();
	}

	@Override
	public Object invoke(final Object proxy, final Method method,
			final Object[] args) throws Throwable {
		//
		DynamicProxy.LOG.debug("Initializating comunications");
		this.sendToServer(RMICommand.START_TRANSMITION);
		this.ackOkOrBoom();
		//
		final String methodName = method.getName();
		DynamicProxy.LOG.debug("sending method name [" + methodName + "]");
		this.sendToServer(RMICommand.METHOD_NAME, methodName);
		this.ackOkOrBoom();
		//
		DynamicProxy.LOG.debug("Sending arguments" + Arrays.toString(args));
		this.sendToServer(RMICommand.SEND_PARAMETERS, args);
		this.ackOkOrBoom();
		//
		DynamicProxy.LOG.debug("Sending execute");
		this.sendToServer(RMICommand.EXECUTE);
		this.ackOkOrBoom();
		//
		DynamicProxy.LOG.debug("reciving result");
		final RMIRequest result = this.getFromServer();
		//
		DynamicProxy.LOG.debug("Ending transmition");
		DynamicProxy.LOG.debug("RESULTADO "
				+ ((Serializable[]) result.getData())[0]);
		return ((Serializable[]) result.getData())[0];
	}

	private void sendToServer(final RMICommand command, final Object... data) {
		this.stream.sendToServer(new RMIRequest(command, data));

	}

}

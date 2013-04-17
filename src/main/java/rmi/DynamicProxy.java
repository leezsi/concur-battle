package rmi;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
			final DualChannel<RMIRequest> channel) {
		final ClassLoader loader = ClassLoader.getSystemClassLoader();
		final Class<?>[] interfaces = new Class<?>[] { targetInterface };
		final InvocationHandler handler = new DynamicProxy(channel);
		return (T) Proxy.newProxyInstance(loader, interfaces, handler);
	}

	private final DualChannel<RMIRequest> channel;

	private DynamicProxy(final DualChannel<RMIRequest> channel) {
		this.channel = channel;
	}

	@Override
	public Object invoke(final Object proxy, final Method method,
			final Object[] args) throws Throwable {
		//
		this.lockServer();
		final RMIMethod data = new RMIMethod(method, args);
		DynamicProxy.LOG.debug("Sending command [START_TRANSMITION]");
		this.sendToServer(RMICommand.START_TRANSMITION, data);
		final RMIRequest request = this.channel.getFromServer();
		Serializable value = null;
		switch (request.getCommand()) {
		case RESULT:
			value = request.getData();
			DynamicProxy.LOG.debug("Reciving command [RESULT] with data ["
					+ value + "]");
			break;
		case ERROR:
			final Throwable error = (Throwable) request.getData();
			DynamicProxy.LOG.error("Reciving [ERROR]:\n\t "
					+ error.getMessage());
			throw error;
		default:
			DynamicProxy.LOG.error("Unexpected commend");
			throw new RMIProxyRuntimeException("Unexpecting command ["
					+ request.getCommand() + "]");
		}
		this.release();
		return value;
	}

	private void lockServer() {
		this.channel.lock();
	}

	private void release() {
		this.channel.release();
	}

	private void sendToServer(final RMICommand command, final Serializable data) {
		this.channel.sendToServer(new RMIRequest(command, data));
	}

}

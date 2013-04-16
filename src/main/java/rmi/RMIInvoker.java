package rmi;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.log4j.Logger;

import ar.edu.unq.concurbattle.comunication.DualChannel;
import ar.edu.unq.concurbattle.comunication.Utils;

public class RMIInvoker extends Thread {
	private static Logger LOG = Logger.getLogger(RMIInvoker.class);

	private final DualChannel<RMIRequest> stream;
	private Object[] methodParameters;
	private String methodName;
	private final Object target;

	public RMIInvoker(final Object target, final int channel1,
			final int channel2) {
		this.target = target;
		this.stream = new DualChannel<RMIRequest>()//
				.setClientChannel(channel1)//
				.setServerChannel(channel2);
	}

	private void checkCommand(final RMICommand command1,
			final RMICommand command2) {
		if (!command1.equals(command2)) {
			throw new RMIProxyRuntimeException("Command expected [" + command1
					+ "] recived [" + command2 + "]");
		}

	}

	private RMIRequest getRequest() {
		return this.stream.getFromClient();
	}

	private void resetMethod() {
		this.methodName = "";
		this.methodParameters = new Object[0];
	}

	@Override
	public void run() {
		while (true) {
			//
			RMIRequest request = this.getRequest();
			RMICommand command = request.getCommand();
			this.checkCommand(RMICommand.START_TRANSMITION, command);
			RMIInvoker.LOG.debug("Starting transmition");
			this.resetMethod();
			this.sendACK();
			//
			request = this.getRequest();
			command = request.getCommand();
			this.checkCommand(RMICommand.METHOD_NAME, command);
			Object[] data = (Object[]) request.getData();
			RMIInvoker.LOG.debug("reciving METHOD_NAME "
					+ Arrays.toString(data));
			this.methodName = (String) data[0];
			this.sendACK();
			//
			request = this.getRequest();
			command = request.getCommand();
			data = (Object[]) request.getData();
			this.checkCommand(RMICommand.SEND_PARAMETERS, command);
			RMIInvoker.LOG
					.debug("Reciving parameters " + Arrays.toString(data));
			this.methodParameters = data;
			this.sendACK();
			//
			request = this.getRequest();
			command = request.getCommand();
			this.checkCommand(RMICommand.EXECUTE, command);
			//
			synchronized (RMIInvoker.LOG) {
				try {
					final Class<? extends Object> clazz = this.target
							.getClass();
					final Class<?>[] parameterClasses = Utils
							.toClasses(this.methodParameters);
					final Method method = clazz.getDeclaredMethod(
							this.methodName, parameterClasses);
					final boolean isAccessible = method.isAccessible();
					method.setAccessible(true);
					final Serializable result = (Serializable) method.invoke(
							this.target, this.methodParameters);
					method.setAccessible(isAccessible);
					this.sendACK();
					this.sendToClient(RMICommand.SEND_RESULT, result);
					//
				} catch (final Exception e) {
					e.printStackTrace();
					throw new RMIProxyRuntimeException(e);
				}
			}

		}
	}

	private void sendACK() {
		RMIInvoker.LOG.debug("Sending ACK");
		this.sendToClient(RMICommand.ACK);
	}

	private void sendToClient(final RMICommand command,
			final Serializable... data) {
		this.stream.sendToClient(new RMIRequest(command, data));
	}
}

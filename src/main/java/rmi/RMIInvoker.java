package rmi;

import java.io.Serializable;

import org.apache.log4j.Logger;

import ar.edu.unq.concurbattle.comunication.DualChannel;

public class RMIInvoker implements Runnable {
	private static Logger LOG = Logger.getLogger(RMIInvoker.class);
	private final DualChannel<RMIRequest> channel;
	private Object target;

	public RMIInvoker(final DualChannel<RMIRequest> channel) {
		this.channel = channel;
	}

	private RMIRequest getFromClient() {
		return this.channel.getFromClient();
	}

	public void on(final Object target) {
		this.target = target;
		new Thread(this).start();
	}

	@Override
	public void run() {
		this.channel.release();
		while (true) {
			RMIInvoker.LOG.debug("Reciving method");
			final RMIRequest request = this.getFromClient();
			final RMICommand command = request.getCommand();
			if (!command.equals(RMICommand.START_TRANSMITION)) {
				final String message = "bad command:\n expected [START_TRANSMITION]\n recived ["
						+ command + "]";
				RMIInvoker.LOG.error(message);
				final RMIProxyRuntimeException exception = new RMIProxyRuntimeException(
						message);
				this.sendToClient(RMICommand.ERROR, exception);
			} else {
				final RMIMethod method = (RMIMethod) request.getData();
				final Serializable result = method.invoke(this.target);
				this.sendToClient(RMICommand.RESULT, result);
			}
		}
	}

	private void sendToClient(final RMICommand command, final Serializable data) {
		this.channel.sendToClient(new RMIRequest(command, data));
	}

}

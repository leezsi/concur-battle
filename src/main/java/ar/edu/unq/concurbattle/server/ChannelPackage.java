package ar.edu.unq.concurbattle.server;

import java.io.Serializable;

import rmi.DynamicProxy;
import rmi.RMIInvoker;
import rmi.RMIRequest;
import ar.edu.unq.concurbattle.comunication.DualChannel;

public class ChannelPackage implements Serializable {
	private static final long serialVersionUID = -4714170545161654337L;
	private final int lockChannel;
	private final int clientChannel;
	private final int serverChannel;
	private DualChannel<RMIRequest> channel;

	public ChannelPackage(final int serverChannel, final int clientChannel,
			final int lockChannel) {
		this.serverChannel = serverChannel;
		this.clientChannel = clientChannel;
		this.lockChannel = lockChannel;
	}

	private DualChannel<RMIRequest> getChannel() {
		if (this.channel == null) {
			this.channel = new DualChannel<RMIRequest>();
			this.channel.setServerChannel(this.serverChannel)//
					.setClientChannel(this.clientChannel)//
					.setLockChannel(this.lockChannel);
		}
		return this.channel;
	}

	public int getClientChannel() {
		return this.clientChannel;
	}

	public int getLockChannel() {
		return this.lockChannel;
	}

	public <T> T getProxy(final Class<T> toProxy) {
		return DynamicProxy.getProxy(toProxy, this.getChannel());
	}

	public int getServerChannel() {
		return this.serverChannel;
	}

	public ChannelPackage prepareForSend() {
		this.channel = null;
		return this;
	}

	public void rmiInvokerOn(final Object target) {
		new RMIInvoker(this.getChannel()).on(target);
	}
}

package ar.edu.unq.concurbattle.comunication;

import java.io.Serializable;

import ar.edu.unq.concurbattle.exception.ConcurbattleRuntimeException;
import ar.edu.unq.tpi.pconc.Channel;

public class ChannelManager {
	public static class ChannelClient<T extends Serializable> {

		private final Channel<ChannelManagerProtocol> sendChannel;
		private final Channel<Boolean> lock;
		private final Channel<ChannelManagerProtocol> receiveChannel;

		public ChannelClient(final int sendChannelId,
				final int receiveChannelId, final int lockChannelId) {
			this.sendChannel = new Channel<ChannelManagerProtocol>(
					sendChannelId);
			this.receiveChannel = new Channel<ChannelManagerProtocol>(
					receiveChannelId);
			this.lock = new Channel<Boolean>(lockChannelId);
		}

		public Channel<T> getChannel() {
			this.serverLock();
			this.send(ChannelManagerProtocol.START);
			this.send(ChannelManagerProtocol.GET_CHANNEL);
			final Channel<T> channel = new Channel<T>(this.receive().data());
			this.send(ChannelManagerProtocol.RELEASE);
			return channel;
		}

		private ChannelManagerProtocol receive() {
			return this.receiveChannel.receive();
		}

		private void send(final ChannelManagerProtocol command) {
			this.sendChannel.send(command);
		}

		private void serverLock() {
			this.lock.receive();
		}

	}

	private static enum ChannelManagerProtocol {
		GET_CHANNEL, SEND_CHANNEL_ID, RELEASE, START;

		private int data;

		public int data() {
			return this.data;
		}

		public ChannelManagerProtocol data(final int data) {
			this.data = data;
			return this;
		}

	}

	public static class ChannelServer extends Thread {

		private final Channel<ChannelManagerProtocol> receiveChannel;
		private final Channel<Boolean> lock;

		private final Channel<ChannelManagerProtocol> sendChannel;

		private int firstChannel;

		public ChannelServer(final int receiveChannelId,
				final int sendChannelId, final int lockChannelId,
				final int firstChannel) {
			this.receiveChannel = new Channel<ChannelManagerProtocol>(
					receiveChannelId);
			this.sendChannel = new Channel<ChannelManagerProtocol>(
					sendChannelId);
			this.lock = new Channel<Boolean>(lockChannelId);
			this.firstChannel = firstChannel;
			this.start();
		}

		private void cleanQueue() {
			while (this.receive().equals(ChannelManagerProtocol.START)) {
			}
		}

		private ChannelManagerProtocol receive() {
			return this.receiveChannel.receive();
		}

		private void release() {
			this.lock.send(true);
		}

		@Override
		public void run() {
			this.release();
			while (true) {
				final ChannelManagerProtocol command = this.receive();
				switch (command) {
				case START:
					break;
				case GET_CHANNEL:
					this.send(ChannelManagerProtocol.SEND_CHANNEL_ID
							.data(this.firstChannel++));
					break;
				case RELEASE:
					this.release();
					break;
				default:
					this.release();
					this.cleanQueue();
					throw new ConcurbattleRuntimeException("Unknow command "
							+ command);
				}
			}
		}

		private void send(final ChannelManagerProtocol data) {
			this.sendChannel.send(data);

		}
	}

	public static <R extends Serializable> Channel<R> getChannel(
			final int sendChannelId, final int receiveChannelId,
			final int lockChannelId) {
		return new ChannelClient<R>(receiveChannelId, sendChannelId,
				lockChannelId).getChannel();
	}

	public static ChannelServer getServer(final int receiveChannelId,
			final int sendChannelId, final int lockChannelId,
			final int firstChannel) {
		return new ChannelServer(receiveChannelId, sendChannelId,
				lockChannelId, firstChannel);
	}
}

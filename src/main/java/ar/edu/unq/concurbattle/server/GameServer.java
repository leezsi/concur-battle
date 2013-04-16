package ar.edu.unq.concurbattle.server;

import ar.edu.unq.concurbattle.comunication.Channel;
import ar.edu.unq.concurbattle.server.ServerProtocol.ServerRequestCommand;

public class GameServer implements Runnable {

	public static final int CHANNEL_SERVER = 0;
	public static final int LOCK_CHANNEL_SERVER = 1;
	private final Channel<ServerProtocol> protocoChannel;
	private final Channel<Boolean> lockServerChannel;
	private int nextChannelId;

	public GameServer() {
		this.protocoChannel = new Channel<>(GameServer.CHANNEL_SERVER);
		this.lockServerChannel = new Channel<>(GameServer.LOCK_CHANNEL_SERVER);
		this.nextChannelId = 100;
	}

	@Override
	public void run() {
		this.lockServerChannel.send(true);
		while (true) {
			final ServerProtocol command = this.protocoChannel.receive();
			switch (command.getCommand()) {
			case SEND:

				break;
			case GENERATE_CHANNEL:
				final ServerProtocol request = new ServerProtocol(
						ServerRequestCommand.NOOP, this.nextChannelId++);
				this.sendPrivateMessageToClient(request);
				break;
			default:
				break;
			}
		}
	}

	private void sendPrivateMessageToClient(final ServerProtocol request) {
		this.protocoChannel.send(request);
	}

}

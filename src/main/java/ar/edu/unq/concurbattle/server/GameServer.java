package ar.edu.unq.concurbattle.server;

import static ar.edu.unq.concurbattle.model.Side.BAD;
import static ar.edu.unq.concurbattle.model.Side.GOOD;

import java.io.Serializable;

import ar.edu.unq.concurbattle.comunication.DualChannel;
import ar.edu.unq.concurbattle.exception.ConcurBattleException;
import ar.edu.unq.concurbattle.model.buildings.Castle;
import ar.edu.unq.concurbattle.model.buildings.Town;

public class GameServer implements Runnable {

	private static final int CLIENT_CHANNEL = 1;
	private static final int SERVER_CHANNEL = 2;
	private static final int LOCK_CHANNEL = 3;
	private static final int FIRST_CLIENT_CHANNEL = 100;

	public static DualChannel<ServerRequest> createServerCommunicationChannel() {
		return new DualChannel<ServerRequest>()
				.setServerChannel(GameServer.SERVER_CHANNEL)//
				.setClientChannel(GameServer.CLIENT_CHANNEL)//
				.setLockChannel(GameServer.LOCK_CHANNEL);
	}

	public static void main(final String[] args) {
		new Thread(new GameServer()).start();
	}

	private final DualChannel<ServerRequest> channel;

	private int newxtChannelIndex;

	public GameServer() {
		this.channel = GameServer.createServerCommunicationChannel();
		this.newxtChannelIndex = GameServer.FIRST_CLIENT_CHANNEL;
	}

	private Serializable getNextChannelPackage() {
		return new ChannelPackage(this.newxtChannelIndex++,
				this.newxtChannelIndex++, this.newxtChannelIndex++);
	}

	private void populateGame() {
		final Castle goodCastle = new Castle(GOOD);
		final Castle badCastle = new Castle(BAD);
		final Town quilmes = new Town("Quilmes");
		final Town berazategui = new Town("Berazategui");
		final Town caba = new Town("C.A.B.A");
		final Town lomas = new Town("Lomas de Zamora");
		final Town avellaneda = new Town("Avellaneda");
		final Town laPlata = new Town("La Plata");
		final Town hudson = new Town("Hudson");
		goodCastle//
				.createPathTo(laPlata)//
				.createPathTo(hudson)//
				.createPathTo(berazategui)//
				.createPathTo(badCastle);
		goodCastle.createPathTo(quilmes)//
				.createPathTo(avellaneda)//
				.createPathTo(lomas)//
				.createPathTo(badCastle);
		goodCastle.createPathTo(caba)//
				.createPathTo(badCastle);
		berazategui//
				.createPathTo(quilmes)//
				.createPathTo(lomas);
		laPlata.createPathTo(caba);

	}

	private ServerRequest receive() {
		return this.channel.getFromClient();
	}

	private void release() {
		this.channel.release();
	}

	@Override
	public void run() {
		this.release();
		new Thread() {
			@Override
			public void run() {
				GameServer.this.populateGame();
			};
		}.start();
		while (true) {
			final ServerRequest request = this.receive();
			switch (request.getCommand()) {
			case GET_CHANNEL_PACKAGE:
				this.send(ServerProtocol.SEND_CHANNEL_PACKAGE,
						this.getNextChannelPackage());
				break;

			default:
				throw new ConcurBattleException("Unknow command ["
						+ request.getCommand() + "]");
			}
		}
	}

	private void send(final ServerProtocol command, final Serializable data) {
		this.channel.sendToClient(new ServerRequest(command, data));
	}

}

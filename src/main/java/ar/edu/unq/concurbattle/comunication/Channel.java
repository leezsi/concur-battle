package ar.edu.unq.concurbattle.comunication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * This class acts like a interprocess communication channel for Object passing.
 */
public class Channel<T extends Serializable> implements Serializable {

	/** Thread class that manages an active channel connection. */
	private static class ChannelClient extends Thread {

		private final ChannelServer server;
		/** Parent server. */
		private final Socket socket;

		/** Socket to the server. */

		/**
		 * Constructor for a ChannelClient.
		 * 
		 * @param server
		 *            a ChannelServer that built this client.
		 * 
		 * @param socket
		 *            a Socket to communicate with the server.
		 */
		public ChannelClient(final ChannelServer server, final Socket socket) {
			this.server = server;
			this.socket = socket;
		}

		/**
		 * Thread run method. This client runs forever waiting for incoming
		 * objects from the channel end it manages forwarding them to the server
		 * and back.
		 */
		@Override
		public void run() {
			try {
				final ObjectInputStream ois = new ObjectInputStream(
						this.socket.getInputStream());
				final ObjectOutputStream oos = new ObjectOutputStream(
						this.socket.getOutputStream());
				while (true) {
					try {
						final ChannelRequest request = (ChannelRequest) ois
								.readObject();
						switch (request.option) {
						case GET:
							synchronized (this.server) {
								oos.writeObject(this.server.pop());
								oos.flush();
								oos.reset();
								if (this.server.synchronous) {
									this.server
											.popPending()
											.writeObject(
													new ChannelRequest(
															ChannelRequest.RequestOption.ACK,
															null));
								}
							}
							break;
						case PUT:
							synchronized (this.server) {
								this.server.push(request.serializable);
								if (this.server.synchronous) {
									this.server.pushPending(oos);
								}
							}
							break;
						case ACK:
							break;
						default:
							break;
						}
					} catch (final ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			} catch (final IOException e) {
				synchronized (this.server.clients) {
					this.server.clients.remove(this);
				}
			}
		}

	}

	/** Internal request structure. */
	private static class ChannelRequest implements Serializable {
		public enum RequestOption {
			GET, PUT, ACK
		}

		private static final long serialVersionUID = 7105851698096727208L;
		public RequestOption option;
		public Serializable serializable;

		public ChannelRequest(final RequestOption option,
				final Serializable serializable) {
			this.option = option;
			this.serializable = serializable;
		}
	}

	/** Internal server thread. */
	private static class ChannelServer extends Thread {

		private final ServerSocket serverSocket;
		/** Socket to accept new connections. */
		private final List<Serializable> buffer;
		/** Buffer of transmitted elements. */
		private List<ObjectOutputStream> pendingAck;
		/** Pending acknowledgments if synchronous. */
		private final List<ChannelClient> clients;
		/** One thread to manage each connected process. */
		private final boolean synchronous;

		/** Indicates if the channel is synchronous. */

		/**
		 * Constructor for a ChannelServer.
		 * 
		 * @param id
		 *            an int with the identifier of the channel.
		 * 
		 * @param synchronous
		 *            a boolean that indicates if the channel is synchronous.
		 * 
		 * @throws IOException
		 *             if an I/O error occurred.
		 */
		public ChannelServer(final int id, final boolean synchronous)
				throws IOException {
			this.serverSocket = new ServerSocket(id);
			this.buffer = new LinkedList<Serializable>();
			this.clients = new LinkedList<ChannelClient>();
			this.synchronous = synchronous;
		}

		/**
		 * Removes the first pending message of the server buffer. Blocks if the
		 * buffer is empty until a message is sent.
		 * 
		 * @return the first message of the buffer.
		 */
		public synchronized Serializable pop() {
			while (this.buffer.isEmpty()) {
				try {
					this.wait();
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			return this.buffer.remove(0);
		}

		/**
		 * Removes the first stream pending for acknowledgment (only for
		 * synchronous channels).
		 * 
		 * @return the first stream pending for acknowledgment.
		 */
		public ObjectOutputStream popPending() {
			return this.pendingAck.remove(0);
		}

		/**
		 * Enqueues a new message object to the server buffer.
		 * 
		 * @param serializable
		 *            a Serializable object that will be added to the server
		 *            buffer.
		 */
		public synchronized void push(final Serializable serializable) {
			this.buffer.add(serializable);
			this.notify();
		}

		/**
		 * Adds a new stream pending for acknowledgment (only for synchronous
		 * channels).
		 * 
		 * @param oos
		 *            an ObjectOutputStream pending for acknowledgment.
		 */
		public void pushPending(final ObjectOutputStream oos) {
			this.pendingAck.add(oos);
		}

		/**
		 * Thread run method. This server runs forever accepting new
		 * connections.
		 */
		@Override
		public void run() {
			try {
				while (true) {
					final Socket socket = this.serverSocket.accept();
					final ChannelClient channelClient = new ChannelClient(this,
							socket);
					synchronized (this.clients) {
						this.clients.add(channelClient);
					}
					channelClient.start();
				}
			} catch (final IOException e) {
			}
		}

	}

	private static final long serialVersionUID = 1709070204269234089L;

	/** Main function that creates a process to manage a given channel. */
	public static void main(final String[] args) {
		if (args.length == 0) {
			System.err.println("Error the channel id is required (0 - 10000).");
		}
		try {
			new Channel<Serializable>(Integer.parseInt(args[0]));
		} catch (final NumberFormatException e) {
			System.err.println("Error: invalid channel id " + args[0]);
		}
	}

	private ChannelServer server;

	/** Server thread if this was build first. */
	private Socket socket;
	/** Socket to communicate through. */
	private ObjectInputStream inputStream;
	/** The socket object input stream. */
	private ObjectOutputStream outputStream;
	/** The socket object output stream. */
	private boolean synchronous;

	/** Indicates if this channel is synchronous. */
	/**
	 * Constructor for an asynchronous channel with the given <b>id</b>.
	 * 
	 * @param id
	 *            an int with the identification number of this channel (must be
	 *            between 0 and 10000).
	 * 
	 * @throws RuntimeException
	 *             if the <b>id</b> is out of range or if an I/O error occurred.
	 */
	public Channel(final int id) {
		this(id, false);
	}

	/**
	 * Constructor for a channel with the given <b>id</b> that can work in a
	 * <b>synchronous</b> mode.
	 * 
	 * @param id
	 *            an int with the identification number of this channel (must be
	 *            between 0 and 10000).
	 * 
	 * @param synchronous
	 *            a boolean that indicates if this channel should be build
	 *            synchronous. All channel variables using the same id must
	 *            construct the channel in the same way.
	 * 
	 * @throws RuntimeException
	 *             if the <b>id</b> is out of range or if an I/O error occurred.
	 */
	public Channel(final int id, final boolean synchronous) {
		if ((id < 0) || (id > 10000)) {
			throw new RuntimeException("Channel id " + id
					+ " out of bounds (0 - 10000)");
		}
		try {
			this.server = new ChannelServer(id + 10000, synchronous);
			this.server.start();
		} catch (final IOException e) {
		}
		try {
			this.socket = new Socket("localhost", id + 10000);
			this.outputStream = new ObjectOutputStream(
					this.socket.getOutputStream());
			this.inputStream = new ObjectInputStream(
					this.socket.getInputStream());
		} catch (final IOException e) {
			throw new RuntimeException("Error creating channel " + id);
		}
	}

	/**
	 * Reads an object from the channel. If no element is available blocks until
	 * one object is written by another process.
	 * 
	 * @return the received object.
	 * 
	 * @throws RuntimeException
	 *             if an I/O occurred during transmission.
	 */
	@SuppressWarnings("unchecked")
	public T receive() {
		final ChannelRequest request = new ChannelRequest(
				ChannelRequest.RequestOption.GET, null);
		T result = null;
		try {
			this.outputStream.writeObject(request);
			this.outputStream.flush();
			this.outputStream.reset();
			result = (T) this.inputStream.readObject();
		} catch (final Exception e) {
			throw new RuntimeException("Error while receiving object");
		}
		return result;
	}

	/**
	 * Sends the given <b>object</b> through the channel.
	 * 
	 * @param object
	 *            the element to be passed through the channel.
	 * 
	 * @throws RuntimeException
	 *             if an I/O error occurred during transmission.
	 */
	public void send(final T object) {
		final ChannelRequest request = new ChannelRequest(
				ChannelRequest.RequestOption.PUT, object);
		try {
			this.outputStream.writeObject(request);
			this.outputStream.flush();
			this.outputStream.reset();
			if (this.synchronous) {
				this.inputStream.readObject();
			}
		} catch (final IOException e) {
			throw new RuntimeException("Error while sending message", e);
		} catch (final ClassNotFoundException e) {
		}
	}

}

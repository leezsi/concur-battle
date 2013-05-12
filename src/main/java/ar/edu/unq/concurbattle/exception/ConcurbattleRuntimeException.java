package ar.edu.unq.concurbattle.exception;

public class ConcurbattleRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -3961103325479536859L;

	public ConcurbattleRuntimeException(final String msg) {
		super(msg);
		this.fullPrintStackTrace(this);
	}

	public ConcurbattleRuntimeException(final Throwable cause) {
		super(cause);
		this.fullPrintStackTrace(cause);
	}

	private void fullPrintStackTrace(final Throwable cause) {
		if (cause != null) {
			cause.printStackTrace();
			this.fullPrintStackTrace(cause.getCause());
		}
	}

}

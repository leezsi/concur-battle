package ar.edu.unq.concurbattle.exception;

public class ConcurbattleRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -3961103325479536859L;

	public ConcurbattleRuntimeException() {
	}

	public ConcurbattleRuntimeException(final String message) {
		super(message);
	}

	public ConcurbattleRuntimeException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	public ConcurbattleRuntimeException(final String message,
			final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConcurbattleRuntimeException(final Throwable cause) {
		super(cause);
	}

}

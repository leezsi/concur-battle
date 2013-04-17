package ar.edu.unq.concurbattle.exception;

public class ConcurBattleException extends RuntimeException {

	private static final long serialVersionUID = 3645862160602401836L;

	public ConcurBattleException() {
	}

	public ConcurBattleException(final String message) {
		super(message);
	}

	public ConcurBattleException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ConcurBattleException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConcurBattleException(final Throwable cause) {
		super(cause);
	}

}

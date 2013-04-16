package rmi;

public class RMIProxyRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -3341188261166254452L;

	public RMIProxyRuntimeException() {

	}

	public RMIProxyRuntimeException(final String message) {
		super(message);
	}

	public RMIProxyRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public RMIProxyRuntimeException(final String message,
			final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RMIProxyRuntimeException(final Throwable cause) {
		super(cause);
	}

}

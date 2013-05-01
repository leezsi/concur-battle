package ar.edu.unq.concurbattle.exception;

public class ExceptionInterceptor extends RuntimeException {

	private static final long serialVersionUID = 8829580879025125283L;

	public ExceptionInterceptor(final Exception e) {
		super(e);
		this.printStackTrace(e);
	}

	private void printStackTrace(final Throwable e) {
		final Throwable cause = e.getCause();
		if (cause != null) {
			e.printStackTrace();
			this.printStackTrace(cause);
		}
	}

}

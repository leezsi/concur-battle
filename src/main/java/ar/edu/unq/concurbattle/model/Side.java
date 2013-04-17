package ar.edu.unq.concurbattle.model;

public enum Side {
	GOOD {
		@Override
		public Side rival() {
			return BAD;
		}
	},
	BAD {
		@Override
		public Side rival() {
			return GOOD;
		}
	};

	public abstract Side rival();
}

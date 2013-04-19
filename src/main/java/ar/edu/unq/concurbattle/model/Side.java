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
	},
	NEUTRAL {
		@Override
		public Side rival() {
			return NEUTRAL;
		}
	};

	public abstract Side rival();

}

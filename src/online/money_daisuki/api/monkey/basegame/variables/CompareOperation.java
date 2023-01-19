package online.money_daisuki.api.monkey.basegame.variables;

public enum CompareOperation {
	EQUALS("==") {
		@Override
		public boolean compareBool(final boolean a, final boolean b) {
			return(a == b);
		}
		@Override
		public boolean compareInt(final int a, final int b) {
			return(a == b);
		}
	},
	ANTIQUALS("!=") {
		@Override
		public boolean compareBool(final boolean a, final boolean b) {
			return(a != b);
		}
		@Override
		public boolean compareInt(final int a, final int b) {
			return(a != b);
		}
	},
	BIGGER_THAN(">") {
		@Override
		public boolean compareBool(final boolean a, final boolean b) {
			throw new IllegalArgumentException("Cannot compare boolean magnitude");
		}
		@Override
		public boolean compareInt(final int a, final int b) {
			return(a > b);
		}
	},
	BIGGER_EQUALS_THAN(">=") {
		@Override
		public boolean compareBool(final boolean a, final boolean b) {
			throw new IllegalArgumentException("Cannot compare boolean magnitude");
		}
		@Override
		public boolean compareInt(final int a, final int b) {
			return(a >= b);
		}
	},
	SMALLER_THAN("<") {
		@Override
		public boolean compareBool(final boolean a, final boolean b) {
			throw new IllegalArgumentException("Cannot compare boolean magnitude");
		}
		@Override
		public boolean compareInt(final int a, final int b) {
			return(a < b);
		}
	},
	SMALLER_EQUALS_THAN("<=") {
		@Override
		public boolean compareBool(final boolean a, final boolean b) {
			throw new IllegalArgumentException("Cannot compare boolean magnitude");
		}
		@Override
		public boolean compareInt(final int a, final int b) {
			return(a <= b);
		}
	};
	
	private String op;
	
	private CompareOperation(final String op) {
		this.op = op;
	}
	
	public abstract boolean compareBool(final boolean a, final boolean b);
	public abstract boolean compareInt(final int a, final int b);
	
	public static CompareOperation getOp(final String s) {
		for(final CompareOperation op:values()) {
			if(op.op.equals(s)) {
				return(op);
			}
		}
		return(null);
	}
}

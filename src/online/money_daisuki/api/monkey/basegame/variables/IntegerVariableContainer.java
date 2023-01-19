package online.money_daisuki.api.monkey.basegame.variables;

public final class IntegerVariableContainer implements VariableContainer {
	private final int i;
	
	public IntegerVariableContainer(final int i) {
		this.i = i;
	}
	@Override
	public boolean test(final CompareOperation op, final String value) {
		return(op.compareInt(i, Integer.valueOf(value)));
	}
	public int asInt() {
		return(i);
	}
}

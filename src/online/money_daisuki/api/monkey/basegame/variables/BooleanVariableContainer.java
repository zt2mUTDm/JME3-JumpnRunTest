package online.money_daisuki.api.monkey.basegame.variables;

public final class BooleanVariableContainer implements VariableContainer {
	private final boolean b;
	
	public BooleanVariableContainer(final boolean b) {
		this.b = b;
	}
	@Override
	public boolean test(final CompareOperation op, final String value) {
		return(op.compareBool(b, Boolean.valueOf(value)));
	}
}

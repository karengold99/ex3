package ast;

import types.Type;
import semantic.SemanticException;

public class AstCFieldVar extends AstCField {
	public AstDecVar varDec;

	public AstCFieldVar(AstDecVar varDec) {
		super(varDec.lineNumber);
		serialNumber = AstNodeSerialNumber.getFresh();
		this.varDec = varDec;
	}

	public void printMe() {
		System.out.println("CLASS FIELD VAR");
		if (varDec != null)
			varDec.printMe();

		AstGraphviz.getInstance().logNode(serialNumber, "CFIELD\nVAR");
		if (varDec != null)
			AstGraphviz.getInstance().logEdge(serialNumber, varDec.serialNumber);
	}

	@Override
	public Type semantMe() throws SemanticException {
		return varDec.semantMe();
	}
}


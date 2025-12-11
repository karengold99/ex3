package ast;

import types.Type;
import semantic.SemanticException;

public class AstCFieldFunc extends AstCField {
	public AstDecFunc funcDec;

	public AstCFieldFunc(AstDecFunc funcDec) {
		super(funcDec.lineNumber);
		serialNumber = AstNodeSerialNumber.getFresh();
		this.funcDec = funcDec;
	}

	public void printMe() {
		System.out.println("CLASS FIELD FUNC");
		if (funcDec != null)
			funcDec.printMe();

		AstGraphviz.getInstance().logNode(serialNumber, "CFIELD\nFUNC");
		if (funcDec != null)
			AstGraphviz.getInstance().logEdge(serialNumber, funcDec.serialNumber);
	}

	@Override
	public Type semantMe() throws SemanticException {
		return funcDec.semantMe();
	}
}


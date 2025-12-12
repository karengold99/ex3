package ast;

import types.*;
import semantic.SemanticException;

public class AstExpNil extends AstExp
{
	public AstExpNil()
	{
		serialNumber = AstNodeSerialNumber.getFresh();
		// Debug disabled: 0
	}

	public void printMe()
	{
		System.out.print("AST NODE NIL\n");
		AstGraphviz.getInstance().logNode(serialNumber, "NIL");
	}

	@Override
	public Type semantMe() throws SemanticException
	{
		return TypeNil.getInstance();
	}
}


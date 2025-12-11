package ast;

import types.Type;
import semantic.SemanticException;

public abstract class AstNode {
	public int serialNumber;
	public int lineNumber;

	public AstNode() {
		this.lineNumber = 0;
	}

	public AstNode(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void printMe() {
		System.out.print("AST NODE UNKNOWN\n");
	}

	public abstract Type semantMe() throws SemanticException;
}

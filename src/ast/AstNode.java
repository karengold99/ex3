package ast;

import types.Type;
import semantic.SemanticException;

public abstract class AstNode {
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating */
	/* a graphviz dot format of the AST ... */
	/*******************************************/
	public int serialNumber;
	public int lineNumber;

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
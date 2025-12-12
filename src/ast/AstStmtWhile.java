package ast;

import types.*;
import semantic.SemanticException;
import symboltable.SymbolTable;

public class AstStmtWhile extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtWhile(AstExp cond, AstStmtList body)
	{
		serialNumber = AstNodeSerialNumber.getFresh();
		this.cond = cond;
		this.body = body;
	}

	public void printMe()
	{
		System.out.print("AST NODE STMT WHILE\n");
		if (cond != null) cond.printMe();
		if (body != null) body.printMe();

		AstGraphviz.getInstance().logNode(serialNumber, "WHILE");
		if (cond != null) AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber, body.serialNumber);
	}

	@Override
	public Type semantMe() throws SemanticException
	{
		// PDF 2.5: condition must be int
		Type condType = cond.semantMe();
		if (!condType.isInt())
			throw new SemanticException(cond.lineNumber, "while condition must be int");

		// Begin scope for while body
		SymbolTable.getInstance().beginScope();

		if (body != null)
			body.semantMe();

		SymbolTable.getInstance().endScope();

		return null;
	}
}

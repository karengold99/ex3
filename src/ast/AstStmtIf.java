package ast;

import types.*;
import semantic.SemanticException;
import symboltable.*;

public class AstStmtIf extends AstStmt
{
	public AstExp cond;
	public AstStmtList thenBody;
	public AstStmtList elseBody;  // null if no else

	public AstStmtIf(AstExp cond, AstStmtList thenBody, AstStmtList elseBody)
	{
		serialNumber = AstNodeSerialNumber.getFresh();
		// Debug disabled
		this.cond = cond;
		this.thenBody = thenBody;
		this.elseBody = elseBody;
	}

	public void printMe()
	{
		System.out.print("AST NODE STMT IF\n");
		if (cond != null) cond.printMe();
		if (thenBody != null) thenBody.printMe();
		if (elseBody != null) elseBody.printMe();

		AstGraphviz.getInstance().logNode(serialNumber, "IF");
		if (cond != null) AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);
		if (thenBody != null) AstGraphviz.getInstance().logEdge(serialNumber, thenBody.serialNumber);
		if (elseBody != null) AstGraphviz.getInstance().logEdge(serialNumber, elseBody.serialNumber);
	}

	@Override
	public Type semantMe() throws SemanticException
	{
		// PDF 2.5: condition must be int
		Type condType = cond.semantMe();
		if (!condType.isInt())
			throw new SemanticException(cond.lineNumber, "if condition must be int");
		
		// Then branch scope
		SymbolTable.getInstance().beginScope();
		if (thenBody != null)
			thenBody.semantMe();
		SymbolTable.getInstance().endScope();

		// Else branch scope (if exists)
		if (elseBody != null) {
			SymbolTable.getInstance().beginScope();
			elseBody.semantMe();
			SymbolTable.getInstance().endScope();
		}

		return null;		
	}	
}

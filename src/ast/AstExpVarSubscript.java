package ast;

import types.*;
import semantic.SemanticException;

public class AstExpVarSubscript extends AstExpVar
{
	public AstExpVar var;
	public AstExp subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpVarSubscript(AstExpVar var, AstExp subscript)
	{
		serialNumber = AstNodeSerialNumber.getFresh();
		// Debug disabled: 0
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void printMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.printMe();
		if (subscript != null) subscript.printMe();

		AstGraphviz.getInstance().logNode(serialNumber, "SUBSCRIPT\nVAR");
		if (var != null) AstGraphviz.getInstance().logEdge(serialNumber, var.serialNumber);
		if (subscript != null) AstGraphviz.getInstance().logEdge(serialNumber, subscript.serialNumber);
	}

	@Override
	public Type semantMe() throws SemanticException
	{
		// PDF 2.3: v[e] - v must be array type, e must be int
		Type varType = var.semantMe();
		Type subscriptType = subscript.semantMe();

		// Check var is array type
		if (!varType.isArray())
			throw new SemanticException(lineNumber, "subscript access on non-array type");

		// Check subscript is int
		if (!subscriptType.isInt())
			throw new SemanticException(lineNumber, "array subscript must be int");

		// PDF 2.3: If subscript is constant expression, must be >= 0
		Integer constVal = subscript.getConstantValue();
		if (constVal != null && constVal < 0)
			throw new SemanticException(subscript.lineNumber, "array subscript cannot be negative constant");

		// Return element type of array
		TypeArray arrType = (TypeArray) varType;
		return arrType.elementType;
	}
}

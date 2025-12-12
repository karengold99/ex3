package ast;

import types.*;
import semantic.SemanticException;

public class AstExpBinop extends AstExp
{
	// op: 0=PLUS, 1=MINUS, 2=TIMES, 3=DIVIDE, 4=LT, 5=GT, 6=EQ
	public int op;
	public AstExp left;
	public AstExp right;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpBinop(AstExp left, AstExp right, int op)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// Debug disabled: 0

		/*******************************/
		/* COPY INPUT DATA MENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.op = op;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{
		String sop="";
		
		/*********************************/
		/* CONVERT OP to a printable sop */
		/*********************************/
		if (op == 0) {sop = "+";}
		if (op == 1) {sop = "-";}
		if (op == 2) {sop = "*";}
		if (op == 3) {sop = "/";}
		if (op == 4) {sop = "<";}
		if (op == 5) {sop = ">";}
		if (op == 6) {sop = "=";}

		/**********************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*********************************/
		System.out.print("AST NODE BINOP EXP\n");
		System.out.format("BINOP EXP(%s)\n",sop);

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.printMe();
		if (right != null) right.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
                serialNumber,
			String.format("BINOP(%s)",sop));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AstGraphviz.getInstance().logEdge(serialNumber,left.serialNumber);
		if (right != null) AstGraphviz.getInstance().logEdge(serialNumber,right.serialNumber);
	}

	@Override
	public Type semantMe() throws SemanticException
	{
		Type t1 = left.semantMe();
		Type t2 = right.semantMe();
		
		// op: 0=PLUS, 1=MINUS, 2=TIMES, 3=DIVIDE, 4=LT, 5=GT, 6=EQ
		
		if (op == 0) // PLUS
		{
			// PDF 2.6: + works on int+int or string+string
			if (t1.isInt() && t2.isInt())
				return TypeInt.getInstance();
			if (t1.isString() && t2.isString())
				return TypeString.getInstance();
			throw new SemanticException(lineNumber, "'+' requires two ints or two strings");
		}
		else if (op >= 1 && op <= 3) // MINUS, TIMES, DIVIDE
		{
			// PDF 2.6: -, *, / only work on int
			if (!t1.isInt() || !t2.isInt())
				throw new SemanticException(lineNumber, "arithmetic operator requires int operands");
			
			// PDF 2.6: Division by constant 0 is error
			if (op == 3 && right instanceof AstExpInt) {
				int val = ((AstExpInt) right).value;
				if (val == 0)
					throw new SemanticException(lineNumber, "division by zero");
			}
			return TypeInt.getInstance();
		}
		else if (op == 4 || op == 5) // LT, GT
		{
			// PDF 2.6: <, > only work on int
			if (!t1.isInt() || !t2.isInt())
				throw new SemanticException(lineNumber, "comparison operator requires int operands");
			return TypeInt.getInstance();
		}
		else if (op == 6) // EQ
		{
			// PDF 2.6: = equality testing
			if (!TypeUtils.canCompareEquality(t1, t2))
				throw new SemanticException(lineNumber, "cannot compare these types for equality");
			return TypeInt.getInstance();
		}
		
		throw new SemanticException(lineNumber, "unknown binary operator");
	}

	@Override
	public Integer getConstantValue() {
		Integer leftVal = left.getConstantValue();
		Integer rightVal = right.getConstantValue();
		if (leftVal == null || rightVal == null) return null;
		
		// op: 0=PLUS, 1=MINUS, 2=TIMES, 3=DIVIDE
		switch (op) {
			case 0: return leftVal + rightVal;
			case 1: return leftVal - rightVal;
			case 2: return leftVal * rightVal;
			case 3: return rightVal == 0 ? null : leftVal / rightVal;
			default: return null;
		}
	}
}

package ast;

import types.*;
import semantic.SemanticException;
import symboltable.*;

public class AstDecClass extends AstDec {
	/********/
	/* NAME */
	/********/
	public String name;

	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstTypeNameList dataMembers;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstDecClass(int lineNumber, String name, AstTypeNameList dataMembers) {
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		this.name = name;
		this.dataMembers = dataMembers;
	}

	/*********************************************************/
	/* The printing message for a class declaration AST node */
	/*********************************************************/
	@Override
	public void printMe() {
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		System.out.format("CLASS DEC = %s\n", name);
		if (dataMembers != null)
			dataMembers.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
				String.format("CLASS\n%s", name));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber, dataMembers.serialNumber);
	}

	@Override
	public Type semantMe() throws SemanticException {
		// Check class is not already declared in global scope
		if (SymbolTable.getInstance().find(name) != null)
			throw new SemanticException(lineNumber, "class '" + name + "' already defined");

		// Insert empty class type BEFORE processing members
		TypeClass emptyClass = new TypeClass(null, name, null);
		SymbolTable.getInstance().enter(name, emptyClass);

		// Begin class scope
		SymbolTable.getInstance().beginScope();

		// Semant data members
		TypeList memberTypes = null;
		if (dataMembers != null)
			memberTypes = dataMembers.semantMe();

		// End class scope
		SymbolTable.getInstance().endScope();

		// Update existing class entry
		emptyClass.dataMembers = memberTypes; 

		return null; // class declarations return null
	}
}
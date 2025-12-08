package ast;

import types.*;
import semantic.SemanticException;
import symboltable.*;

public class AstDecVar extends AstDec {
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String type;
	public String name;
	public AstExp initialValue;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstDecVar(int lineNumber, String type, String name, AstExp initialValue) {
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		this.type = type;
		this.name = name;
		this.initialValue = initialValue;
	}

	/************************************************************/
	/* The printing message for a variable declaration AST node */
	/************************************************************/
	public void printMe() {
		/****************************************/
		/* AST NODE TYPE = AST VAR DECLARATION */
		/***************************************/
		if (initialValue != null)
			System.out.format("VAR-DEC(%s):%s := initialValue\n", name, type);
		if (initialValue == null)
			System.out.format("VAR-DEC(%s):%s                \n", name, type);

		/**************************************/
		/* RECURSIVELY PRINT initialValue ... */
		/**************************************/
		if (initialValue != null)
			initialValue.printMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
				String.format("VAR\nDEC(%s)\n:%s", name, type));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (initialValue != null)
			AstGraphviz.getInstance().logEdge(serialNumber, initialValue.serialNumber);

	}

	@Override
	public Type semantMe() throws SemanticException {
		// 1. Check type exists
		Type t = SymbolTable.getInstance().find(type);
		if (t == null)
			throw new SemanticException(lineNumber, "type '" + type + "' does not exist");

		if (t == TypeVoid.getInstance())
			throw new SemanticException(lineNumber, "variable cannot be declared with type void");

		// 2. Check name does not already exist in the current scope
		if (SymbolTable.getInstance().findInCurrentScope(name) != null)
			throw new SemanticException(lineNumber, "variable '" + name + "' already declared in this scope");

		// 3. Check initial value (if exists)
		if (initialValue != null) {
			Type initType = initialValue.semantMe();

			// Nil assignment rules (arrays/classes only)
			if (initType == TypeNil.getInstance()) {
				if (!(t instanceof TypeClass) && !(t instanceof TypeArray))
					throw new SemanticException(lineNumber, "cannot assign nil to type '" + type + "'");
			} else {
				// primitive types: exact match
				if (t instanceof TypeInt) {
					if (!(initType instanceof TypeInt))
						throw new SemanticException(lineNumber, "type mismatch: expected int, got " + initType);
				} else if (t instanceof TypeString) {
					if (!(initType instanceof TypeString))
						throw new SemanticException(lineNumber, "type mismatch: expected string, got " + initType);
				}
				// class assignment: subtype allowed
				else if (t instanceof TypeClass) {
					if (!(initType instanceof TypeClass) &&
							!(initType == TypeNil.getInstance()))
						throw new SemanticException(lineNumber, "illegal assignment to class type");

					if (initType instanceof TypeClass) {
						TypeClass dst = (TypeClass) t;
						TypeClass src = (TypeClass) initType;

						if (!src.isSubclassOf(dst))
							throw new SemanticException(lineNumber, "class type mismatch");
					}
				}
				// array assignment: exact type only (no subtyping)
				else if (t instanceof TypeArray) {
					if (!t.equals(initType))
						throw new SemanticException(lineNumber, "array types are not interchangeable");
				}
			}
		}

		// 4. Enter variable into symbol table
		SymbolTable.getInstance().enter(name, t);

		return null;
	}

}

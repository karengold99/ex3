package ast;

import types.*;
import symboltable.*;
import semantic.SemanticException;

public class AstDecFunc extends AstDec
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String returnTypeName;
	public String name;
	public AstTypeNameList params;
	public AstStmtList body;
	
	public AstDecFunc(
		int lineNumber,
		String returnTypeName,
		String name,
		AstTypeNameList params,
		AstStmtList body)
	{
		super(lineNumber);
		serialNumber = AstNodeSerialNumber.getFresh();

		this.returnTypeName = returnTypeName;
		this.name = name;
		this.params = params;
		this.body = body;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void printMe()
	{
		/*************************************************/
		/* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
		/*************************************************/
		System.out.format("FUNC(%s):%s\n",name,returnTypeName);

		/***************************************/
		/* RECURSIVELY PRINT params + body ... */
		/***************************************/
		if (params != null) params.printMe();
		if (body   != null) body.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
                serialNumber,
			String.format("FUNC(%s)\n:%s\n",name,returnTypeName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (params != null) AstGraphviz.getInstance().logEdge(serialNumber,params.serialNumber);
		if (body   != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
	}

	@Override
	public Type semantMe() throws SemanticException
	{
		Type t;
		Type returnType = null;
		TypeList type_list = null;

		/************************************/
		/* [0] Check function name is unique */
		/************************************/
		if (SymbolTable.getInstance().findInCurrentScope(name) != null)
			throw new SemanticException(lineNumber, "function '" + name + "' already declared");

		/************************/
		/* [1] Check return type */
		/************************/
		returnType = SymbolTable.getInstance().find(returnTypeName);
		if (returnType == null)
			throw new SemanticException(lineNumber, "return type '" + returnTypeName + "' does not exist");

		/*************************************************/
		/* [2] Create function type and enter BEFORE body */
		/*     (allows recursive calls)                   */
		/*************************************************/
		// Build params type list first (iterate to count)
		for (AstTypeNameList it = params; it != null; it = it.tail)
		{
			t = SymbolTable.getInstance().find(it.head.type);
			if (t == null)
				throw new SemanticException(lineNumber, "parameter type '" + it.head.type + "' does not exist");
			if (t.isVoid())
				throw new SemanticException(lineNumber, "parameter cannot have void type");
			type_list = new TypeList(t, type_list);
		}

		TypeFunction funcType = new TypeFunction(returnType, name, type_list);
		SymbolTable.getInstance().enter(name, funcType);

		/*******************************************/
		/* [3] Begin Function Scope (tracks return) */
		/*******************************************/
		SymbolTable.getInstance().beginFuncScope(funcType);

		/*************************************/
		/* [4] Enter params into function scope */
		/*************************************/
		for (AstTypeNameList it = params; it != null; it = it.tail)
		{
			t = SymbolTable.getInstance().find(it.head.type);
			SymbolTable.getInstance().enter(it.head.name, t);
		}

		/*******************/
		/* [5] Semant Body */
		/*******************/
		if (body != null)
			body.semantMe();

		/*****************/
		/* [6] End Scope */
		/*****************/
		SymbolTable.getInstance().endFuncScope();

		/************************************************************/
		/* [6] Return value is irrelevant for function declarations */
		/************************************************************/
		return null;		
	}
	
}

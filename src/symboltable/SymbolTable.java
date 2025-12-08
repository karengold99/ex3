/***********/
/* PACKAGE */
/***********/
package symboltable;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import types.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SymbolTable
{
	private int hashArraySize = 13;
	
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SymbolTableEntry[] table = new SymbolTableEntry[hashArraySize];
	private SymbolTableEntry top;
	private int topIndex = 0;
	
	/*******************/
	/* Context State   */
	/*******************/
	private int curScopeDepth = 0;
	private TypeClass curClass = null;
	private Type returnType = null;
	private TypeFunction currFunc = null;
	
	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s)
	{
		if (s.charAt(0) == 'l') {return 1;}
		if (s.charAt(0) == 'm') {return 1;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 6;}
		if (s.charAt(0) == 'd') {return 6;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 6;}
		if (s.charAt(0) == 'S') {return 6;}
		return 12;
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name, Type t)
	{
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SymbolTableEntry next = table[hashValue];
	
		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SymbolTableEntry e = new SymbolTableEntry(name, t, hashValue, next, curScopeDepth, top, topIndex++);

		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		printMe();
	}

	/***********************************************/
	/* Find the inner-most scope element with name */
	/***********************************************/
	// PDF 2.7 - Name resolution:
	// 1. Search local scopes (symbol table)
	// 2. If inside class and not found, search class hierarchy (dataMembers)
	// 3. Finally global scope
	public Type find(String name)
	{
		SymbolTableEntry e;
				
		// Step 1: Search in symbol table (local → global)
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if (name.equals(e.name))
			{
				return e.type;
			}
		}
		
		// Step 2: If inside a class, search class hierarchy
		if (curClass != null)
		{
			// TODO: When TypeClass.findMember() is implemented:
			// Type t = curClass.findMember(name);
			// if (t != null) return t;
		}
		
		return null;
	}

	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope()
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be able to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		enter(
			"SCOPE-BOUNDARY",
			new TypeForScopeBoundaries("NONE"));
		
		curScopeDepth++;

		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		printMe();
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope()
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
		/**************************************************************************/
		while (top.name != "SCOPE-BOUNDARY")
		{
			table[top.index] = top.next;
			topIndex = topIndex -1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */		
		/**************************************/
		table[top.index] = top.next;
		topIndex = topIndex -1;
		top = top.prevtop;
		
		curScopeDepth--;

		/*********************************************/
		/* Print the symbol table after every change */		
		/*********************************************/
		printMe();
	}

	/*==================================================================*/
	/*                    SCOPE QUERIES                                 */
	/*==================================================================*/

	/******************************************/
	/* Check if we are at global scope        */
	/******************************************/
	public boolean isGlobalScope()
	{
		return curScopeDepth == 0;
	}

	/******************************************/
	/* Check if name exists in current scope  */
	/******************************************/
	// PDF 2.7 - Identifier may appear only once in a given scope
	public boolean existsInCurrentScope(String name)
	{
		return findInCurrentScope(name) != null;
	}

	/******************************************/
	/* Find name in current scope only        */
	/******************************************/
	// PDF 2.7 - For duplicate declaration check
	// Walks from top until SCOPE-BOUNDARY
	public Type findInCurrentScope(String name)
	{
		for (SymbolTableEntry e = top; e != null; e = e.prevtop)
		{
			// Reached scope boundary - not found in current scope
			if (e.name.equals("SCOPE-BOUNDARY"))
			{
				return null;
			}
			// Found the name in current scope
			if (e.name.equals(name))
			{
				return e.type;
			}
		}
		return null;
	}

	/*==================================================================*/
	/*                    CLASS SCOPE                                   */
	/*==================================================================*/

	/******************************************/
	/* Begin class scope                      */
	/******************************************/
	public void beginClassScope(TypeClass classType)
	{
		beginScope();
		this.curClass = classType;
	}

	/******************************************/
	/* End class scope                        */
	/******************************************/
	public void endClassScope()
	{
		endScope();
		this.curClass = null;
	}

	/******************************************/
	/* Check if inside a class                */
	/******************************************/
	public boolean insideClass()
	{
		return curClass != null;
	}

	/******************************************/
	/* Get the enclosing class                */
	/******************************************/
	public TypeClass getEnclosingClass()
	{
		return curClass;
	}

	/******************************************/
	/* Check if at class fields level         */
	/******************************************/
	// True if inside class but NOT inside a method
	public boolean inClassFieldsLevel()
	{
		return curClass != null && currFunc == null;
	}

	/*==================================================================*/
	/*                    FUNCTION SCOPE                                */
	/*==================================================================*/

	/******************************************/
	/* Begin function scope                   */
	/******************************************/
	public void beginFuncScope(TypeFunction func)
	{
		beginScope();
		this.currFunc = func;
		this.returnType = func.returnType;
	}

	/******************************************/
	/* End function scope                     */
	/******************************************/
	public void endFuncScope()
	{
		endScope();
		this.currFunc = null;
		this.returnType = null;
	}

	/******************************************/
	/* Get current function return type       */
	/******************************************/
	public Type getReturnType()
	{
		return returnType;
	}

	/******************************************/
	/* Get current function                   */
	/******************************************/
	public TypeFunction getCurrFunc()
	{
		return currFunc;
	}
	
	/******************************************/
	/* Check if we are inside a function      */
	/******************************************/
	public boolean insideFunction()
	{
		return currFunc != null;
	}

	/*==================================================================*/
	/*                    SPECIALIZED LOOKUPS                           */
	/*==================================================================*/

	// NOTE: Shadowing checks (PDF 2.2) will be in TypeClass:
	// - TypeClass.findMember(name) - for field shadowing (field can't shadow field OR method)
	// - TypeClass.findField(name) - for method shadowing (method can't shadow field)
	// - Signature matching for method override in TypeFunction

	/******************************************/
	/* Find only in class scope               */
	/******************************************/
	public Type findOnlyInClassScope(String name)
	{
		SymbolTableEntry e = top;
		Type result = null;
		
		while (e != null)
		{
			if (e.name.equals("SCOPE-BOUNDARY") && 
				e.prevtop != null && 
				e.prevtop.type instanceof TypeClass)
			{
				break;
			}
			if (e.name.equals("SCOPE-BOUNDARY"))
			{
				result = null;
			}
			else if (name.equals(e.name))
			{
				result = e.type;
			}
			
			e = e.prevtop;
		}
		
		return result;
	}
	
	/******************************************/
	/* Find a type that can be allocated      */
	/* For "new T" (class) or "new T[size]"   */
	/* defined class or array type            */
	/******************************************/
	public Type findNewableType(String typeName)
	{
		Type t = find(typeName);
		if (t == null) {
			return null;
		}
		if (t instanceof TypeClass) {
			return t;
		}
		// TODO: When TypeArray is created, add:
		// if (t instanceof TypeArray) { return t; }
		// Array types can be allocated with "new T[size]"
		return null;
	}

	
	public static int n=0;
	
	public void printMe()
	{
		int i=0;
		int j=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SymbolTableEntry it = table[i]; it!=null; it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtopIndex);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SymbolTable instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SymbolTable() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SymbolTable getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SymbolTable();

			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int",   TypeInt.getInstance());
			instance.enter("string", TypeString.getInstance());

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/

			/***************************************/
			/* [3] Enter library function PrintInt */
			/***************************************/
			instance.enter(
				"PrintInt",
				new TypeFunction(
					TypeVoid.getInstance(),
					"PrintInt",
					new TypeList(
						TypeInt.getInstance(),
						null)));

			/******************************************/
			/* [4] Enter library function PrintString */
			/******************************************/
			instance.enter(
				"PrintString",
				new TypeFunction(
					TypeVoid.getInstance(),
					"PrintString",
					new TypeList(
						TypeString.getInstance(),
						null)));
		}
		return instance;
	}
}

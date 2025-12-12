import java.io.*;
import java_cup.runtime.Symbol;
import ast.*;
import semantic.SemanticException;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AstDecList ast;
		FileReader fileReader;
		PrintWriter fileWriter;
		String inputFileName = argv[0];
		String outputFileName = argv[1];
		
		try
		{
			fileReader = new FileReader(inputFileName);
			fileWriter = new PrintWriter(outputFileName);
			l = new Lexer(fileReader);
			p = new Parser(l, fileWriter);
			ast = (AstDecList) p.parse().value;
			// ast.printMe(); // Disabled debug output
			
			try {
				ast.semantMe();
				fileWriter.print("OK");
			} catch (SemanticException e) {
				fileWriter.print("ERROR(" + e.line + ")");
			}
			
			fileWriter.close();
			AstGraphviz.getInstance().finalizeFile();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

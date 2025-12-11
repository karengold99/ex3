package ast;

import types.Type;
import semantic.SemanticException;

public abstract class AstCField extends AstNode {
	public AstCField(int lineNumber) {
		super(lineNumber);
	}

	@Override
	public abstract Type semantMe() throws SemanticException;
}


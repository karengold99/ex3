package ast;

import types.*;
import semantic.SemanticException;

public abstract class AstDec extends AstNode
{
    public AstDec(int lineNumber) {
        super(lineNumber);
    }

    @Override
    public abstract Type semantMe() throws SemanticException;
}
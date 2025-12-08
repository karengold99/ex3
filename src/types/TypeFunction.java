package types;

public class TypeFunction extends Type
{
    public Type returnType;
    public TypeList params;
    
    public TypeFunction(Type returnType, String name, TypeList params)
    {
        this.name = name;
        this.returnType = returnType;
        this.params = params;
    }

    @Override
    public boolean isFunction() { return true; }

    @Override
    public <R> R accept(TypeVisitor<R> visitor){return visitor.visit(this);}

    /** Check if signatures match (for method override) */
    public boolean signatureMatches(TypeFunction other)
    {
        // TODO: implement
        return false;
    }

    /** Get number of parameters */
    public int paramCount()
    {
        // TODO: implement
        return 0;
    }
}

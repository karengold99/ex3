package types;

public class TypeArray extends Type
{
    public Type elementType;

    public TypeArray(Type elementType)
    {
        super("array");
        this.elementType = elementType;
    }


    /*==================================================================*/
    /*                    TYPE CHECKS                                   */
    /*==================================================================*/
    @Override
    public boolean isArray() { return true; }

    @Override
    public <R> R accept(TypeVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
}

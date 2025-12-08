package types;

/**
 * Visitor that checks if a value can be assigned to a target type.
 * Usage: targetType.accept(new AssignmentChecker(valueType))
 */
public class AssignmentChecker implements TypeVisitor<Boolean>
{
    private Type valueType;
    
    public AssignmentChecker(Type valueType)
    {
        this.valueType = valueType;
    }

    @Override
    public Boolean visit(TypeInt type)
    {
        // TODO:
        return false;
    }

    @Override
    public Boolean visit(TypeString type)
    {
        // TODO:
        return false;
    }

    @Override
    public Boolean visit(TypeVoid type)
    {
        // TODO:
        return false;
    }

    @Override
    public Boolean visit(TypeNil type)
    {
        // TODO:
        return false;
    }

    @Override
    public Boolean visit(TypeClass type)
    {
        // TODO:
        return false;
    }

    @Override
    public Boolean visit(TypeArray type)
    {
        // TODO:
        return false;
    }

    @Override
    public Boolean visit(TypeFunction type)
    {
        // TODO:
        return false;
    }
}

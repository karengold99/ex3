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
        return valueType.isInt();
    }

    @Override
    public Boolean visit(TypeString type)
    {
        return valueType.isString();
    }

    @Override
    public Boolean visit(TypeVoid type)
    {
        return false;
    }

    @Override
    public Boolean visit(TypeNil type)
    {
        return false;
    }

    @Override
    public Boolean visit(TypeClass type)
    {
        if (valueType == type)
        {
            return true;
        }
        if (valueType.isNil())
        {
            return true;
        }
        if (valueType instanceof TypeClass)
        {
            TypeClass child = (TypeClass) valueType;
            TypeClass parent = type;
            return parent.isAncestorOf(child);
        }
        return false;
    }

    @Override
    public Boolean visit(TypeArray type)
    {
        if (valueType == type)
        {
            return true;
        }
        if (valueType.isNil())
        {
            return true;
        }
        return false;
    }

    @Override
    public Boolean visit(TypeFunction type)
    {
        if(!(valueType instanceof TypeFunction))
        {
            return false;
        }
        TypeFunction valueFunc = (TypeFunction) valueType;
        return valueFunc.signatureMatches(type);
    }
}

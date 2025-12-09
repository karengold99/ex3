package types;

public class TypeUtils
{
    /** Check if value can be assigned to target */
    public static boolean canAssignTo(Type value, Type target)
    {
        if (value == target)
        {
            return true;
        }
        if (value.isNil() && (target.isClass()||target.isArray()))
        {
            return true;
        }
        if (value.isClass() && target.isClass())
        {
            TypeClass valueClass = (TypeClass)value;
            TypeClass targetClass = (TypeClass)target;
            return targetClass.isAncestorOf(valueClass);
        }
        return false;
    }

    /** Check if expr type can be returned from function */
    public static boolean canReturn(Type exprType, Type returnType)
    {
        return canAssignTo(exprType, returnType);
    }

    /** Check if child extends parent */
    public static boolean isChild(Type child, Type parent)
    {
        if (!(child instanceof TypeClass) || !(parent instanceof TypeClass))
        {
            return false;
        }
        TypeClass childClass = (TypeClass) child;
        TypeClass parentClass = (TypeClass) parent;
        return parentClass.isAncestorOf(childClass);
    }

    /** Check if type is int or string */
    public static boolean isIntOrString(Type t)
    {
        return t.isInt() || t.isString();
    }
}

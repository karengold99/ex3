package types;

public class TypeUtils
{
    /**
     * Check if value can be assigned to target (PDF 2.4)
     */
    public static boolean canAssignTo(Type value, Type target)
    {
        // Same type
        if (value == target)
            return true;
        
        // Nil can be assigned to class or array
        if (value.isNil() && (target.isClass() || target.isArray()))
            return true;
        
        // Class: subclass allowed
        if (value.isClass() && target.isClass())
        {
            TypeClass valueClass = (TypeClass) value;
            TypeClass targetClass = (TypeClass) target;
            return valueClass.isSubclassOf(targetClass);
        }
        
        return false;
    }

    /** Check if expr type can be returned from function */
    public static boolean canReturn(Type exprType, Type returnType)
    {
        if (returnType.isVoid())
            return false;
        return canAssignTo(exprType, returnType);
    }

    /** Check if two types can be compared for equality */
    public static boolean canCompareEquality(Type t1, Type t2)
    {
        if (t1 == t2)
            return true;
        
        // Nil can be compared with class or array
        if (t1.isNil() && (t2.isClass() || t2.isArray()))
            return true;
        if (t2.isNil() && (t1.isClass() || t1.isArray()))
            return true;
        
        // Classes: comparable if one is subclass of other
        if (t1.isClass() && t2.isClass())
        {
            TypeClass c1 = (TypeClass) t1;
            TypeClass c2 = (TypeClass) t2;
            return c1.isSubclassOf(c2) || c2.isSubclassOf(c1);
        }
        
        return false;
    }

    /** Check if child extends parent */
    public static boolean isSubclass(Type child, Type parent)
    {
        if (!(child instanceof TypeClass) || !(parent instanceof TypeClass))
            return false;
        TypeClass childClass = (TypeClass) child;
        TypeClass parentClass = (TypeClass) parent;
        return childClass.isSubclassOf(parentClass);
    }

    /** Check if type is int or string */
    public static boolean isIntOrString(Type t)
    {
        return t.isInt() || t.isString();
    }
}

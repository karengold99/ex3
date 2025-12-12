package types;

public class TypeUtils
{
    /** Check if value can be assigned to target (PDF 2.4) */
    public static boolean canAssignTo(Type value, Type target)
    {
        if (value == target) return true;
        if (value.isNil() && (target.isClass() || target.isArray())) return true;
        if (value.isClass() && target.isClass())
            return ((TypeClass) value).isSubclassOf((TypeClass) target);
        // Arrays: anonymous array (name="array") can be assigned to named array with same element type
        if (value.isArray() && target.isArray()) {
            TypeArray va = (TypeArray) value;
            TypeArray ta = (TypeArray) target;
            return va.name.equals("array") && va.elementType == ta.elementType;
        }
        return false;
    }

    /** Check if two types can be compared for equality */
    public static boolean canCompareEquality(Type t1, Type t2)
    {
        if (t1 == t2) return true;
        if (t1.isNil() && (t2.isClass() || t2.isArray())) return true;
        if (t2.isNil() && (t1.isClass() || t1.isArray())) return true;
        if (t1.isClass() && t2.isClass()) {
            TypeClass c1 = (TypeClass) t1;
            TypeClass c2 = (TypeClass) t2;
            return c1.isSubclassOf(c2) || c2.isSubclassOf(c1);
        }
        return false;
    }

    /** Check if two function signatures match (for method override) */
    public static boolean signaturesMatch(TypeFunction f1, TypeFunction f2)
    {
        if (f1.returnType != f2.returnType) return false;
        TypeList p1 = f1.params, p2 = f2.params;
        while (p1 != null && p2 != null) {
            if (p1.head != p2.head) return false;
            p1 = p1.tail;
            p2 = p2.tail;
        }
        return p1 == null && p2 == null;
    }
}

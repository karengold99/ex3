package types;

public class TypeClass extends Type
{
    public TypeClass father;
    public TypeClassVarDecList dataMembers;
    
    public TypeClass(TypeClass father, String name, TypeClassVarDecList dataMembers)
    {
        this.name = name;
        this.father = father;
        this.dataMembers = dataMembers;
    }

    @Override
    public boolean isClass() { return true; }

    @Override
    public <R> R accept(TypeVisitor<R> visitor){return visitor.visit(this);}

    /** Check if this class is ancestor of other */
    public boolean isAncestorOf(TypeClass other)
    {
        // TODO: implement
        return false;
    }

    /** Find member in this class only */
    public TypeClassVarDec findMember(String memberName)
    {
        // TODO: implement
        return null;
    }

    /** Find member in this class OR parent classes */
    public TypeClassVarDec findMemberInHierarchy(String memberName)
    {
        // TODO: implement
        return null;
    }

    /** Find only fields (not methods) in hierarchy */
    public TypeClassVarDec findFieldInHierarchy(String fieldName)
    {
        // TODO: implement
        return null;
    }
}

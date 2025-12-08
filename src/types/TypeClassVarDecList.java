package types;

public class TypeClassVarDecList
{
    public TypeClassVarDec head;
    public TypeClassVarDecList tail;
    
    public TypeClassVarDecList(TypeClassVarDec head, TypeClassVarDecList tail)
    {
        this.head = head;
        this.tail = tail;
    }

    /** Find member by name in this list */
    public TypeClassVarDec find(String name)
    {
        // TODO: implement
        return null;
    }

    /** Create inherited copy of all members */
    public TypeClassVarDecList inherit()
    {
        // TODO: implement
        return null;
    }

    /** Insert new member at end */
    public TypeClassVarDecList insert(TypeClassVarDec d)
    {
        // TODO: implement
        return null;
    }
}

package types;

public abstract class Type
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;


	/*==================================================================*/
    /*                    TYPE CHECKS                                   */
    /*==================================================================*/
	public boolean isClass() { return false; }
    public boolean isArray() { return false; }
    public boolean isFunction() { return false; }
    public boolean isVoid() { return false; }
    public boolean isNil() { return false; }

	/*==================================================================*/
    /*                    VISITOR PATTERN                               */
    /*==================================================================*/
	public abstract <R> R accept(TypeVisitor<R> visitor);
	
}

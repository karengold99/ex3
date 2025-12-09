package types;

public class TypeInt extends Type
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TypeInt instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TypeInt() { super("int"); }

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TypeInt getInstance()
	{
		if (instance == null)
		{
			instance = new TypeInt();
			instance.name = "int";
		}
		return instance;
	}
	@Override
	public boolean isInt() { return true; }
	/*==================================================================*/
    /*                    VISITOR PATTERN                               */
    /*==================================================================*/
	@Override
    public <R> R accept(TypeVisitor<R> visitor) { return visitor.visit(this); }
}

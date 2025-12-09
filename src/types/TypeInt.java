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
	protected TypeInt() {}

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
	/*==================================================================*/
    /*                    VISITOR PATTERN                               */
    /*==================================================================*/
	@Override
    public <R> R accept(TypeVisitor<R> visitor) { return visitor.visit(this); }
}

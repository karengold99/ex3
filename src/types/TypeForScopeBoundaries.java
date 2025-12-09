package types;

public class TypeForScopeBoundaries extends Type
{
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TypeForScopeBoundaries(String name)
	{
		super(name);
	}
	
	@Override
    public <R> R accept(TypeVisitor<R> visitor)
    {
        throw new RuntimeException("Cannot visit SCOPE-BOUNDARY type");
    }
}

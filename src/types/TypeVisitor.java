package types;
/**
 * Visitor interface for Type hierarchy.
 * Allows adding new operations without modifying Type classes.
 * 
 * @param <R> Return type of the visitor operation
 */

public interface TypeVisitor<R> {
    R visit(TypeInt type);
    R visit(TypeString type);
    R visit(TypeVoid type);
    R visit(TypeNil type);
    R visit(TypeClass type);
    R visit(TypeArray type);
    R visit(TypeFunction type);
}

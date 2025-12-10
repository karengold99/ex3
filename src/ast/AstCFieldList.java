package ast;

import types.Type;
import semantic.SemanticException;

public class AstCFieldList extends AstNode {
	public AstCField head;
	public AstCFieldList tail;

	public AstCFieldList(AstCField head, AstCFieldList tail) {
		super(head != null ? head.lineNumber : 0);
		serialNumber = AstNodeSerialNumber.getFresh();
		this.head = head;
		this.tail = tail;
	}

	public void printMe() {
		System.out.println("CLASS FIELD LIST");
		if (head != null)
			head.printMe();
		if (tail != null)
			tail.printMe();

		AstGraphviz.getInstance().logNode(serialNumber, "CFIELD\nLIST");
		if (head != null)
			AstGraphviz.getInstance().logEdge(serialNumber, head.serialNumber);
		if (tail != null)
			AstGraphviz.getInstance().logEdge(serialNumber, tail.serialNumber);
	}

	@Override
	public Type semantMe() throws SemanticException {
		if (head != null)
			head.semantMe();
		if (tail != null)
			tail.semantMe();
		return null;
	}
}


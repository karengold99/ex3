package types;

public class TypeArray {
    private String elementType;
    private int size;

    public TypeArray(String elementType, int size) {
        this.elementType = elementType;
        this.size = size;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "TypeArray{" +
                "elementType='" + elementType + '\'' +
                ", size=" + size +
                '}';
    }
}
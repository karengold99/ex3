package semantic;

public class SemanticException extends RuntimeException {
    public int line;

    public SemanticException(int line, String message) {
        super(message);
        this.line = line;
    }
}
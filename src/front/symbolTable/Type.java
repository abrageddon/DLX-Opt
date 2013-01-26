package front.symbolTable;

public abstract class Type {

	public TypeKind kind;

	public TypeKind getTypeKind() {
		return kind;
	};

	public enum TypeKind {
		VAR, ARRAY;
	}
}


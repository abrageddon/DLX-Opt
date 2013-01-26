package front.symbolTable;

import java.util.ArrayList;
import java.util.List;

public class ArrayType extends Type {

	public List<Integer> dimSize;
	public Integer dim;
	
	public ArrayType() {
		super.kind = TypeKind.ARRAY;
		this.dim = 0;
		this.dimSize = new ArrayList<>(); 
	}
	
	public void addDimension(String dimSize) {
		this.dimSize.add(Integer.valueOf(dimSize));
		this.dim++;
	}

	public void addDimension(Integer dim) {
		this.dimSize.add(dim);
		this.dim++;
	}
	
}

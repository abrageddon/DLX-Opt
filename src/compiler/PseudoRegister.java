package compiler;

public class PseudoRegister extends Memory {

    @Override
    public boolean equals(Object obj) {
        if (PseudoRegister.class.isAssignableFrom(obj.getClass())) { 
            return number == ((PseudoRegister) obj).number; 
        }
        return false;
    }

    public PseudoRegister(Integer num) {
        number = num;
    }

}

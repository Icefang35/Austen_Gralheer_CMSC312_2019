//class for individual instructions containing name and runtime for each
public class Instruction {
    String type;
    int time;

    public Instruction(String type, int time){
        this.type = type;
        this.time = time;
    }

    public String toString(){
        return(type + " Runtime: " + time);
    }
}

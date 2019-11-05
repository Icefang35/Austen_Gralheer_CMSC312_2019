//class for individual instructions containing name and runtime for each
public class Instruction {
    String type;
    int time;
    boolean isCritical;

    public Instruction(String type, int time, boolean critical){
        this.type = type;
        this.time = time;
        isCritical = critical;
    }

    public String toString(){
        String instruction = "Type: " + type + "\n";

        if(type.contains("I/O")){
            instruction += "Memory: " + time + "\n";
        }
        else{
            instruction += "Runtime: " + time + "\n";
        }

        if(isCritical){
            instruction += "Critical Section";
        }

        return instruction;
    }
}

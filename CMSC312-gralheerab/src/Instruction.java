//class for individual instructions containing name and runtime for each
public class Instruction {
    String type;
    int time;
    int memory;
    boolean isCritical;

    public Instruction(String type, int time, int memory, boolean critical){
        this.type = type;
        this.time = time;
        this.memory = memory;
        isCritical = critical;
    }

    public String toString(){
        String instruction = "Type: " + type + "\n";

        instruction += "Memory: " + memory + "\n";
        instruction += "Runtime: " + time + "\n";

        if(isCritical){
            instruction += "Critical Section" + "\n";
        }

        return instruction;
    }
}

//class for individual instructions containing name and runtime for each
public class Instruction {
    String type;
    int time;
    int memory;
    boolean isCritical;
    boolean hasAllocated;

    public Instruction(String type, int time, int memory, boolean critical){
        this.type = type;
        this.time = time;
        this.memory = memory;
        isCritical = critical;
    }

    public String toString(){
        String instruction = "Type: " + type;

        //instruction += "Memory: " + memory + "\n";
        //instruction += "Runtime: " + time + "\n";

        if(isCritical){
            instruction += " Is the Critical Section" + "\n";
        } else {
            instruction += "\n";
        }

        return instruction;
    }
}

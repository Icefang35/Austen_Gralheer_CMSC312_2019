import java.util.Queue;

//Class for individual ProcessControlBlocks containing all of the information for individual processes
public class ProcessControlBlock {
    String processState;
    String jobType;
    int runtime;
    int memory;
    int pId;
    int programCounter;
    int priority;
    Queue<Instruction> instructions;

    public ProcessControlBlock(String pState, String jType, Queue<Instruction> instructs, int time, int mem, int pID){
        processState = pState;
        jobType = jType;
        instructions = instructs;
        runtime = time;
        memory = mem;
        pId = pID;
        //programCounter = pCounter;
        //this.priority = priority;
    }

    public String toString(){
        int numCalculate = 0;
        int numIO = 0;

        //TODO: add program counter, and priority
        String process =jobType + " " + pId + "\n" + processState + "\n";

        for(int i = 0; i < instructions.size(); i++){
            String instructType = instructions.peek().type;
            if(instructType.equals("Calculate")){
                numCalculate++;
            }
            else if(instructType.equals("I/O")){
                numIO++;
            }
        }
        process += "Runtime: " + runtime + "\n";
        process += "Memory: " + memory + "\n";
        process += "Instructions: " + instructions.size() + "\n";
        process += "CALCULATE: " + numCalculate + "\n";
        process += "I/0: " + numIO + "\n" + "\n";

        return process;
    }
}

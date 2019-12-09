import java.io.PipedWriter;
import java.util.Queue;

//Class for individual ProcessControlBlocks containing all of the information for individual processes
public class ProcessControlBlock {
    String processState;
    String jobType;
    int runtime;
    int memory;
    int pId;
    int instructCount;
    int numCalculate = 0;
    int numIO = 0;
    int programCounter;
    int priority;
    boolean isChild = false;
    boolean usesPipe = false;
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

        countInstructs();
    }

    public void sendMessage(PipedWriter writer){
        try{
            writer.write("This is the message sent through the pipeline! n");
            writer.flush();
        }
        catch(Exception e){
            System.out.println("Sending message to pipe failed.");
        }
    }

    private void countInstructs(){
        instructCount = instructions.size();

        for(int i = 0; i < instructions.size(); i++){
            String instructType = instructions.peek().type;
            if(instructType.equals("Calculate")){
                numCalculate++;
            }
            else if(instructType.equals("I/O")){
                numIO++;
            }
        }
    }

    public String toString(){


        //TODO: add program counter, and priority
        String process =jobType + " " + pId + "\n" + processState + "\n";

        process += "Runtime: " + runtime + "\n";
        process += "Memory: " + memory + "\n";
        process += "Instructions: " + instructCount + "\n";
        process += "CALCULATE: " + numCalculate + "\n";
        process += "I/0: " + numIO + "\n";

        if(isChild){
            process += "Is a Child" + "\n" + "\n";
        } else {
            process += "\n";
        }

        return process;
    }
}

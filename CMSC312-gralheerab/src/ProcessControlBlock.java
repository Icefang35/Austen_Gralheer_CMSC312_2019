import java.io.*;
import java.net.Socket;
import java.util.Queue;

//Class for individual ProcessControlBlocks containing all of the information for individual processes
public class ProcessControlBlock {
    String processState;
    String jobType;
    int runtime;
    int memory;
    int pId;
    int ParentId;
    int instructCount;
    int numCalculate = 0;
    int numIO = 0;
    int programCounter;
    int priority;
    boolean isChild = false;
    boolean usesPipe = false;
    boolean usesSocket = false;
    Queue<Instruction> instructions;
    PageTable pages;

    public ProcessControlBlock(String pState, String jType, int priority, Queue<Instruction> instructs, int time, int mem, int pID){
        processState = pState;
        jobType = jType;
        instructions = instructs;
        runtime = time;
        memory = mem;
        pId = pID;
        //programCounter = pCounter;
        this.priority = priority;

        instructCount = instructions.size();
        pages = new PageTable(memory);
        //countInstructs();
    }
    public ProcessControlBlock(String pState, String jType, Queue<Instruction> instructs, int time, int mem, int pID, int ParentID){
        processState = pState;
        jobType = jType;
        instructions = instructs;
        runtime = time;
        memory = mem;
        pId = pID;
        ParentId = ParentID;

        instructCount = instructions.size();
        pages = new PageTable(memory);
        isChild = true;
    }

    public void sendMessage(PipedWriter writer){
        try{
            writer.write("Pipeline ");
            writer.flush();
            writer.close();
        }
        catch(Exception e){
            System.out.println("Sending message to pipe failed.");
        }
    }

    public void writeToSocket() throws IOException {
        Socket sock = new Socket("localhost", 7777);

        OutputStream output = sock.getOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(output);

        dataOutput.writeUTF("Socket");
        dataOutput.flush();
        dataOutput.close();
        sock.close();
    }

    private void countInstructs(){
        instructCount = instructions.size();

        for(int i = 0; i < instructions.size(); i++){
            String instructType = instructions.peek().type;
            if(instructType.contains("Calculate")){
                numCalculate++;
            }
            else if(instructType.contains("I/O")){
                numIO++;
            }
        }
    }

    public String toString(){


        //TODO: add program counter
        String process =jobType + " " + pId + "\n" + processState + "\n";

        process += "Runtime: " + runtime + "\n";
        process += "Memory: " + memory + "\n";
        process += "Instructions: " + instructCount + "\n";
        //process += "CALCULATE: " + numCalculate + "\n";
        //process += "I/0: " + numIO + "\n";

        if(isChild){
            process += "Is a Child of Process " + ParentId + "\n";
        } else {
            process += "\n";
        }

        return process;
    }
}

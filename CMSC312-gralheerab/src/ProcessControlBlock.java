public class ProcessControlBlock {
    String processState;
    String jobType;
    int pId;
    int programCounter;
    int priority;
    String[] instructions;

    public ProcessControlBlock(String pState, String jType, String[] instructs){
        processState = pState;
        jobType = jType;
        instructions = instructs;
        //this.pId = pId;
        //programCounter = pCounter;
        //this.priority = priority;
    }

    public String ToString(){
        String process = "";
        return process;
    }
}

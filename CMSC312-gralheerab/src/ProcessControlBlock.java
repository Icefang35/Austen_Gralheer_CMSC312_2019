public class ProcessControlBlock {
    String processState;
    String jobType;
    int pId;
    int programCounter;
    int priority;

    public ProcessControlBlock(String pState, String jType, int pId, int pCounter, int priority){
        processState = pState;
        jobType = jType;
        this. pId = pId;
        programCounter = pCounter;
        this.priority = priority;
    }

    public String ToString(){
        String process = "";
        return process;
    }
}

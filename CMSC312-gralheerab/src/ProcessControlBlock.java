public class ProcessControlBlock {
    String processState;
    String jobType;
    int runtime;
    int pId;
    int programCounter;
    int priority;
    Instruction[] instructions;

    public ProcessControlBlock(String pState, String jType, Instruction[] instructs, int time){
        processState = pState;
        jobType = jType;
        instructions = instructs;
        runtime = time;
        //this.pId = pId;
        //programCounter = pCounter;
        //this.priority = priority;
    }

    public String toString(){
        int numCalculate = 0;
        int numIO = 0;

        //TODO: add pId, program counter, and priority
        String process =jobType + "\n" + processState + "\n";

        for(int i = 0; i < instructions.length; i++){
            String instructType = instructions[i].type;
            if(instructType.equals("Calculate")){
                numCalculate++;
            }
            else if(instructType.equals("I/O")){
                numIO++;
            }
        }
        process += "Runtime: " + runtime + "\n";
        process += "Instructions: " + instructions.length + "\n";
        process += "CALCULATE: " + numCalculate + "\n";
        process += "I/0: " + numIO + "\n";

        return process;
    }
}

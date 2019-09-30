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

    public String toString(){
        int numCalculate = 0;
        int numIO = 0;

        //TODO: add pId, program counter, and priority
        String process =jobType + "\n" + processState + "\n";

        for(int i = 0; i < instructions.length; i++){
            if(instructions[i].equals("Calculate")){
                numCalculate++;
            }
            else if(instructions[i].equals("I/O")){
                numIO++;
            }
        }
        process += "Instructions: " + instructions.length + "\n";
        process += "CALCULATE: " + numCalculate + "\n";
        process += "I/0: " + numIO + "\n";

        return process;
    }
}

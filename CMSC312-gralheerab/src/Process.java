//Class for each individual process, containing a ProcessControlBlock as well as some accessor methods for variables of the ProcessControlBlock
public class Process {
    ProcessControlBlock PCB;

    public Process(String jType, Instruction[] instructs, int runtime, int memory, int pID){
        PCB = new ProcessControlBlock("new", jType, instructs, runtime, memory, pID);
    }

    public String toString(){
        String process = PCB.toString();
        return process;
    }

    public int getRuntime(){
        return PCB.runtime;
    }

    public int getMemory() {
        return PCB.memory;
    }

    public int getPID(){
        return PCB.pId;
    }

    public String getState(){
        return PCB.processState;
    }

}

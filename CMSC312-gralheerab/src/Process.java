public class Process {
    ProcessControlBlock PCB;

    public Process(String jType, Instruction[] instructs, int runtime, int pID){
        PCB = new ProcessControlBlock("new", jType, instructs, runtime, pID);
    }

    public String toString(){
        String process = PCB.toString();
        return process;
    }

    public int getRuntime(){
        return PCB.runtime;
    }

    public int getPID(){
        return PCB.pId;
    }

}

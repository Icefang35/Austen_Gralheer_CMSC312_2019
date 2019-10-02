public class Process {
    ProcessControlBlock PCB;

    public Process(String jType, Instruction[] instructs, int runtime){
        PCB = new ProcessControlBlock("new", jType, instructs, runtime);
    }

    public String toString(){
        String process = PCB.toString();
        return process;
    }


}

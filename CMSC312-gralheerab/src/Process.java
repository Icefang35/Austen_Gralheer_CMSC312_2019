public class Process {
    ProcessControlBlock PCB;

    public Process(String jType, String[] instructs){
        PCB = new ProcessControlBlock("new", jType, instructs);
    }

    public String toString(){
        String process = PCB.toString();
        return process;
    }


}

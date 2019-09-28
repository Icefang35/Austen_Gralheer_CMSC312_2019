public class Process {
    ProcessControlBlock PCB;

    public Process(String jType, String[] instructs){
        PCB = new ProcessControlBlock("new", jType, instructs);
    }

    public String ToString(){
        String process = PCB.ToString();
        return process;
    }


}

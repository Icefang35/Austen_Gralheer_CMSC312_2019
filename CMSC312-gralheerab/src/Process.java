//Class for each individual process, containing a ProcessControlBlock as well as some accessor methods for variables of the ProcessControlBlock
public class Process extends Thread {
    ProcessControlBlock PCB;
    Dispatcher dispatcher;

    public Process(String jType, Instruction[] instructs, int runtime, int memory, int pID, Dispatcher dispatcher){
        PCB = new ProcessControlBlock("new", jType, instructs, runtime, memory, pID);
        this.dispatcher = dispatcher;
    }

    public void run(){
        try{
            dispatcher.runJob(PCB);
        }
        catch(Exception e){

        }
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

    public String getProcessState(){
        return PCB.processState;
    }

}

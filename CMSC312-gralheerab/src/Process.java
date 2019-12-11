import java.io.PipedWriter;
import java.util.Queue;

//Class for each individual process, containing a ProcessControlBlock as well as some accessor methods for variables of the ProcessControlBlock
public class Process extends Thread {
    ProcessControlBlock PCB;
    Dispatcher dispatcher;

    public Process(String jType, Queue<Instruction> instructs, int runtime, int memory, int pID){
        PCB = new ProcessControlBlock("NEW", jType, instructs, runtime, memory, pID);
        this.dispatcher = Dispatcher.getInstance();
    }

    @Override
    public void run(){
        try{
            dispatcher.setState(PCB, "RUN");
            dispatcher.runJob(this, false);
            System.out.println("Finished Process " + getPID() + "\n");
            //dispatcher.setState(PCB, "EXIT");
        }
        catch(Exception e){
            System.out.println("Failed to run process " + getPID());
        }
    }

    public void interrupt(InterruptHandler interrupt) throws InterruptedException {
        interrupt.join();
    }

    public void usePipe(){
        PCB.usesPipe = true;
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

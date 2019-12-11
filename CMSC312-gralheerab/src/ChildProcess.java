import java.util.Queue;

public class ChildProcess extends Process{

    public ChildProcess(String jType, Queue<Instruction> instructs, int runtime, int memory, int pID, int ParentID) {
        super(jType, instructs, runtime, memory, pID, 0);
        PCB = new ProcessControlBlock("NEW", jType, instructs, runtime, memory, pID, ParentID);
        PCB.isChild = true;
    }

    @Override
    public void run() {
        try{
            dispatcher.setState(PCB, "RUN");
            dispatcher.runJob(this, true);
            System.out.println("Finished Child Process " + getPID() + " Child of process " + getParent());
            //dispatcher.setState(PCB, "EXIT");
        }
        catch(Exception e){
            System.out.println("Failed to run process " + getPID());
        }
    }

    public int getParent(){
        return PCB.ParentId;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

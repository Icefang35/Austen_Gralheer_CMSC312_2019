import java.util.ArrayList;
import java.util.Queue;

//class for the process dispatcher that runs and changes the state of each process
public class Dispatcher {

    private static Dispatcher single_instance = null;
    public static ArrayList<Process> processes = new ArrayList<Process>();
    public static PhysicalMemory physical;

    public static Dispatcher getInstance(){
        if (single_instance == null){
            single_instance = new Dispatcher();
        }

        return single_instance;
    }

    //changes the state of individual jobs
    public static void setState (ProcessControlBlock PCB, String newState){
        PCB.processState = newState;
    }

    //"runs" each scheduled job by sleeping for the total runtime of each job job
//    public static void runJobs() throws InterruptedException{
//        boolean mutexLock = false;
//
//        for(int i = 0; i < processes.size(); i++){
//            Process current = processes.get(i);
//            System.out.println(current.getPID() + " - " + current.getRuntime());
//        }
//
//        for(int i = 0; i < processes.size(); i++){
//            //if(processes.get(i).getState().contains("ready")) {
//                Instruction[] instructions = processes.get(i).PCB.instructions;
//                setState(processes.get(i).PCB, "running");
//                System.out.println("Running job " + i);
//                for (int j = 0; j < instructions.length; j++) {
//                    if (instructions[j].isCritical) {
//                        mutexLock = true;
//                        Thread.sleep(instructions[j].time);
//                        mutexLock = false;
//                    } else {
//                        Thread.sleep(instructions[j].time);
//                    }
//                }
//                setState(processes.get(i).PCB, "terminated");
//                System.out.print(processes.get(i).toString());
//                //processes.remove(i);
//            //}
//        }
//
//        //if(processes.size() == 0){
//
//        //}
//        //else {
//            System.out.println("'runJobs' finished");
//        //}
//    }

    public static void runJobs() throws InterruptedException{
        for(int i = 0; i < processes.size(); i++){
            Process current = processes.get(i);
            System.out.println(current.getPID() + " - " + current.getRuntime());
        }

        //checkMemory()
        System.out.println();

        for(int i = 0; i < processes.size(); i++){
            //if(processes.get(i).getProcessState().contains("Ready")) {
                processes.get(i).start();
            //}
        }
    }

    public static void runJob(ProcessControlBlock job) throws InterruptedException{
        boolean mutexLock = false;

        //System.out.println(job.pId + " - " + job.runtime);

        Queue<Instruction> instructions = job.instructions;
        setState(job, "running");
        while(instructions.isEmpty() == false) {
            if (instructions.peek().isCritical) {
                if(!mutexLock) {
                    mutexLock = true;
                    System.out.println(job.pId + " " + instructions.peek().toString());
                    Thread.sleep(instructions.peek().time);
                    mutexLock = false;
                    instructions.remove();
                }
            } else {
                System.out.println(job.pId + " " + instructions.peek().toString());
                Thread.sleep(instructions.peek().time);
                instructions.remove();
            }
        }
        setState(job, "terminated");
        System.out.print(job.toString());
    }

    public ArrayList<Process> checkMemory(){
        ProcessControlBlock PCB;
        for (int i = 0; i < processes.size(); i++){
            PCB = processes.get(i).PCB;
            if (PCB.memory > physical.CheckMemory()){
                //physical.AllocateFrames(PCB.memory, PCB.pId);
                setState(processes.get(i).PCB, "ready");
            }
        }
        return processes;
    }
}

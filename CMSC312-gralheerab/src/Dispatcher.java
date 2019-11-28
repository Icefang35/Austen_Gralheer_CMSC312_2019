import java.util.ArrayList;

//class for the process dispatcher that runs and changes the state of each process
public class Dispatcher extends Thread {

    public static ArrayList<Process> processes = new ArrayList<Process>();

    public void run(){
        try{
            runJobs();
        }
        catch(Exception e){

        }
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

        for(int i = 0; i < processes.size(); i++){
            processes.get(i).start();
        }
    }

    public static void runJob(ProcessControlBlock job) throws InterruptedException{
        boolean mutexLock;

        //System.out.println(job.pId + " - " + job.runtime);

        Instruction[] instructions = job.instructions;
        setState(job, "running");
        //System.out.println("Running job " + i);
        for (int j = 0; j < instructions.length; j++) {
            if (instructions[j].isCritical) {
                mutexLock = true;
                Thread.sleep(instructions[j].time);
                mutexLock = false;
            } else {
                Thread.sleep(instructions[j].time);
            }
        }
        setState(job, "terminated");
        System.out.print(job.toString());
    }

    public static ArrayList<Process> checkMemory(ArrayList<Process> processes, PhysicalMemory physical){
        ProcessControlBlock PCB;
        for (int i = 0; i < processes.size(); i++){
            PCB = processes.get(i).PCB;
            if (PCB.memory > physical.CheckMemory()){
                physical.AllocateFrames(PCB.memory, PCB.pId);
                setState(processes.get(i).PCB, "ready");
            }
        }
        return processes;
    }
}

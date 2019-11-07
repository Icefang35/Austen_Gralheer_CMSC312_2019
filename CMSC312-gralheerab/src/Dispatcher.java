import java.util.ArrayList;

//class for the process dispatcher that runs and changes the state of each process
public class Dispatcher {

    //changes the state of individual jobs
    public static void setState (ProcessControlBlock PCB, String newState){
        PCB.processState = newState;
    }

    //"runs" each scheduled job by sleeping for the total runtime of each job job
    public static void runJobs(ArrayList<Process> processes) throws InterruptedException{
        boolean mutexLock = false;
        for(int i = 0; i < processes.size(); i++){
            Instruction[] instructions = processes.get(i).PCB.instructions;
            setState(processes.get(i).PCB, "running");
            System.out.println("Running job " + i);
            for(int j = 0; j < instructions.length; j++){
                if(instructions[j].isCritical){
                    mutexLock = true;
                    Thread.sleep(instructions[j].time);
                    mutexLock = false;
                }
                else{
                    Thread.sleep(instructions[j].time);
                }
            }
            setState(processes.get(i).PCB, "terminated");
            System.out.print(processes.get(i).toString());
        }

        System.out.println("'runJobs' finished");
    }
}

//class for the process dispatcher that runs and changes the state of each process
public class Dispatcher {

    //changes the state of individual jobs
    public static void setState (ProcessControlBlock PCB, String newState){
        PCB.processState = newState;
    }

    //"runs" each scheduled job by sleeping for the total runtime of each job job
    public static void runJobs(Process[] processes) throws InterruptedException{
        for(int i = 0; i < processes.length; i++){
            setState(processes[i].PCB, "running");
            System.out.println("Running job " + i);
            Thread.sleep(processes[i].getRuntime());
            setState(processes[i].PCB, "terminated");
            System.out.print(processes[i].toString());
        }

        System.out.println("'runJobs' finished");
    }
}

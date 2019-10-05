public class Dispatcher {

    public static void setState (ProcessControlBlock PCB, String newState){
        PCB.processState = newState;
    }


    public static void runJobs(Process[] processes) throws InterruptedException{
        for(int i = 0; i < processes.length; i++){
            setState(processes[i].PCB, "running");
            Thread.sleep(processes[i].getRuntime());
            setState(processes[i].PCB, "terminated");
            System.out.print(processes[i].toString());
        }

        System.out.println("'runJobs' finished");
    }
}

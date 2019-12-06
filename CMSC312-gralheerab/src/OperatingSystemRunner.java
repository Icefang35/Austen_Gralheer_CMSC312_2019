import java.io.FileNotFoundException;
import java.util.*;

public class OperatingSystemRunner {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        PhysicalMemory physical;
        VirtualMemory virtual;
        Scheduler jobScheduler = new Scheduler();
        ProcessGenerator generator = new ProcessGenerator();
        Scanner user = new Scanner(System.in);
        System.out.print("Number of processes to generate: ");
        generator.processCount = user.nextInt();
//        int processCount = user.nextInt();
        generator.start();
        //Process[] scheduledJobs = jobScheduler.shortJobFirst(processes);
        ArrayList<Process> scheduledJobs = jobScheduler.getSchedule();

        //prints out pID for each job in the order they have been scheduled
//        for(int i = 0; i < scheduledJobs.size(); i++){
//            Process current = scheduledJobs.get(i);
//            System.out.println(current.getPID() + " - " + current.getRuntime());
//        }

        //jobDispatcher.runJobs(scheduledJobs);
        user.close();
    }
}



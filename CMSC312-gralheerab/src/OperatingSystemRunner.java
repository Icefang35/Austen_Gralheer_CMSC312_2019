import java.io.FileNotFoundException;
import java.util.*;

public class OperatingSystemRunner {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        Scanner user = new Scanner(System.in);
        int processCount = user.nextInt();
        ProcessGenerator generator = new ProcessGenerator();
        Scheduler jobScheduler = new Scheduler();
        Dispatcher jobDispatcher = new Dispatcher();
        Process[] processes = generator.createProcesses(processCount);
        Process[] scheduledJobs = jobScheduler.shortJobFirst(processes);

        for(int i = 0; i < processCount; i++){
            Process current = scheduledJobs[i];
            System.out.println(current.getPID());
        }

        Dispatcher.runJobs(scheduledJobs);

    }
}



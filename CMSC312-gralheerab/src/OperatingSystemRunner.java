import java.io.FileNotFoundException;
import java.util.*;

public class OperatingSystemRunner {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        boolean running = true;
        ProcessGenerator generator = new ProcessGenerator();
        Dispatcher dispatcher = Dispatcher.getInstance();
        Scanner user = new Scanner(System.in);
        String nextStep;
        while(running) {
            System.out.print("Number of processes to generate: ");
            generator.processCount = user.nextInt();
            generator.createProcesses();
            System.out.print("Run Processes? (Y/N): ");
            nextStep = user.next();
            if(nextStep.contains("Y") || nextStep.contains("y")) {
                dispatcher.runJobs();
            }
            System.out.print("Quit? (Y/N): ");
            nextStep = user.next();
            if(nextStep.contains("Y") || nextStep.contains("y")){
                running = false;
            }
        }

        user.close();
    }
}



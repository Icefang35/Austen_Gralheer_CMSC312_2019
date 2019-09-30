import java.io.FileNotFoundException;
import java.util.*;

public class OperatingSystemRunner {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner user = new Scanner(System.in);
        int processCount = user.nextInt();
        ProcessGenerator generator = new ProcessGenerator();
        Process[] processes = generator.createProcesses(processCount);

        for(int i = 0; i < processCount; i++){
            Process current = processes[i];
            System.out.print(current.toString());
        }
    }
}



import java.io.FileNotFoundException;
import java.util.*;

public class OperatingSystemRunner {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        ProcessGenerator generator = new ProcessGenerator();
        Scanner user = new Scanner(System.in);
        System.out.print("Number of processes to generate: ");
        generator.processCount = user.nextInt();
        generator.createProcesses();

        user.close();
    }
}



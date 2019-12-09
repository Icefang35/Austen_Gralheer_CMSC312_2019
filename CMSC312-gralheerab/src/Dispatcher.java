import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


//class for the process dispatcher that runs and changes the state of each process
public class Dispatcher {

    private static Dispatcher single_instance = null;
    public static ArrayList<Process> processes = new ArrayList<Process>();
    public static PhysicalMemory physical;
    public static Random rand = new Random();


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

    public static void runJob(ProcessControlBlock job) throws InterruptedException, IOException {
        boolean mutexLock = false;
        boolean hasChild = false;
        boolean usesPipe = false;

        Queue<Instruction> instructions = job.instructions;
        PipeReaderProcess childReader = null;
        setState(job, "running");

        int random = rand.nextInt(100);
        if(random < 25){
            hasChild = true;
        }
        else if(random > 75){
            usesPipe = true;
        }

        if (usesPipe){
            PipedReader reader = new PipedReader();
            PipedWriter writer = new PipedWriter();

            writer.connect(reader);

            childReader = new PipeReaderProcess(job.pId, reader);

            childReader.start();
            job.sendMessage(writer);
            //Thread.sleep(2000);
            //childReader.stopReading();
        }

        if(hasChild){
            int childIndex = rand.nextInt(instructions.size());
            Queue<Instruction> forChild = new LinkedList<>(job.instructions);
            int currentIndex = 0;

            while (instructions.isEmpty() == false) {
                if(currentIndex == childIndex){
                    Process childProcess = new Process(job.jobType, forChild, job.runtime, job.memory, job.pId);

                    childProcess.setChild();
                    childProcess.start();
                    childProcess.join();
                    System.out.println("Child terminated");
                }

                if (instructions.peek().isCritical) {
                    if (!mutexLock) {
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
                currentIndex++;
            }
        } else {
            while (instructions.isEmpty() == false) {
                if (instructions.peek().isCritical) {
                    if (!mutexLock) {
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
        }

        if(usesPipe){
            childReader.stopReading();
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

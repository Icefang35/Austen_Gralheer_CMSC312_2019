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
    public static Random rand = new Random();
    public static VirtualMemory virtual = new VirtualMemory(rand.nextInt(1000) + 1000);
    public static PhysicalMemory physical = new PhysicalMemory(virtual.size / 10);


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

        System.out.println();

        processes.get(rand.nextInt(processes.size())).usePipe();
        checkMemory();

        while(processes.isEmpty() == false) {
            if(processesContainState("READY")) {
                for (Process process : processes) {
                    if (process.getProcessState().contains("READY")) {
                        //System.out.println(process.getPID());
                        process.start();
                    }
                }
            }
            Thread.sleep(500);

            if(!processesContainState("EXIT") && !processesContainState("RUN") && !processesContainState("WAIT")){
                checkMemory();
            }
        }
    }

    public static boolean processesContainState(final String status){
        return processes.stream().filter(o -> o.getProcessState().equals(status)).findFirst().isPresent();
    }

    public static void runJob(Process process) throws InterruptedException, IOException {
        boolean mutexLock = false;
        boolean hasChild = false;
        ProcessControlBlock job = process.PCB;
//        boolean usesPipe = false;

        Queue<Instruction> instructions = job.instructions;
        PipeReaderProcess childReader = null;

        int random = rand.nextInt(100);
        if(random < 25){
            hasChild = true;
        }

        if (job.usesPipe){
            PipedReader reader = new PipedReader();
            PipedWriter writer = new PipedWriter();

            writer.connect(reader);

            childReader = new PipeReaderProcess(job.pId, reader);

            childReader.start();
            job.sendMessage(writer);
        }

        if(hasChild){
            int childIndex = rand.nextInt(instructions.size());
            Queue<Instruction> forChild = new LinkedList<>(job.instructions);
            int currentIndex = 0;

            while (instructions.isEmpty() == false) {
                if(currentIndex == childIndex){
                    setState(job, "WAIT");
                    Process childProcess = new Process(job.jobType, forChild, job.runtime, job.memory, job.pId);

                    childProcess.setChild();
                    childProcess.start();
                    childProcess.join();
                    setState(job, "RUN");
                    System.out.println("Child terminated");
                }

                if (instructions.peek().isCritical) {
                    if (!mutexLock) {
                        mutexLock = true;
                        //System.out.println(job.pId + " " + instructions.peek().toString());
                        Thread.sleep(instructions.peek().time);
                        mutexLock = false;
                        instructions.remove();
                    }
                } else {
                    //System.out.println(job.pId + " " + instructions.peek().toString());
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
                        //System.out.println(job.pId + " " + instructions.peek().toString());
                        Thread.sleep(instructions.peek().time);
                        mutexLock = false;
                        instructions.remove();
                    }
                } else {
                    //System.out.println(job.pId + " " + instructions.peek().toString());
                    Thread.sleep(instructions.peek().time);
                    instructions.remove();
                }
            }
        }

        if(job.usesPipe){
            childReader.stopReading();
        }

        virtual.DeallocateFrames(job.pId);
        setState(job, "EXIT");
        System.out.print(process.toString());
        processes.remove(process);
    }

    public static void checkMemory(){
        ProcessControlBlock PCB;
        for (int i = 0; i < processes.size(); i++){
            PCB = processes.get(i).PCB;
            if (PCB.memory < virtual.CheckMemory()){
                if(virtual.AllocateFrames(PCB.memory, PCB.pId)) {
                    setState(processes.get(i).PCB, "READY");
                }
            }
        }
    }
}

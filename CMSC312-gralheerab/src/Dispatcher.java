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
    public static PhysicalMemory physical = new PhysicalMemory(virtual.size / 5);
    public static boolean mutexLock = false;
    public static int currentPage = 0;

    public static MemoryManagementUnit MMU = new MemoryManagementUnit();

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
            System.out.println(processes.get(i).getPID() + " T=" + processes.get(i).getRuntime() + " M=" + processes.get(i).getMemory());
        }

        System.out.println();

        processes.get(rand.nextInt(processes.size())).usePipe();

        MMU.start();

        checkVMemory();

        while(processes.isEmpty() == false) {
            if(processesContainState("READY")) {
                for (Process process : processes) {
                    if (process.getProcessState().contains("READY")) {
                        System.out.println(process.getPID());
                        process.start();
                    }
                }
            }
            Thread.sleep(1000);

            if(!processesContainState("EXIT") && !processesContainState("RUN") && !processesContainState("WAIT")){
                checkVMemory();
            }
        }

        MMU.running = false;
    }

    public static boolean processesContainState(final String status){
        return processes.stream().filter(o -> o.getProcessState().equals(status)).findFirst().isPresent();
    }

    public static void runJob(Process process) throws InterruptedException, IOException {
        boolean hasChild = false;
        ProcessControlBlock job = process.PCB;
//        boolean usesPipe = false;

        Queue<Instruction> instructions = job.instructions;
        Instruction instruction;
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
            int childIndex = 0;
            int currentIndex = 0;
            if(!instructions.isEmpty()) {
                childIndex = rand.nextInt(instructions.size());
            }
            Queue<Instruction> forChild = new LinkedList<>(job.instructions);
            while (instructions.isEmpty() == false) {
                instruction = instructions.peek();
                if(currentIndex == childIndex){
                    setState(job, "WAIT");
                    Process childProcess = new Process(job.jobType, forChild, job.runtime, job.memory, job.pId);
                    physical.DeallocateFrames(job);
                    childProcess.setChild();
                    childProcess.start();
                    childProcess.join();
                    setState(job, "RUN");
                    System.out.println("Child terminated\n");
                }

                if (instruction.isCritical) {
                    if (!mutexLock) {
                        if(checkPMemory(instruction, job)) {
                            mutexLock = true;
                            //System.out.println("Mutex locked");
                            System.out.println(job.pId + " " + instruction.toString());
                            Thread.sleep(instruction.time);
                            mutexLock = false;
                            //System.out.println("Mutex un-locked");
                            instructions.remove();
                            //physical.DeallocateFrames(job);
                            currentIndex++;
                        }
                    }
                } else {
                    if(checkPMemory(instruction, job)) {
                        System.out.println(job.pId + " " + instruction.toString());
                        Thread.sleep(instruction.time);
                        instructions.remove();
                        //physical.DeallocateFrames(job);
                        currentIndex++;
                    }
                }
            }
        } else {
            while (instructions.isEmpty() == false) {
                instruction = instructions.peek();
                if (instruction.isCritical) {
                    if (!mutexLock) {
                        if(checkPMemory(instruction, job)) {
                            mutexLock = true;
                            //System.out.println("Mutex locked");
                            System.out.println(job.pId + " " + instruction.toString());
                            Thread.sleep(instruction.time);
                            mutexLock = false;
                            //System.out.println("Mutex un-locked");
                            instructions.remove();
                            //physical.DeallocateFrames(job);
                        }
                    } else {
                        //System.out.println("Blocked by mutex lock");
                    }
                } else {
                    if(checkPMemory(instruction, job)) {
                        System.out.println(job.pId + " " + instruction.toString());
                        Thread.sleep(instruction.time);
                        instructions.remove();
                        //physical.DeallocateFrames(job);
                    }
                }
            }
        }

        if(job.usesPipe){
            childReader.stopReading();
        }

        //virtual.DeallocateFrames(job.pId);
        setState(job, "EXIT");
        System.out.print(process.toString());
        processes.remove(process);
    }

    public static void checkVMemory(){
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

    public static boolean checkPMemory(Instruction instruction, ProcessControlBlock job){
        if (instruction.memory < physical.CheckMemory()){
            physical.AllocateFrames(instruction.memory, job);
            return true;
        }
        return false;
    }
}

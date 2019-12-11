import org.jetbrains.annotations.NotNull;

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
    public static ArrayList<Process> processes = new ArrayList<>();
    public static ArrayList<ChildProcess> childProcesses = new ArrayList<>();
    public static Random rand = new Random();
    public static MemoryManagementUnit MMU;
    public static InterruptHandler interrupt = new InterruptHandler();
    public static boolean mutexLock = false;
    public static int currentPage = 0;
    public static int ChildId = -1;
    public static int schedule;

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

        MMU = new MemoryManagementUnit();

        if(schedule == 1) {
            for (int i = 0; i < processes.size(); i++) {
                System.out.println(processes.get(i).getPID() + " T=" + processes.get(i).getRuntime() + " M=" + processes.get(i).getMemory());
            }
        } else if (schedule == 2){
            for (int i = 0; i < processes.size(); i++) {
                System.out.println(processes.get(i).getPID() + " P=" + processes.get(i).getProcessPriority() + " T=" + processes.get(i).getRuntime());
            }
        }
        System.out.println();

        int communicate = rand.nextInt(processes.size());
        processes.get(communicate).usePipe();
        processes.get(communicate).useSocket();

        MMU.start();

        checkVMemory();

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
            if(rand.nextInt(100) > 80){
                interrupt.interruptProcesses();
            }

            if(!processes.isEmpty()) {
                if (!processesContainState("EXIT") && !processesContainState("RUN") && !processesContainState("WAIT")) {
                    checkVMemory();
                }
            }
        }

        System.out.println("No more processes");
        MMU.running = false;
    }

    public static boolean processesContainState(final String status){
        return processes.stream().filter(o -> o.getProcessState().contains(status)).findFirst().isPresent();
    }

    public static void runJob(@NotNull Process process, boolean isChild) throws InterruptedException, IOException {
        ProcessControlBlock job = process.PCB;
        PipeReaderProcess childReader = null;
        boolean hasChild = false;

        Queue<Instruction> instructions = job.instructions;
        Instruction instruction;

        int random = rand.nextInt(100);

        if(!isChild) {
            if (random < 30) {
                hasChild = true;
            }
        } else {
            if(random < 25) {
                hasChild = true;
            }
        }

        if (job.usesPipe){
            PipedReader reader = new PipedReader();
            PipedWriter writer = new PipedWriter();

            writer.connect(reader);

            childReader = new PipeReaderProcess(job.pId, reader);

            childReader.start();
            job.sendMessage(writer);
            Thread.sleep(14);
            childReader.reading = false;
        }

        if (job.usesSocket){
            SocketServerProcess childServer = new SocketServerProcess();
            childServer.start();

            job.writeToSocket();
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
                    ChildId++;
                    ChildProcess childProcess = new ChildProcess(job.jobType, forChild, job.runtime, job.memory, ChildId, job.pId);
                    childProcesses.add(childProcess);
                    MMU.physical.DeallocateFrames(job);
                    childProcess.start();
                    childProcess.join();
                    setState(job, "RUN");
                    System.out.println("Child terminated\n");
                    currentIndex++;
                }

                if (instruction.isCritical) {
                    if (!mutexLock) {
                        if(checkPMemory(instruction, job)) {
                            mutexLock = true;
                            //System.out.println("Mutex locked");
                            //System.out.println(job.pId + " " + instruction.toString());
                            Thread.sleep(instruction.time);
                            mutexLock = false;
                            //System.out.println("Mutex un-locked");
                            instructions.remove();
                            //physical.DeallocateFrames(job);
                            currentIndex++;
                        }
                    } else {
                        Thread.sleep(1);
                    }
                } else {
                    if(checkPMemory(instruction, job)) {
                        //System.out.println(job.pId + " " + instruction.toString());
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
                            //System.out.println(job.pId + " " + instruction.toString());
                            Thread.sleep(instruction.time);
                            mutexLock = false;
                            //System.out.println("Mutex un-locked");
                            instructions.remove();
                            //physical.DeallocateFrames(job);
                        }
                    } else {
                        Thread.sleep(1);
                    }
                } else {
                    if(checkPMemory(instruction, job)) {
                        //System.out.println(job.pId + " " + instruction.toString());
                        Thread.sleep(instruction.time);
                        instructions.remove();
                        //physical.DeallocateFrames(job);
                    }
                }
            }
        }

        MMU.virtual.DeallocateFrames(job);
        setState(job, "EXIT");
        //System.out.print(process.toString());
        if(!isChild) {
            processes.remove(process);
        } else {
            childProcesses.remove(process);
        }
    }

    public static void checkVMemory(){
        ProcessControlBlock PCB;
        for (int i = 0; i < processes.size(); i++) {
            PCB = processes.get(i).PCB;
            //if(!PCB.processState.contains("EXIT")) {
                if (PCB.memory < MMU.virtual.CheckMemory()) {
                    if (MMU.virtual.AllocateFrames(PCB.memory, PCB)) {
                        setState(processes.get(i).PCB, "READY");
                    }
                }
            //}
        }
    }

    public static boolean checkPMemory(Instruction instruction, ProcessControlBlock job){
        if (instruction.memory < MMU.physical.CheckMemory()){
            MMU.physical.AllocateFrames(instruction.memory, job);
            instruction.hasAllocated = true;
            return true;
        }
        return false;
    }
}

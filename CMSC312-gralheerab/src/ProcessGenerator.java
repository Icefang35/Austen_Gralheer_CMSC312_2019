import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ProcessGenerator extends Thread{
    Random rand = new Random();
    Scheduler scheduler = new Scheduler();
    Dispatcher dispatcher;
    public int processCount;

    public ProcessGenerator(){
        this.dispatcher = Dispatcher.getInstance();
    }

    int runtime;
    int memory;

    public void run(){
        try{
            createProcesses();
        }
        catch(Exception e){
            System.out.println("Process Creation Failed");
        }
    }

    public ArrayList<Process> createProcesses() throws FileNotFoundException, InterruptedException {
        //Scanner user = new Scanner(System.in);
        //System.out.print("Number of processes to generate: ");
        //processCount = user.nextInt();
        //System.out.println("got count");
        ArrayList<Process> processes = new ArrayList<Process>();
        Process process;
        String job = "";
        String type = "";
        String criticalSec = "";
        Scanner templateScanner;
        int percentCalculate = 0;
        int calcMax = 0;
        int calcMin = 0;
        int percentIO = 0;
        int IOMax = 0;
        int IOMin = 0;
        int pID = 1;
        Queue<Instruction> instructions;
        Dispatcher jobDispatcher = Dispatcher.getInstance();

        //randomly selects the job type, amount of instructions, and type of instructions for each process, as well as finding the correct template file
        for(int i = 0; i < processCount; i++){
            int jobType = rand.nextInt(4) + 1;
            switch (jobType){
                case 1:
                    job = "ProgramTemplates/Debugger.txt";
                    type = "Debugger";
                    break;
                case 2:
                    job = "ProgramTemplates/Internet_Browser.txt";
                    type = "Internet_Browser";
                    break;
                case 3:
                    job = "ProgramTemplates/Text_Editor.txt";
                    type = "Text_Editor";
                    break;
                case 4:
                    job = "ProgramTemplates/Video_Game_Player.txt";
                    type = "Video_Game_Player";
                    break;
                case 5:
                    job = "ProgramTemplates/Video_Processor.txt";
                    type = "Video_Processor";
                    break;
                default:
                    break;
            }

            File jobTemplate = new File(job);
            templateScanner = new Scanner(jobTemplate);

            //reads the template file and pulls out the percent of each instruction type that each process type can have, as well as the maximum and minimum run times for each instruction
            while(templateScanner.hasNextLine()){
                String curLine = templateScanner.nextLine();
                if(curLine.contains("CALCULATE")){
                    percentCalculate = templateScanner.nextInt();
                    calcMin = templateScanner.nextInt();
                    calcMax = templateScanner.nextInt();
                }
                else if(curLine.contains("I/0")){
                    percentIO = templateScanner.nextInt();
                    IOMin = templateScanner.nextInt();
                    IOMax = templateScanner.nextInt();
                }
                else if(curLine.contains("Early")){
                    criticalSec = "Early";
                }
                else if(curLine.contains("Middle")){
                    criticalSec = "Middle";
                }
                else if(curLine.contains("Late")){
                    criticalSec = "Late";
                }
            }

            templateScanner.close();
            instructions = createInstructions(percentCalculate, percentIO, calcMax, calcMin, IOMax, IOMin, criticalSec);
            process = new Process(type, instructions, runtime, memory, pID);
            scheduler.shortJobFirst(process);
            processes.add(process);
            pID++;
            runtime = 0;
            memory = 0;
        }

        jobDispatcher.processes = scheduler.getSchedule();
        jobDispatcher.runJobs();
        return processes;
    }

    //randomly sets the instruction type and runtime for each individual instruction
    public Queue<Instruction> createInstructions(int calculate, int IO, int calcMax, int calcMin, int IOMax, int IOMin, String criticalSec) {
        int instructionCount = rand.nextInt(9) + 1;
        int percentInstruction;
        String instructionType = "";
        int randCalc = calcMax - calcMin;
        int randIO = IOMax - IOMin;
        int instructionTime = 0;
        int criticalIndex = 0;
        boolean isCritical = false;
        Queue<Instruction> instructions = new Queue<Instruction>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Instruction> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] ts) {
                return null;
            }

            @Override
            public boolean add(Instruction instruction) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Instruction> collection) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public boolean offer(Instruction instruction) {
                return false;
            }

            @Override
            public Instruction remove() {
                return null;
            }

            @Override
            public Instruction poll() {
                return null;
            }

            @Override
            public Instruction element() {
                return null;
            }

            @Override
            public Instruction peek() {
                return null;
            }
        };

        if(criticalSec.contains("Early")){
            criticalIndex = instructionCount/4;
        }
        else if(criticalSec.contains("Middle")){
            criticalIndex = instructionCount/2;
        }
        else if(criticalSec.contains("Late")){
            criticalIndex = (instructionCount/4) * 3;
        }

        for(int i = 0; i < instructionCount; i++){
            percentInstruction = rand.nextInt(99) + 1;

            if(percentInstruction <= calculate){
                instructionType = "Calculate";
                instructionTime = rand.nextInt(randCalc) + calcMin;
                runtime += instructionTime;
                memory += 20;
            }
            else if(percentInstruction > calculate && percentInstruction <= (calculate + IO)){
                instructionType = "I/O";
                instructionTime = rand.nextInt(randIO) + IOMin;
                memory += instructionTime;
                runtime += 50;
            }

            if(i == criticalIndex){
                isCritical = true;
            }
            else{
                isCritical = false;
            }

            instructions.add(new Instruction(instructionType, instructionTime, isCritical));
        }

        return instructions;
    }
}

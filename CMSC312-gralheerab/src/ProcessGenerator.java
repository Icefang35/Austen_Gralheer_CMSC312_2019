import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ProcessGenerator{
    Random rand = new Random();
    Scheduler scheduler = new Scheduler();
    public volatile boolean running = true;
    public int processCount;

    public ProcessGenerator(){
        //this.dispatcher = Dispatcher.getInstance();
    }

    int runtime;
    int memory;
    int pID = 0;

    public void createProcesses() throws FileNotFoundException, InterruptedException {
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
            jobDispatcher.setState(process.PCB, "NEW");
            scheduler.shortJobFirst(process);
            processes.add(process);
            pID++;
            runtime = 0;
            memory = 0;
        }

        jobDispatcher.processes = scheduler.getSchedule();
        //jobDispatcher.runJobs();
        //running = false;
    }

    //randomly sets the instruction type and runtime for each individual instruction
    public Queue<Instruction> createInstructions(int calculate, int IO, int calcMax, int calcMin, int IOMax, int IOMin, String criticalSec) {
        int instructionCount = rand.nextInt(9) + 1;
        int percentInstruction;
        String instructionType = "";
        int randCalc = calcMax - calcMin;
        int randIO = IOMax - IOMin;
        int instructionTime = 0;
        int instructionMemory = 0;
        int criticalIndex = 0;
        boolean isCritical = false;
        Queue<Instruction> instructions = new LinkedList<>();

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
            percentInstruction = rand.nextInt(100) + 1;

            if(percentInstruction <= calculate){
                instructionType = "Calculate";
                instructionTime = rand.nextInt(randCalc) + calcMin;
                instructionMemory = 20;
                runtime += instructionTime;
                memory += instructionMemory;
            }
            else if(percentInstruction <= (calculate + IO)){
                instructionType = "I/O";
                instructionMemory = rand.nextInt(randIO) + IOMin;
                instructionTime = 50;
                memory += instructionMemory;
                runtime += instructionTime;
            }

            if(i == criticalIndex){
                isCritical = true;
            }
            else{
                isCritical = false;
            }

            instructions.add(new Instruction(instructionType, instructionTime, instructionMemory, isCritical));
        }

        return instructions;
    }
}

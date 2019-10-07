import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ProcessGenerator {
    Random rand = new Random();
    public ProcessGenerator(){
    }

    int runtime;
    public Process[] createProcesses(int processCount) throws FileNotFoundException {
        Process[] processes = new Process[processCount];
        String job = "";
        String type = "";
        Scanner templateScanner;
        int percentCalculate = 0;
        int calcMax = 0;
        int calcMin = 0;
        int percentIO = 0;
        int IOMax = 0;
        int IOMin = 0;
        int pID = 1;
        Instruction[] instructions;

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
            }

            templateScanner.close();
            instructions = createInstructions(percentCalculate, percentIO, calcMax, calcMin, IOMax, IOMin);
            processes[i] = new Process(type, instructions, runtime, pID);
            pID++;
            runtime = 0;
        }

        return processes;
    }

    //randomly sets the instruction type and runtime for each individual instruction
    public Instruction[] createInstructions(int calculate, int IO, int calcMax, int calcMin, int IOMax, int IOMin) {
        int instructionCount = rand.nextInt(9) + 1;
        int percentInstruction;
        String instructionType = "";
        int randCalc = calcMax - calcMin;
        int randIO = IOMax - IOMin;
        int instructionTime = 0;
        Instruction[] instructions = new Instruction[instructionCount];

        for(int i = 0; i < instructionCount; i++){
            percentInstruction = rand.nextInt(99) + 1;

            if(percentInstruction <= calculate){
                instructionType = "Calculate";
                instructionTime = rand.nextInt(randCalc) + calcMin;
                runtime += instructionTime;
            }
            else if(percentInstruction > calculate && percentInstruction <= (calculate + IO)){
                instructionType = "I/O";
                instructionTime = rand.nextInt(randIO) + IOMin;
                runtime += instructionTime;
            }

            instructions[i] = new Instruction(instructionType, instructionTime);
        }

        return instructions;
    }
}

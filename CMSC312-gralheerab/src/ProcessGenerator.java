import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ProcessGenerator {
    Random rand = new Random();
    public ProcessGenerator(){
    }

    public Process[] createProcesses(int processCount) throws FileNotFoundException {
        Process[] processes = new Process[processCount];
        String job = "";
        String type = "";
        Scanner templateScanner;
        int percentCalculate = 0;
        int percentIO = 0;
        String[] instructions;

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

            while(templateScanner.hasNextLine()){
                String curLine = templateScanner.nextLine();
                if(curLine.contains("CALCULATE")){
                    percentCalculate = templateScanner.nextInt();
                }
                else if(curLine.contains("I/0")){
                    percentIO = templateScanner.nextInt();
                }
            }

            templateScanner.close();
            instructions = createInstructions(percentCalculate, percentIO);
            processes[i] = new Process(type, instructions);
        }

        return processes;
    }

    public String[] createInstructions(int calculate, int IO) {
        int instructionCount = rand.nextInt(9) + 1;
        int percentInstruction;
        String instructionType = "";
        String[] instructions = new String[instructionCount];

        for(int i = 0; i < instructionCount; i++){
            percentInstruction = rand.nextInt(99) + 1;

            if(percentInstruction <= calculate){
                instructionType = "Calculate";
            }
            else if(percentInstruction > calculate && percentInstruction <= (calculate + IO)){
                instructionType = "I/O";
            }

            instructions[i] = instructionType;
        }

        return instructions;
    }
}

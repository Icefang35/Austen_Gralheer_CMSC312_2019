import java.util.*;

public class ProcessGenerator {
    int processCount;
    Random rand = new Random();

    public ProcessGenerator(int count){
        processCount = count;
    }

    public void createProcesses(){
        int jobType = rand.nextInt(5);
        String job = "";
        Scanner templateScanner;
        int percentCalculate = 0;
        int percentIO = 0;

        while(processCount > 0){
            switch (jobType){
                case 1:
                    job = "Debugger";
                    break;
                case 2:
                    job = "Internet_Browser";
                    break;
                case 3:
                    job = "Text_Editor";
                    break;
                case 4:
                    job = "Video_Game_Player";
                    break;
                case 5:
                    job = "Video_Processor";
                    break;
                default:
                    break;
            }

            templateScanner = new Scanner("Program Templates/" + job + ".txt");
            while(templateScanner.hasNextLine()){
                if(templateScanner.nextLine().contains("CALCULATE")){
                    percentCalculate = templateScanner.nextInt();
                }
                else if(templateScanner.nextLine().contains("I/0")){
                    percentIO = templateScanner.nextInt();
                }
            }

            createInstructions(percentCalculate, percentIO);

            processCount --;
        }
    }

    public void createInstructions(int calculate, int IO) {
        int instructionCount = rand.nextInt(10);
        int current = 0;
        int percentInstruction;
        String instructionType = "";
        String[] instructions = new String[instructionCount];

        for(int i = instructionCount; i > 0; i--){
            percentInstruction = rand.nextInt(100);

            if(percentInstruction <= calculate){
                instructionType = "Calculate";
            }
            else if(percentInstruction > calculate && percentInstruction <= (calculate + IO)){
                instructionType = "I/O";
            }

            instructions[current] = instructionType;
        }
    }
}

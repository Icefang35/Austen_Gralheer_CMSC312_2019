import java.util.*;

public class ProcessGenerator {
    int processCount;
    Random rand = new Random();

    public ProcessGenerator(int count){
        processCount = count;
        int jobType = rand.nextInt(5);
        String job;

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

            processCount --;
        }
    }

    public void createProcesses(){

    }

    public void createInstructions() {
        int instructionCount = rand.nextInt(10);
        int instructionPossible;

        for(int i = instructionCount; i > 0; i--){
            instructionPossible = rand.nextInt(100);

            switch(instructionPossible){

            }
        }
    }
}

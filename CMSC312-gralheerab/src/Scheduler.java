import java.util.ArrayList;

//class for the process scheduler that sorts the process based on scheduling algorithms
public class Scheduler {

    Dispatcher dispatch = new Dispatcher();

    public Scheduler(){
    }

    //Sorts the process using the shortest job first algorithm
    public Process[] shortJobFirst(ArrayList<Process> unScheduled){
        Process[] scheduled = new Process[unScheduled.size()];
        Instruction[] dummyInstructs = new Instruction[1];
        int shortTime;
        int shortIndex = -1;
        int i = 0;
        while(unScheduled.size() > 0){
            shortTime = 9000;
            for(int j = 0; j < unScheduled.size(); j++){
                if(unScheduled.get(j).getRuntime() < shortTime){
                    shortTime = unScheduled.get(j).getRuntime();
                    shortIndex = j;
                }
            }
            scheduled[i] = unScheduled.get(shortIndex);
            dispatch.setState(scheduled[i].PCB, "ready");
            unScheduled.remove(shortIndex);
            i++;
        }
        return scheduled;
    }

}

import java.lang.reflect.Array;
import java.util.ArrayList;

//class for the process scheduler that sorts the process based on scheduling algorithms
public class Scheduler {

    public Dispatcher dispatch = Dispatcher.getInstance();
    public static ArrayList<Process> scheduled = new ArrayList<Process>();

    public Scheduler(){
    }

    public static ArrayList<Process> getSchedule(){
        return scheduled;
    }

    //Sorts the process using the shortest job first algorithm
    public ArrayList<Process> shortJobFirst(Process unScheduled){
        int insertIndex = -1;
        for(int j = 0; j < scheduled.size(); j++){
            if(scheduled.get(j).getRuntime() > unScheduled.getRuntime()){
                insertIndex = j;
                break;
            }
        }
        if(insertIndex >= 0) {
            scheduled.add(insertIndex, unScheduled);
        }
        else{
            scheduled.add(unScheduled);
        }
        return scheduled;
    }

    public ArrayList<Process> Priority(Process unScheduled){
        int insertIndex = -1;
        for(int j = 0; j < scheduled.size(); j++){
            if(scheduled.get(j).getProcessPriority() > unScheduled.getProcessPriority()){
                insertIndex = j;
                break;
            } else if (scheduled.get(j).getProcessPriority() == unScheduled.getPriority()){
                if(scheduled.get(j).getRuntime() > unScheduled.getRuntime()){
                    insertIndex = j;
                    break;
                }
            }
        }
        if(insertIndex >= 0) {
            scheduled.add(insertIndex, unScheduled);
        }
        else{
            scheduled.add(unScheduled);
        }
        return scheduled;
    }
}

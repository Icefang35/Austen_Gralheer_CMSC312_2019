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
            //dispatch.setState(scheduled.get(i).PCB, "ready");
        return scheduled;
    }
//    Old method in case the new one breaks
//    public ArrayList<Process> shortJobFirst(ArrayList<Process> unScheduled){
//        int shortTime;
//        int shortIndex = -1;
//        int i = 0;
//        while(unScheduled.size() > 0){
//            shortTime = 9000;
//            for(int j = 0; j < unScheduled.size(); j++){
//                if(unScheduled.get(j).getRuntime() < shortTime){
//                    shortTime = unScheduled.get(j).getRuntime();
//                    shortIndex = j;
//                }
//            }
//            scheduled[i] = unScheduled.get(shortIndex);
//            dispatch.setState(scheduled[i].PCB, "ready");
//            unScheduled.remove(shortIndex);
//            i++;
//        }
//        return scheduled;
//    }

}

//class for the process scheduler that sorts the process based on scheduling algorithms
public class Scheduler {

    Dispatcher dispatch = new Dispatcher();

    public Scheduler(){
    }

    //Sorts the process using the shortest job first algorithm
    public Process[] shortJobFirst(Process[] unScheduled){
        Process[] scheduled = new Process[unScheduled.length];
        Instruction[] dummyInstructs = new Instruction[1];
        int shortTime;
        int shortIndex = -1;
        for(int i = 0; i < scheduled.length; i++){
            shortTime = 9000;
            for(int j = 0; j < unScheduled.length; j++){
                if(unScheduled[j].getRuntime() < shortTime){
                    shortTime = unScheduled[j].getRuntime();
                    shortIndex = j;
                }
            }
            scheduled[i] = unScheduled[shortIndex];
            dispatch.setState(scheduled[i].PCB, "ready");
            unScheduled[shortIndex] = new Process("invalid", dummyInstructs, 10000, 1);
        }
        return scheduled;
    }

}

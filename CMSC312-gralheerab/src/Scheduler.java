public class Scheduler {

    public Scheduler(){

    }

    public Process[] shortJobFirst(Process[] unScheduled){
        Process[] scheduled = new Process[unScheduled.length];
        Instruction[] dummyInstructs = new Instruction[1];
        int shortTime;
        int shortIndex = -1;
        for(int i = 0; i < scheduled.length; i++){
            shortTime = 9000;
            for(int j = 0; j < unScheduled.length; j++){
                ProcessControlBlock curPCB = unScheduled[j].PCB;
                if(curPCB.runtime < shortTime){
                    shortTime = curPCB.runtime;
                    shortIndex = j;
                }
            }
            scheduled[i] = unScheduled[shortIndex];
            unScheduled[shortIndex] = new Process("invalid", dummyInstructs, 10000);
        }
        return scheduled;
    }
}

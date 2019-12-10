public class MemoryManagementUnit extends Thread{
    boolean running;
    Dispatcher dispatcher;

    public MemoryManagementUnit(){
        running = true;
        dispatcher = Dispatcher.getInstance();
    }

    @Override
    public void run() {
        while(running){
            LastRecentlyUsed();
        }
    }

    public void LastRecentlyUsed(){
        Frame lastRecentV = null;
        Frame lastRecentP = null;

        for(Frame frame : dispatcher.virtual.memory){
            if(frame.pID != -1) {
                if (frame.secondChance == 0) {
                    frame.secondChance = 1;
                } else if (frame.secondChance == 1) {
                    if (lastRecentV == null) {
                        lastRecentV = frame;
                    } else {
                        if (frame.lastUsed.isAfter(lastRecentV.lastUsed)) {
                            lastRecentV = frame;
                        }
                    }
                }
            }
        }
        if(lastRecentV != null) {
            dispatcher.virtual.DeallocateFrame(lastRecentV.address);
        }

        for(Frame frame : dispatcher.physical.memory){
            if(frame.pID != -1) {
                if (frame.secondChance == 0) {
                    frame.secondChance = 1;
                } else if (frame.secondChance == 1) {
                    if (lastRecentP == null) {
                        lastRecentP = frame;
                    } else {
                        if (frame.lastUsed.isAfter(lastRecentP.lastUsed)) {
                            lastRecentP = frame;
                        }
                    }
                }
            }
        }
        if(lastRecentP != null) {
            dispatcher.physical.DeallocateFrame(lastRecentP.address);
        }
    }
}

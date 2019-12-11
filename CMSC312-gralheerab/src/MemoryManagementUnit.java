import java.util.Random;

public class MemoryManagementUnit extends Thread{
    public volatile boolean running;
    public Random rand = new Random();
    public VirtualMemory virtual = new VirtualMemory(rand.nextInt(1000) + 1000);
    public PhysicalMemory physical = new PhysicalMemory(virtual.size / 5);
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

        for(Frame frame : virtual.memory){
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
            virtual.DeallocateFrame(lastRecentV.address);
        }

        for(Frame frame : physical.memory){
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
            physical.DeallocateFrame(lastRecentP.address);
        }
    }
}

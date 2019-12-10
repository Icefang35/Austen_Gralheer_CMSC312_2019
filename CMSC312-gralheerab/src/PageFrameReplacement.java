public class PageFrameReplacement extends Thread {
    boolean running;
    Dispatcher dispatcher;

    public PageFrameReplacement(){
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
        Frame lastRecent = null;
        for(Frame frame : dispatcher.virtual.memory){
            if(frame.secondChance == 0){
                frame.secondChance = 1;
            } else if(frame.secondChance == 1){
                if(lastRecent == null){
                    lastRecent = frame;
                } else {
                    if(frame.lastUsed.isAfter(lastRecent.lastUsed)){
                        lastRecent = frame;
                    }
                }
            }
        }
        dispatcher.virtual.DeallocateFrame(lastRecent.address);
    }
}

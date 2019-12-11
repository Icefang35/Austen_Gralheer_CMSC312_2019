public class VirtualMemory {
    int size;
    int FreeSpace;
    Frame[] memory;
    Dispatcher dispatcher = Dispatcher.getInstance();

    public VirtualMemory(int size){
        this.size = size;
        FreeSpace = size;
        memory = new Frame[size];

        for(int i = 0; i < size; i ++){
            memory[i] = new Frame(i, -1);
        }
    }

    public boolean AllocateFrames(int size, ProcessControlBlock job){
        int i = 0;
        int j = 0;
        Page[] newPages = new Page[size];
        while(size > 0) {
            if( i < memory.length) {
                if(memory[i].pID == -1) {
                    memory[i].pID = job.pId;
                    FreeSpace--;
                    size--;
                    newPages[j] = new Page(dispatcher.currentPage, i, 'P');
                    dispatcher.currentPage++;
                    j++;
                }
                i++;
            }
            else{
                size = 0;
                DeallocateFrames(job);
                return false;
            }
        }
        for(Page page : newPages){
            job.pages.setPage(page);
        }
        return true;
    }

    public void DeallocateFrames(ProcessControlBlock job){
        int j = 0;
        for(int i = 0; i < memory.length; i++) {
            if (memory[i].pID == job.pId) {
                memory[i].pID = -1;
                FreeSpace++;
                for(int k = j; k < job.pages.length(); k++){
                    if(job.pages.pages[k].frameAddress == i){
                        job.pages.pages[k].frameAddress = -1;
                    }
                }
            }
        }
    }

    public void DeallocateFrame(int frameAddress){
        memory[frameAddress].pID = -1;
        FreeSpace++;
    }

    public int CheckMemory(){
        return FreeSpace;
    }
}

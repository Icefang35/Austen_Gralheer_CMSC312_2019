public class PhysicalMemory {
    int size;
    int FreeSpace;
    Frame[] memory;

    public PhysicalMemory(int size){
        this.size = size;
        FreeSpace = size;
        memory = new Frame[size];

        for(int i = 0; i < size; i ++){
            memory[i] = new Frame(i, -1);
        }
    }

    public boolean AllocateFrames(int size, int pID){
        int i = 0;
        while(size > 0) {
            if( i < memory.length) {
                if(memory[i].pID == -1) {
                    memory[i].pID = pID;
                    FreeSpace--;
                    size--;
                }
                    i++;
            }
            else{
                size = 0;
                DeallocateFrames(pID);
                return false;
            }
        }
        return true;
    }

    public void DeallocateFrames(int pID){
        for(int i = 0; i < memory.length; i++) {
            if (memory[i].pID == pID) {
                memory[i].pID = -1;
                FreeSpace++;
            }
        }
    }

    public int CheckMemory(){
        return FreeSpace;
    }
}

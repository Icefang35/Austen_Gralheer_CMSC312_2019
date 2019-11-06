public class VirtualMemory {
    int size;
    int FreeSpace;
    Frame[] memory;

    public VirtualMemory(int size){
        this.size = size;
        FreeSpace = size;
        memory = new Frame[size];
    }

    public void AllocateFrame(int address, int pID){
        memory[address] = new Frame(address, pID);
        FreeSpace --;
    }

    public void DeallocateFrame(int address){
        memory[address] = new Frame(0, 0);
        FreeSpace ++;
    }
}

import java.time.LocalTime;

public class Frame {
    int size = 1;
    int secondChance = 0;
    int address;
    int pID;
    int pageId;
    LocalTime lastUsed;

    public Frame(int address, int pID){
        this.address = address;
        this.pID = pID;
        lastUsed = LocalTime.now();
    }
}

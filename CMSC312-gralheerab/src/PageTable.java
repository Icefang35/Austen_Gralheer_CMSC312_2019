public class PageTable {
    Frame[] frames;
    int[] addresses;

    public PageTable(int frameSizes, int addressSize){
        frames = new Frame[frameSizes];
        addresses = new int[addressSize];
    }


}

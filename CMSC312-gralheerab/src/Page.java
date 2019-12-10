public class Page {
    int size = 1;
    int pageID;
    int frameAddress;
    char memory;

    public Page(int pageID, int frameAddress, char memory){
        this.pageID = pageID;
        this.frameAddress = frameAddress;
        this.memory = memory;
    }
}

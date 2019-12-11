import java.io.IOException;
import java.io.PipedReader;

public class PipeReaderProcess extends Thread {
    PipedReader reader;
    int parentID;
    public volatile boolean reading = true;

    public PipeReaderProcess(int parentID, PipedReader reader){
        this.parentID = parentID;
        this.reader = reader;
    }

    public void run()
    {
        try{
            System.out.println("Pipeline Open");
            readMessage();
        }
        catch (Exception e){
            System.out.println("Pipeline Closed");
        }
    }

    public void readMessage() throws IOException {
        while(reading){
            char c = (char) reader.read();
            if (c != -1) {
                System.out.print(c);
            }
        }
    }

    public void stopReading(){
        reading = false;
    }
}

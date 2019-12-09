import java.io.IOException;
import java.io.PipedReader;

public class PipeReaderProcess extends Thread {
    PipedReader reader;
    int parentID;
    boolean reading = true;

    public PipeReaderProcess(int parentID, PipedReader reader){
        this.parentID = parentID;
        this.reader = reader;
    }

    public void run()
    {
        try{
            readMessage();
        }
        catch (Exception e){
            System.out.println("Pipe Reader Failed");
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

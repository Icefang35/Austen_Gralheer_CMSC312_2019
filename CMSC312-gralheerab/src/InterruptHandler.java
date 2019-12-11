import java.util.ArrayList;
import java.util.Random;

public class InterruptHandler extends Thread{
    Dispatcher dispatcher = Dispatcher.getInstance();

    public void run(){
        try{
            interruptProcesses();
        } catch (Exception e){
            System.out.println("Interrupt failed");
        }
    }

    public void interruptProcesses() throws InterruptedException {

        ArrayList<Process> processes = dispatcher.processes;
        ArrayList<ChildProcess> children = dispatcher.childProcesses;

        for(Process process : processes){
            synchronized (process) {
                process.interrupt(this);
            }
        }

        for(ChildProcess child : children){
            synchronized (child) {
                child.interrupt(this);
            }
        }

        System.out.println("I/O interrupt\n");
    }
}

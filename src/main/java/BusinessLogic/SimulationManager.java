package BusinessLogic;


import Model.Server;
import Model.Task;
import javafx.scene.control.TextArea;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class SimulationManager implements Runnable{

    //data read from UI

    public int timeLimit;// = 60;  // maximum processing time - read from ui
    public int maxProcessingTime;// = 7;
    public int minProcessingTime;// = 1;
    public int maxArrival;// = 40;
    public int minArrival;// = 2;
    public int numberOfServers;// = 5;
    public int numberOfClients;// = 50;
    public boolean notExit = true;

    //ENTITY RESPONSIBLE WITH QUEUE MANAGEMENT AND CLIENT DISTRIBUTION
    private final Scheduler scheduler;

    //pool of tasks (client shopping in the store)
    private final List<Task> generatedTasks;
    private final Thread thread;
    private final TextArea ta;

    public SimulationManager(TextArea ta, int timeLimit, int maxProcessingTime, int minProcessingTime,
                             int maxArrival, int minArrival, int numberOfServers, int numberOfClients){
        this.timeLimit = timeLimit;
        this.maxArrival = maxArrival;
        this.minArrival = minArrival;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;

        this.ta = ta;

        generatedTasks = new ArrayList<>();
        thread = new Thread(this);
        scheduler = new Scheduler(numberOfServers);
        generateNRandomTasks();

        /// create log.txt
        createLog();
        clearLog();
    }

    public void clearLog(){
        try{
            FileWriter fw = new FileWriter("log.txt");
            fw.write("");
            fw.close();
        }catch(IOException e){
            System.out.println("error while clearing logs.");
            e.printStackTrace();
        }
    }

    public void createLog(){
        if(!new File("log.txt").exists())
        try {
            File log = new File("log.txt");
            if (!log.createNewFile())
                System.out.println("File already exists.");
        } catch (IOException e) {
            System.out.println("error creating file.");
            e.printStackTrace();
        }
    }

    public void exitApp(){
        for(Server s : scheduler.getServers()){
            s.notExit = false;
        }
        this.notExit = false;
        thread.interrupt();
    }

    private void generateNRandomTasks(){
        for(int i = 1; i <= numberOfClients; i++){
            generatedTasks.add(new Task(i, (int)Math.floor(Math.random()*(maxArrival-minArrival+1)+minArrival),
                    (int)Math.floor(Math.random()*(maxProcessingTime-minProcessingTime+1)+minProcessingTime)));
        }
        generatedTasks.sort(new SortByArrival());
    }

    @Override
    public void run() {
        int currentTime = 0;
        while(currentTime < timeLimit && notExit){
            Iterator<Task> itr = generatedTasks.iterator();
                while(itr.hasNext()){
                    Task t = itr.next();
                if((t).getArrivalTime() == currentTime){
                    scheduler.dispatchTask(t);
                    itr.remove();
                }
            }
            StringBuilder stri = new StringBuilder("Waiting list:");

            for(Task t : generatedTasks){
                stri.append("[").append(t.getID()).append(" ").append(t.getArrivalTime()).append(" ").append(t.getServiceTime()).append("]");
            }

            StringBuilder str = new StringBuilder();
            for(Server s : scheduler.getServers()){
                str.append(s.getTasksStr());

            }
            String outputString = "Current time: " + currentTime + "\n" + stri + "\n" + str +"\n";
            ta.setText(outputString);

            try {
                Files.write(Paths.get("log.txt"), outputString.getBytes(), StandardOpenOption.APPEND);
            }catch(IOException e){
                System.out.println("Error writing to file.");
                e.printStackTrace();
            }

            currentTime++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}


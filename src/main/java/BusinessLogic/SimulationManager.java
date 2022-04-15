package BusinessLogic;

import GUI.SimulationFrame;
import Model.Server;
import Model.Task;
import javafx.scene.control.TextArea;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class SimulationManager implements Runnable{

    //data read from UI
    public int timeLimit = 60; // maximum processing time - read from ui
    public int maxProcessingTime = 7;
    public int minProcessingTime = 1;
    public int maxArrival = 40;
    public int minArrival = 2;
    public int numberOfServers = 5;
    public int numberOfClients = 50;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    public boolean notExit = true;
    //ENTITY RESPONSIBLE WITH QUEUE MANAGEMENT AND CLIENT DISTRIBUTION
    private Scheduler scheduler;
    //frame for displaying simulation
    private SimulationFrame frame;
    //pool of tasks (client shopping in the store)
    private List<Task> generatedTasks;
    private Thread thread;
    private TextArea ta;

    public SimulationManager(){
        generatedTasks = new ArrayList<>();
        thread = new Thread(this);
        scheduler = new Scheduler(numberOfServers);
        generateNRandomTasks();
        createLog();
        clearLog();
    }
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
        Collections.sort(generatedTasks, new Sortbyarrival());
    }

    @Override
    public void run() {
        int currentTime = 0;
        //System.out.printf("Simulation started.\n");
        while(currentTime < timeLimit && notExit){
            Iterator itr = generatedTasks.iterator();
                while(itr.hasNext()){
                    Task t = (Task)itr.next();
                if((t).getArrivalTime() == currentTime){
                    scheduler.dispatchTask(t);
                    itr.remove();
                }
            }
            String stri = "Waiting list:";
            //System.out.println(currentTime);

            for(Task t : generatedTasks){
                stri += "[" + t.getID() + " " + t.getArrivalTime()+ " " + t.getServiceTime() + "]";
            }
            //System.out.println(stri);
            String str = "";
            for(Server s : scheduler.getServers()){
                str += s.getTasksStr();

            }
            String outputString = "Current time: " + currentTime + "\n" + stri + "\n" + str +"\n";
            ta.setText(outputString);
            //System.out.println(str);
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


package Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread{
    private BlockingQueue<Task> tasks; // priority blocking queue
    private AtomicInteger waitingPeriod;
    private int ID;
    public boolean notExit = true;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Server(){
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
    }

    public Server(int ID){
        this.ID = ID;
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
    }

    public Server(BlockingQueue tasks){
        this.tasks = tasks;
    }

    public BlockingQueue getTasks(){
        return tasks;
    }

    public String getTasksStr(){
        String ss = "Queue" + this.getID() + " ";
        for(Task t : tasks){
            ss += "[" + t.getID() + " " + t.getArrivalTime() + " "  + t.getServiceTime() + "] ";
        }
        ss += "\n";
        return ss;
    }
    public void addTask(Task newTask){
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }
    public void deleteTask(Task newTask){
        tasks.remove(newTask);
        waitingPeriod.addAndGet(-1);
    }
    public int getWaitingPeriod(){
        return Integer.parseInt(String.valueOf(this.waitingPeriod));
    }

    public void run(){
        while(notExit){
            if(!tasks.isEmpty()){
                if(tasks.element().getServiceTime() == 1){
                    deleteTask(tasks.element());
                }
                else {
                    tasks.element().setServiceTime(tasks.element().getServiceTime() - 1);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

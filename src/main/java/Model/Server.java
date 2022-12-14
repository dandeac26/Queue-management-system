package Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread{
    private final BlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private final int ID;
    public boolean notExit = true;

    public int getID() {
        return ID;
    }

    public Server(int ID){
        this.ID = ID;
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
    }

    public String getTasksStr(){
        StringBuilder ss = new StringBuilder("Queue" + this.getID() + " ");
        for(Task t : tasks){
            ss.append("[").append(t.getID()).append(" ").append(t.getArrivalTime()).append(" ").append(t.getServiceTime()).append("] ");
        }
        ss.append("\n");
        return ss.toString();
    }
    public void addTask(Task newTask){
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }
    public void deleteTask(Task newTask){
        if (!tasks.remove(newTask)){
            System.out.println("Can't remove task " + newTask.getID());
        }
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

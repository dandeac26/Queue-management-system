package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private Strategy strategy;
    public Thread[] threads;

    public Scheduler(int maxNoServers){//, int maxTasksPerServer){
        threads = new Thread[200];
        servers = Collections.synchronizedList(new ArrayList<Server>());
        this.maxNoServers = maxNoServers;
            for(int i = 0; i < maxNoServers; i++){

                Server s = new Server(i+1);
                servers.add(s);
                threads[i] = new Thread(s);
                threads[i].start();
            }

    }
    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        }
        if(policy == SelectionPolicy.SHORTEST_PATH) {
            strategy = new ConcreteStrategyTime();
        }
    }
    public void dispatchTask(Task t) {
        //call the strategy addTask method
        int minn = Integer.MAX_VALUE;
        int server_no = 1;
        for(Server s : servers){
            if(s.getWaitingPeriod() < minn){
                minn = s.getWaitingPeriod();
                server_no = s.getID();
            }
        }

        servers.get(server_no-1).addTask(t);
    }
    public List<Server>getServers(){
        return servers;
    }
}

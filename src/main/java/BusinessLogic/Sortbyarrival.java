package BusinessLogic;

import Model.Task;

import java.util.Comparator;

public class Sortbyarrival implements Comparator<Task> {
    public int compare(Task a, Task b){
        return a.getArrivalTime() - b.getArrivalTime();
    }
}

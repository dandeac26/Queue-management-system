package Model;

public class Task {


    private final int ID;
    private final int arrivalTime;
    private int serviceTime;

    public Task(int ID, int arrivalTime, int serviceTime){
        this.ID = ID;
        this.serviceTime = serviceTime;
        this.arrivalTime = arrivalTime;
    }

    public int getID() {return ID;}
    public int getServiceTime() {
        return serviceTime;
    }
    public int getArrivalTime(){
        return arrivalTime;
    }
    public void setServiceTime(int serviceTime){
        this.serviceTime = serviceTime;
    }
}

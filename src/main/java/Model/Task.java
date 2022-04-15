package Model;

public class Task {


    private int ID;
    private int arrivalTime;
    private int serviceTime;

    public Task(){

    }
    public Task(int ID, int arrivalTime, int serviceTime){
        this.ID = ID;
        this.serviceTime = serviceTime;
        this.arrivalTime = arrivalTime;
    }

    public int getID() {return ID;}
    public void setID(int ID) {this.ID = ID;}
    public int getServiceTime() {
        return serviceTime;
    }
    public int getArrivalTime(){
        return arrivalTime;
    }
    public void setServiceTime(int serviceTime){
        this.serviceTime = serviceTime;
    }
    public void setArrivalTime(int arrivalTime){
        this.arrivalTime = arrivalTime;
    }


}

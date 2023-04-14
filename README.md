# QUEUE MANAGEMENT APPLICATION

![app interface](https://i.imgur.com/IEqTrct.png)

## Objectives
The primary objective of this project is to create a queue management system that manages a list of clients in a series of servers and puts the client in the queue with the least waiting time. The secondary objectives are to create a user interface to input the required data for the project simulation, a simulation manager, a scheduler, a text file to keep logs of events every second, synchronization of threads, stop simulation, kill threads, generate a client list based on inputs, and update the user interface with the results of the program.

## Problem Analysis, Scenarios, Use Cases
This program takes a set of inputs from the keyboard in several text fields provided by the user interface. It generates a list of clients with random attributes based on the values given in the GUI. The Scheduler sorts the list based on each client's generated arrival time, ensuring that when the client arrives, they are put in an appropriate queue with the shortest waiting time.

Stopping the simulation and all the threads created by the program can be difficult. To overcome this, an attribute was created called "notExit," which functions as a flag. It was eventually the best way to solve the problem. The attribute "notExit" is controlled in the simulation manager by the "exitApp" function, which stops the threads created by the program.

This kind of application is useful in day-to-day scenarios where resources need to be managed. In this case, the resource is time. Each task needs to be completed as quickly as possible, so the best waiting time is calculated, and the task is sent to the shortest queue by time.

Step-by-step functionality includes taking a list of clients and sorting it by their arrival time. The program iterates through the client list, dispatches them to the scheduler when the current time equals the client's arrival time, and computes the minimum waiting times for each server. Then it adds the current task to that one queue. This is repeated until the simulation current time reaches the time limit.

## Design
Package:
![Package diagram](https://i.imgur.com/xsVvu2t.png)
Class:
![Class diagram](https://i.imgur.com/6PqdV0e.png)


### Simulation Manager

In the `SimulationManager` class, the most important method is the `run()` method, which iterates through the clients and dispatches them as follows:
```java
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
currentTime++;
// ...

```

Here, `generatedTasks` is an `ArrayList` of `Task` objects, and the `while` loop iterates through all the tasks in `generatedTasks`. If the `arrivalTime` of the task matches the `currentTime`, the task is dispatched using the `scheduler` object, and then removed from the list.

### Output Functionalities

When the `run()` method is executed, the simulation manager generates output, which is displayed in a text area on the user interface, and also written to a `log.txt` file. This is done using the following code:
```java
String outputString = "Current time: " + currentTime + "\n" + stri + "\n" + str +"\n";
ta.setText(outputString);
//System.out.println(str);
try {
Files.write(Paths.get("log.txt"), outputString.getBytes(), StandardOpenOption.APPEND);
}catch(IOException e){
System.out.println("Error writing to file.");
e.printStackTrace();
}
```

Here, `outputString` contains the output generated by the simulation manager, which includes the current time, the state of the scheduler (`stri`), and the state of the clients (`str`). The output is displayed in the text area using the `setText()` method, and also written to the `log.txt` file using the `Files.write()` method.

### Task Generator

The `generateNRandomTasks()` method in the `SimulationManager` class generates tasks with randomized attributes between the minimum and maximum arrival times, and minimum and maximum processing times. The tasks are sorted by arrival time using the `Collections.sort()` method, as shown below:
```java
private void generateNRandomTasks(){
for(int i = 1; i <= numberOfClients; i++){
generatedTasks.add(new Task(i, (int)Math.floor(Math.random()(maxArrival-minArrival+1)+minArrival),
(int)Math.floor(Math.random()(maxProcessingTime-minProcessingTime+1)+minProcessingTime)));
}
Collections.sort(generatedTasks, new Sortbyarrival());
}

class Sortbyarrival implements Comparator<Task> {
public int compare(Task a, Task b){
return a.getArrivalTime() - b.getArrivalTime();
}
}
```

The `Sortbyarrival` class is a comparator that compares two tasks based on their arrival times. The `generateNRandomTasks()` method uses the `Math.random()` method to generate random values for the task attributes, and adds the tasks to the `generatedTasks` list.

### Controller

The `Controller` class contains two methods that handle the user interface events. The `onSimulationButtonClick()` method creates a new `SimulationManager` object with the specified parameters, and starts a new thread to execute the `run()` method. The `onExitClick()` method stops the simulation by setting the `notExit` flag to `false`. Here is the code:
```java
@FXML
protected void onSimulationButtonClick() {
    gen = new SimulationManager(output, Integer.parseInt(simt.getText()),Integer.parseInt(maxs.getText()),Integer.parseInt(mins.getText()),Integer.parseInt(maxar.getText()),Integer.parseInt(minar.getText()),Integer.parseInt(Queues.getText()),Integer.parseInt(Clients.getText()));
    Thread t = new Thread(gen);
    //t.setDaemon(true);
    t.start();
}

@FXML
protected void onExitClick() {
    //System.out.println("Stopping...");
    if(gen != null){
        gen.exitApp();
    }
}
```
In onSimulationButtonClick, a SimulationManager object is created, and a new thread is started to run the simulation. In onExitClick, if gen is not null, the exitApp method of SimulationManager is called.

## Results
The queue management application was successfully implemented according to the design and objectives defined at the beginning of the project. The program has been tested using different input values, and it has performed well, providing the expected output.
The user interface provides an easy way to input the required data.

![input data](https://i.imgur.com/YjZXqVU.png)

The log file will contain the following set of data:
```
Current time: 1
Waiting list:[6 2 2][4 5 3][3 7 2][7 7 2][2 10 4][5 11 2][1 13 2]
Queue1 
Queue2 

Current time: 2
Waiting list:[4 5 3][3 7 2][7 7 2][2 10 4][5 11 2][1 13 2]
Queue1 [6 2 2] 
Queue2 

Current time: 3
Waiting list:[4 5 3][3 7 2][7 7 2][2 10 4][5 11 2][1 13 2]
Queue1 [6 2 1] 
Queue2 

Current time: 4
Waiting list:[4 5 3][3 7 2][7 7 2][2 10 4][5 11 2][1 13 2]
Queue1 
Queue2 

Current time: 5
Waiting list:[3 7 2][7 7 2][2 10 4][5 11 2][1 13 2]
Queue1 
Queue2 [4 5 3] 

Current time: 6
Waiting list:[3 7 2][7 7 2][2 10 4][5 11 2][1 13 2]
Queue1 
Queue2 [4 5 2] 
```
and so on...

One of the trickier parts of the project was stopping the simulation and all the threads created by the program. However, a flag attribute named "notExit" was implemented to solve this problem.

The program can be useful in day-to-day scenarios where it is required to manage resources, especially time. Each task needs to be completed as fast as possible, so the best waiting time is calculated and based on it. The

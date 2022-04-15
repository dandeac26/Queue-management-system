package com.application.main;

import BusinessLogic.SimulationManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    SimulationManager gen;

    @FXML private TextField Queues;
    @FXML private TextField Clients;
    @FXML private TextField minar;
    @FXML private TextField maxar;
    @FXML private TextField mins;
    @FXML private TextField maxs;
    @FXML private TextField simt;
    @FXML private TextArea output;

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
}
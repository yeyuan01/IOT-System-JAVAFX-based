package ca.uvic.seng330.assn3.mainMenu;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

import org.json.JSONObject;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.fxml.Initializable;

public class MainController implements Initializable{
	private Hub model;
	
	private ObservableList<Camera> cameraData = FXCollections.observableArrayList();
	private ObservableList<Lightbulb> lightbulbData = FXCollections.observableArrayList();
	private ObservableList<SmartPlug> smartplugData = FXCollections.observableArrayList();
	private ObservableList<Thermostat> thermostatData = FXCollections.observableArrayList();
	
	@FXML private Parent root;
	@FXML private TableView<Camera> cameraList = new TableView<Camera>();
	@FXML private TableView<Lightbulb> lightbulbList = new TableView<Lightbulb>();
	@FXML private TableView<SmartPlug> smartplugList = new TableView<SmartPlug>();
	@FXML private TableView<Thermostat> thermostatList = new TableView<Thermostat>();
	@FXML private ListView<String> alertlist;
	//private String currentuser;
	private ObservableList<String> alerts = FXCollections.observableArrayList();
	
	public void setmodel(Hub h) {
		model = h;
	}
	
	public Parent getView() {
		return root;
	}
	
	// add alertMessage to alert list
	public void showAlert(JSONObject pJsonMessage) {
		String alertMessage = pJsonMessage.toString();
		alerts.add(alertMessage);
		alertlist.setItems(alerts);
	}
	
	/*
	 * build device tables for admin user using all devices
	 */
	public void BuildAdminTable() {
		addCameraForAdminUser();
		addThermostatForAdminUser();
		addLightbulbForAdminUser();
		addSmartPlugForAdminUser();
	}

	/*
	 * build device tables for basic user using assigned device for that user
	 */
	public void BuildBasicTable(String uName) {
		ArrayList<Device> basicdevicelist = new ArrayList<Device>();
		if(model.getDevicesForUser(uName) == null) {
			cameraList.setItems(cameraData);
			lightbulbList.setItems(lightbulbData);
			smartplugList.setItems(smartplugData);
			thermostatList.setItems(thermostatData);
			return;
		}
		for(UUID d : model.getDevicesForUser(uName)) {
			basicdevicelist.add(model.getDeviceMap().get(d));
		}
		addCameraForBasicUser(basicdevicelist);
		addLightbulbForBasicUser(basicdevicelist);
		addSmartPlugForBasicUser(basicdevicelist);
		addThermostatForBasicUser(basicdevicelist);
	}

	
	/*
	 * helper methods to build device tables for admin user
	 */
	private void addCameraForAdminUser() {
	    cameraData = FXCollections.observableArrayList();
	    for (Device d : model.getDeviceMap().values()) {
	    	if (d instanceof Camera) {
	    		cameraData.add((Camera) d);
	    	}
	    }
	    cameraList.setItems(cameraData);
	}
	
  	private void addThermostatForAdminUser() {
	    thermostatData = FXCollections.observableArrayList();
	    for (Device d : model.getDeviceMap().values()) {
	    	if (d instanceof Thermostat) {
	    		thermostatData.add((Thermostat) d);
	    	}
	    }
	    thermostatList.setItems(thermostatData);
	}
  
  	private void addLightbulbForAdminUser() {
	    lightbulbData = FXCollections.observableArrayList();
	    for (Device d : model.getDeviceMap().values()) {
	    	if (d instanceof Lightbulb) {
	    		lightbulbData.add((Lightbulb) d);
	    	}
	    }
	    lightbulbList.setItems(lightbulbData);
	}

  	private void addSmartPlugForAdminUser() {
	    smartplugData = FXCollections.observableArrayList();
	    for (Device d : model.getDeviceMap().values()) {
	    	if (d instanceof SmartPlug) {
	    		smartplugData.add((SmartPlug) d);
	    	}
	    }
	    smartplugList.setItems(smartplugData);
	}

  	
  	/*
	 * helper methods to build device tables for basic user
	 */
  	private void addCameraForBasicUser(ArrayList<Device> basicdevicelist) {
	    cameraData = FXCollections.observableArrayList();
	    for (Device d : basicdevicelist) {
	    	if (d instanceof Camera) {
	    		cameraData.add((Camera) d);
	    	}
	    }
	    cameraList.setItems(cameraData);
  	}

  	private void addThermostatForBasicUser(ArrayList<Device> basicdevicelist) {
	    thermostatData = FXCollections.observableArrayList();
	    for (Device d : basicdevicelist) {
	    	if (d instanceof Thermostat) {
	    		thermostatData.add((Thermostat) d);
	    	}
	    }
	    thermostatList.setItems(thermostatData);
  	}

  	private void addLightbulbForBasicUser(ArrayList<Device> basicdevicelist) {
	    lightbulbData = FXCollections.observableArrayList();
	    for (Device d : basicdevicelist) {
	    	if (d instanceof Lightbulb) {
	    		lightbulbData.add((Lightbulb) d);
	    	}
	    }
	    lightbulbList.setItems(lightbulbData);
  	}

  	private void addSmartPlugForBasicUser(ArrayList<Device> basicdevicelist) {
	    smartplugData = FXCollections.observableArrayList();
	    for (Device d : basicdevicelist) {
	    	if (d instanceof SmartPlug) {
	    		smartplugData.add((SmartPlug) d);
	    	}
	    }
	    smartplugList.setItems(smartplugData);
  	}
	
	
	@Override
	public void initialize(URL url,ResourceBundle rb)
	{
		initializeCameraTableView();
		initializeThermostatTableView();
		initializeSmartPlugTableView();
		initializeLightbulbTableView();
	}

	@SuppressWarnings("unchecked")
	private void initializeCameraTableView() {
	    //camera table
	    TableColumn<Camera, String> c1 = new TableColumn<Camera, String>("UUID");
	    c1.setMinWidth(100);
	    c1.setCellValueFactory(  
	            new PropertyValueFactory<Camera, String>("Identifier")); 
	    TableColumn<Camera, String> c2 = new TableColumn<Camera, String>("Status");
	    c2.setMinWidth(100);
	    c2.setCellValueFactory(  
	            new PropertyValueFactory<Camera, String>("Status")); 
	    TableColumn<Camera, String> c3 = new TableColumn<Camera, String>("On/Off");
	    c3.setMinWidth(100);
	    c3.setCellValueFactory(  
	            new PropertyValueFactory<Camera, String>("ToggleButton")); 
	    TableColumn<Camera, String> c4 = new TableColumn<Camera, String>("Record");
	    c4.setMinWidth(100);
	    c4.setCellValueFactory(  
	            new PropertyValueFactory<Camera, String>("RecordButton")); 
	    TableColumn<Camera, StackPane> c6 = new TableColumn<Camera, StackPane>("Video");
	    c6.setMinWidth(200);
	    c6.setCellValueFactory(  
	            new PropertyValueFactory<Camera, StackPane>("stackPane"));
	    cameraList.getColumns().addAll(c1, c2, c3,c4,c6);
	}
	
	@SuppressWarnings("unchecked")
	private void initializeThermostatTableView() {
	    //thermostat table
	    TableColumn<Thermostat, String> t1 = new TableColumn<Thermostat, String>("UUID");
	    t1.setMinWidth(100);
	    t1.setCellValueFactory(  
	            new PropertyValueFactory<Thermostat, String>("Identifier")); 
	    TableColumn<Thermostat, String> t2 = new TableColumn<Thermostat, String>("Status");
	    t2.setMinWidth(100);
	    t2.setCellValueFactory(  
	            new PropertyValueFactory<Thermostat, String>("Status")); 
	    TableColumn<Thermostat, String> t3 = new TableColumn<Thermostat, String>("On/Off");
	    t3.setMinWidth(100);
	    t3.setCellValueFactory(  
	            new PropertyValueFactory<Thermostat, String>("ToggleButton")); 
	    TableColumn<Thermostat, String> t4 = new TableColumn<Thermostat, String>("Temperature");
	    t4.setMinWidth(100);
	    t4.setCellValueFactory(  
	            new PropertyValueFactory<Thermostat, String>("Temptext")); 
	    TableColumn<Thermostat, String> t5 = new TableColumn<Thermostat, String>("Unit");
	    t5.setMinWidth(100);
	    t5.setCellValueFactory(  
	            new PropertyValueFactory<Thermostat, String>("Unit")); 
	    thermostatList.getColumns().addAll(t1, t2, t3,t4,t5);
	}

	@SuppressWarnings("unchecked")
	private void initializeSmartPlugTableView() {
	    //SmartPlug table
	    TableColumn<SmartPlug, String> s1 = new TableColumn<SmartPlug, String>("UUID");
	    s1.setMinWidth(100);
	    s1.setCellValueFactory(  
	            new PropertyValueFactory<SmartPlug, String>("Identifier")); 
	    TableColumn<SmartPlug, String> s2 = new TableColumn<SmartPlug, String>("Status");
	    s2.setMinWidth(100);
	    s2.setCellValueFactory(  
	            new PropertyValueFactory<SmartPlug, String>("Status")); 
	    TableColumn<SmartPlug, String> s3 = new TableColumn<SmartPlug, String>("Toggle");
	    s3.setMinWidth(100);
	    s3.setCellValueFactory(  
	            new PropertyValueFactory<SmartPlug, String>("ToggleButton"));
	    TableColumn<SmartPlug, String> s4 = new TableColumn<SmartPlug, String>("On/Off");
	    s4.setMinWidth(100);
	    s4.setCellValueFactory(  
	            new PropertyValueFactory<SmartPlug, String>("Onoff"));
	    smartplugList.getColumns().addAll(s1,s2,s3,s4);
	}

	@SuppressWarnings("unchecked")
	private void initializeLightbulbTableView() {
	    //Lightbulb table
	    TableColumn<Lightbulb, String> l1 = new TableColumn<Lightbulb, String>("UUID");
	    l1.setMinWidth(100);
	    l1.setCellValueFactory(  
	            new PropertyValueFactory<Lightbulb, String>("Identifier")); 
	    TableColumn<Lightbulb, String> l2 = new TableColumn<Lightbulb, String>("Status");
	    l2.setMinWidth(100);
	    l2.setCellValueFactory(  
	            new PropertyValueFactory<Lightbulb, String>("Status")); 
	    TableColumn<Lightbulb, String> l3 = new TableColumn<Lightbulb, String>("Toggle");
	    l3.setMinWidth(100);
	    l3.setCellValueFactory(  
	            new PropertyValueFactory<Lightbulb, String>("ToggleButton"));
	    TableColumn<Lightbulb, String> l4 = new TableColumn<Lightbulb, String>("On/Off");
	    l4.setMinWidth(100);
	    l4.setCellValueFactory(  
	            new PropertyValueFactory<Lightbulb, String>("Onoff"));
	    lightbulbList.getColumns().addAll(l1,l2,l3,l4);
	}


}

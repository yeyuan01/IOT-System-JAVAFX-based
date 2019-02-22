package ca.uvic.seng330.assn3.adminMenu;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;
import ca.uvic.seng330.assn2.user.BasicUser;
import ca.uvic.seng330.assn2.user.User;
import ca.uvic.seng330.assn3.CameraController;
import ca.uvic.seng330.assn3.LightbulbController;
import ca.uvic.seng330.assn3.SmartPlugController;
import ca.uvic.seng330.assn3.ThermostatController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class AdminController implements Initializable{
	@FXML private Parent root;
	@FXML private Button Shutdown;
	@FXML private Button checkStatus;
	
	/* add device menu */	
	@FXML private Button addCamera;
	@FXML private Button addThermostat;
	@FXML private Button addSmartPlug;
	@FXML private Button addLightbulb;
	
	/* add user menu */
	@FXML private TextField username;
	@FXML private TextField password;
	@FXML private CheckBox isAdmin;
	@FXML private Button addUser;
	@FXML private Text loginerror;
	
	private ObservableList<Device> deviceData;
	private ObservableList<User> userData = FXCollections.observableArrayList();
	private ObservableList<String> logData = FXCollections.observableArrayList();
	
	@FXML private TableView<Device> deviceList = new TableView<Device>();;
	@FXML private TableView<User> userList = new TableView<User>();;
	@FXML private ListView<String> logList = new ListView<String>();;//for display log infos
	
	private  Hub model;
	private MainController aMainController;
	
	public void setmodel(Hub h) {
		model = h;
		username.textProperty().addListener((obs,oldvalue,newvalue) -> hideLoginErrorIfNoUsernameInput(newvalue));
		model.logListProperty().addListener((obs,oldvalue,newvalue) -> buildLogList(newvalue));
	}
	
	public void setmaincontroller(MainController mc) {
		aMainController = mc;
	}
	
	public Parent getView() {
		return root;
	}
	
	public void buildDeviceTable() {
		List<Device> devicelist= new ArrayList<Device>();
		for(Device d : model.getDeviceMap().values()) {
			devicelist.add(d);
		}
		deviceData = FXCollections.observableArrayList(devicelist);
		deviceList.setItems(deviceData);
	}
	
	
	public void buildUserTable() {
		List<User> userlist = new ArrayList<User>();
		for(User u : model.getUserMap().values()) {
			userlist.add(u);
		}
		userData = FXCollections.observableArrayList(userlist);
		userList.setItems(userData);
	}
	
	public void buildLogList(ObservableList<String> pData) {
		logData = pData;
		logList.setItems(logData);
	}
	
	@FXML 
	private void addThermostat(ActionEvent event) throws HubRegistrationException {
		Thermostat thermostat = new Thermostat(model);//devices are registered once initialized.
		ThermostatController tc = new ThermostatController(model,thermostat,this,aMainController);	
		buildDeviceTable();
		aMainController.BuildAdminTable();
	}
	
	@FXML 
	private void addCamera(ActionEvent event) throws HubRegistrationException {
		Camera camera = new Camera(model);
		CameraController cc = new CameraController(model,camera,this,aMainController);
		buildDeviceTable();
		aMainController.BuildAdminTable();
	}
	
	@FXML 
	private void addSmartPlug(ActionEvent event) throws HubRegistrationException {
		SmartPlug smartplug = new SmartPlug(model);
		SmartPlugController sc = new SmartPlugController(model,smartplug,this,aMainController);
		buildDeviceTable();
		aMainController.BuildAdminTable();
	}
	
	@FXML 
	private void addLightbulb(ActionEvent event) throws HubRegistrationException {
		Lightbulb lightbulb = new Lightbulb(model);
		LightbulbController lc = new LightbulbController(model,lightbulb,this,aMainController);
		buildDeviceTable();
		aMainController.BuildAdminTable();
	}
	
	@FXML 
	private void addUser(ActionEvent event) throws HubRegistrationException {
		String usrname = username.getText();
		if(username.getText().isEmpty()== true || password.getText().isEmpty()==true) {
			loginerror.setVisible(true);
			loginerror.setFill(Color.FIREBRICK);
			loginerror.setText("Username or password can not be empty");
		} else if (model.getUsersMap().containsKey(usrname)) {	// if duplicate user
			loginerror.setVisible(true);
			loginerror.setFill(Color.FIREBRICK);
			loginerror.setText("Username already exist");
		} else {
			loginerror.setVisible(false);
			String psw = password.getText();
			BasicUser basicUser = new BasicUser(model,this, usrname, psw);
			buildUserTable();
		}
	}
	
	@FXML 
	private void shutdown(ActionEvent event) {
		model.shutdown();
		Shutdown.setDisable(true);
	} 
	
	@FXML
	private void checkStatus(ActionEvent event) {
		model.checkStatus();
		checkStatus.setDisable(true);
	} 
	
	private void hideLoginErrorIfNoUsernameInput(String newV) {
		if (newV.equals("")) {
			loginerror.setText("");
		}
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeDeviceTableView();
		initializeUserTableView();
	}

	@SuppressWarnings("unchecked")
	private void initializeUserTableView() {
	    TableColumn<User, String> u1 = new TableColumn<User, String>("Username");
	    u1.setMinWidth(100);
	    u1.setCellValueFactory(  
	            new PropertyValueFactory<User, String>("Username")); 
	    TableColumn<User, String> u2 = new TableColumn<User, String>("Remove");
	    u2.setMinWidth(100);
	    u2.setCellValueFactory(  
	            new PropertyValueFactory<User, String>("RemoveButton"));
	    TableColumn<User, String> u3 = new TableColumn<User, String>("User type");
	    u3.setMinWidth(100);
	    u3.setCellValueFactory(  
	            new PropertyValueFactory<User, String>("UserType"));
	    userList.getColumns().addAll(u1,u2,u3);
	}

	@SuppressWarnings("unchecked")
	private void initializeDeviceTableView() {
	    TableColumn<Device, String> d1 = new TableColumn<Device, String>("Device UUID");
	    d1.setMinWidth(100);
	    d1.setCellValueFactory(  
	            new PropertyValueFactory<Device, String>("Identifier")); 
	    TableColumn<Device, String> d2 = new TableColumn<Device, String>("Device type");
	    d2.setMinWidth(100);
	    d2.setCellValueFactory(  
	            new PropertyValueFactory<Device, String>("DeviceName"));
	    TableColumn<Device, String> d3 = new TableColumn<Device, String>("Remove");
	    d3.setMinWidth(100);
	    d3.setCellValueFactory(  
	            new PropertyValueFactory<Device, String>("RemoveButton"));
	    TableColumn<Device, String> d4 = new TableColumn<Device, String>("Username");
	    d4.setMinWidth(100);
	    d4.setCellValueFactory(  
	            new PropertyValueFactory<Device, String>("UsernameTextfield"));
	    TableColumn<Device, String> d5 = new TableColumn<Device, String>("Assign to user");
	    d5.setMinWidth(100);
	    d5.setCellValueFactory(  
	            new PropertyValueFactory<Device, String>("AssignButton"));
	    deviceList.getColumns().addAll(d1,d2,d3,d4,d5);
	}

}

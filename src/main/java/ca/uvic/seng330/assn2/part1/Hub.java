package ca.uvic.seng330.assn2.part1;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import ca.uvic.seng330.assn2.part2.Client;
import ca.uvic.seng330.assn2.user.BasicUser;
import ca.uvic.seng330.assn2.user.User;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;


public class Hub extends Device implements Mediator {
  private String currentUser;
  private HashMap<UUID, Client> aClients = new HashMap<UUID, Client>();
  private Logger logger = LoggerFactory.getLogger(Hub.class);

  private HashMap<UUID, UUID> cameraLightbulbMap = new HashMap<UUID, UUID>();	// key: camera, value: lightbulb

  private ObservableMap<UUID, Device> aDevicesMap = FXCollections.observableHashMap();
  private MapProperty<UUID, Device> deviceMap = new SimpleMapProperty<UUID, Device>(aDevicesMap);
  
  private ObservableList<String> aLogs = FXCollections.observableArrayList();
  private ListProperty<String> logList = new SimpleListProperty<String>(aLogs);

  private ObservableMap<String, User> aUsersMap = FXCollections.observableHashMap();
  private MapProperty<String, User> userMap = new SimpleMapProperty<String, User>(aUsersMap);
  
  private HashMap<String, List<UUID>> userDeviceMap = new HashMap<String, List<UUID>>();
  
  private MainController aMainController;
  private AdminController aAdminController;
  
  private Thread checkStatusThread;
  
  
  public void setcontrollers(MainController mc, AdminController ac) {
	  aMainController = mc;
	  aAdminController = ac;
  }
  
  public void checkStatus() {
	CheckStatus checkStatusTask = new CheckStatus();
	checkStatusTask.setModel(this);
	checkStatusThread = new Thread(checkStatusTask);
	checkStatusThread.start();
  }
  
  /*
   * read data from json and log file
   */
  public void startup() throws IOException {
	readLogFile("logs.log");
	String jsonData = readFile("data.json");
	JSONObject jsonObj = new JSONObject(jsonData);
	JSONReader reader = new JSONReader(this);
	userDeviceMap = reader.readUserDeviceJSON(jsonObj);
	reader.readUserJSON(jsonObj,aAdminController);
	reader.readDeviceJSON(jsonObj,aAdminController,aMainController);
  }
  
  /*
   * safely shut down the system, save data to json and log file
   */
  public void shutdown() {
	turnOffDevices();
	writeDataToJsonFile();
	
	// notify clients that system is shutting down
	log("The system is shutting down");
//	for (Client c: aClients.values()) {
//		c.notify("The system is shutdown");
//	}
	
	// terminate check status thread if it exists
	if (checkStatusThread != null) {
		checkStatusThread.interrupt();
	}
  }

  /*
   * helper method for startup
   */
  public void readLogFile(String filename) throws IOException{
	  	BufferedReader in = new BufferedReader(new FileReader(filename));
		String str;
		List<String> llist = new ArrayList<String>();
		while((str = in.readLine()) != null){
			llist.add(str);
		}
		aLogs = FXCollections.observableArrayList(llist);
  }
  
  /* 
   * helper method for startup
   * reference:
   * https://www.thepolyglotdeveloper.com/2015/03/parse-json-file-java/
   */
  public static String readFile(String filename) {
    String result = "";
    try {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        result = sb.toString();
    } catch(Exception e) {
        e.printStackTrace();
    }
    return result;
  }
  
  /* 
   * Helper method for shutdown
   */
  private void turnOffDevices(){
	for (Device d: aDevicesMap.values()) {
		if (d instanceof Camera) {
			((Camera)d).turnoff();
		} else if (d instanceof Thermostat) {
			((Thermostat)d).turnoff();
		} else if (d instanceof SmartPlug) {
			if (((SmartPlug)d).getIsOn()) {
				((SmartPlug)d).toggle();
			} else {
				log("SmartPlug " + ((SmartPlug)d).getIdentifier() + " is already off");
			}
		} else if (d instanceof Lightbulb) {
			if (((Lightbulb)d).getIsOn()) {
				((Lightbulb)d).toggle();
			} else {
				log("Lightbulb " + ((Lightbulb)d).getIdentifier() + " is already off");
			}
		}
	}
  }
  
  /* 
   * Helper method for shutdown
   */
  private void writeDataToJsonFile(){// save all data to JSON
		JSONObject data = new JSONObject();
		JSONWriter writer = new JSONWriter();
		data.put("devices", writer.writeDeviceJSON(aDevicesMap));
		data.put("users", writer.writeUserJSON(aUsersMap));
		data.put("userDevices", writer.writeUserDeviceJSON(userDeviceMap));
		
		// write JSON to file
		try (FileWriter file = new FileWriter("data.json")) {
		    file.write(data.toString());
		    file.flush();
		} catch (IOException e) {
		    e.printStackTrace();
		}
  }
  
  @Override
  public void register(Device pDevice) throws HubRegistrationException {
    if (!aDevicesMap.containsKey(pDevice.getIdentifier())) {
      aDevicesMap.put(pDevice.getIdentifier(), pDevice);
      log("Device added " + pDevice.toString());
    } 
    else {
      throw new HubRegistrationException(pDevice + " was already registered");
    }
  }

  @Override
  public void register(Client pClient) throws HubRegistrationException {
    if (!aClients.containsKey(pClient.getIdentifier())) {
      aClients.put(pClient.getIdentifier(), pClient);
    } else {
      throw new HubRegistrationException(pClient + " was already registered");
    }
  }
  
  @Override
  public void register(User pUser) throws HubRegistrationException {
    if (!aUsersMap.containsKey(pUser.getUsername())) {
      aUsersMap.put(pUser.getUsername(), pUser);
      log("User added " + pUser.toString());
    } else {
      throw new HubRegistrationException(pUser + " was already registered");
    }
  }

  @Override
  public void unregister(Device device) throws HubRegistrationException {
    if (aDevicesMap.containsKey(device.getIdentifier())) {
    	aDevicesMap.remove(device.getIdentifier());
    	log("Device removed " + device.toString());
    }
    else {
    	log("Unknown Device unregister");
        throw new HubRegistrationException("Device does not exists!");
    }
    
  }

  @Override
  public void unregister(Client client) throws HubRegistrationException {
    if (!aClients.containsKey(client.getIdentifier())) {
      throw new HubRegistrationException("Client does not exists!");
    }
    aClients.remove(client.getIdentifier());
  }
  
  @Override
  public void unregister(User pUser) throws HubRegistrationException {
    if (aUsersMap.containsKey(pUser.getUsername())) {
    	aUsersMap.remove(pUser.getUsername());
    	log("User removed " + pUser.toString());
      
    } else {
    	log("Unknown User unregister");
    	throw new HubRegistrationException("User does not exists!");
    }
  }

  /**
   * Logging. Use SLF4J to write all message traffic to the log file.
   *
   * @param logMsg
   */
  public void log(String logMsg) {
    logger.info(logMsg);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formatDateTime = LocalDateTime.now().format(formatter);
    String logString = formatDateTime + "[ INFO ]" + logMsg;
    aLogs.add(logString);
    logList = new SimpleListProperty<String>(aLogs);
  }

  /**
   * Alerts are events that happen at the Device level. They send the alert to the hUb, which
   * redistributes to the clients
   *
   * @param pMessage
   */
  @Override
  public void alert(Device pDevice, String pMessage) {
    // initialize the map
    JSONObject jsonMessage = new JSONMessaging(pDevice, pMessage).invoke();

    if ((!isBasicUser(currentUser)) || 
    		(isBasicUser(currentUser) && (userDeviceMap.get(currentUser).contains(pDevice.getIdentifier())))) {
    	aMainController.showAlert(jsonMessage);
    	//notifyClients(jsonMessage);
    	log("ALERT msg: " + pMessage + " - from Device " + pDevice.toString());
    }
  }
  
  /* 
   * Operations for userDeviceList
   */
  public void assignDeviceToUser(String username, UUID deviceID) {
	  if (!userDeviceMap.containsKey(username) && userMap.containsKey(username)) {
		  ArrayList<UUID> idList = new ArrayList<UUID>();
		  idList.add(deviceID);
		  userDeviceMap.put(username, idList);
		  log("device " + deviceID + " is assigned to user " + username);
	  } else if (userDeviceMap.containsKey(username) && (!userDeviceMap.get(username).contains(deviceID))) {
		  // duplicate assignment is not allowed
		  userDeviceMap.get(username).add(deviceID);
		  log("device " + deviceID + " is assigned to user " + username);
	  }
  }
  
  public List<UUID> getDevicesForUser(String username) {
	  return userDeviceMap.get(username);
  }
  
  public Map<String, List<UUID>> getUserDeviceMap() {
	  return new HashMap<String, List<UUID>>(userDeviceMap);
  }
  
  
  // check if a user is basic user
  public boolean isBasicUser(String username) {
	  if (aUsersMap.get(username) instanceof BasicUser) {
		  return true;
	  } else {
		  return false;
	  }
  }
  
  /* 
   * Operations for cameraLightbulbMap
   */
  public void linkCameraLightbulb(UUID cameraID, UUID lightbulbID) {
	  cameraLightbulbMap.put(cameraID, lightbulbID);
  }
  
  public Map<UUID, UUID> getCameralightbulbMap() {
	  return new HashMap<UUID, UUID>(cameraLightbulbMap);
  }
  

  public String getCurrentUser() {
	  return currentUser;
  }
	
  public void setCurrentUser(String pUser) {
	  currentUser = pUser;
  }

  
  private void notifyClients(JSONObject pMsg) {
    for (Client c : aClients.values()) {
      c.notify(pMsg);
      log("Notified: " + c.toString());
    }
  }
  
  public void notifyClients(String pMsg) {
	for (Client c : aClients.values()) {
	  c.notify(pMsg);
	  log("Notified: " + c.toString());
	}
  }

  public Map<UUID, Client> getClients() {
    return new HashMap<UUID, Client>(aClients);
  }
  
  public Map<String, User> getUsersMap() {
    return new HashMap<String, User>(aUsersMap);
  }
  
  
  // Log list
  public final ListProperty<String> logListProperty() {

    return logList;
  }

  public final ObservableList<String> getLogList() {
    return logList.get();
  }

  public final void setLogList(ObservableList<String> list) {
    logList.set(list);
  }
  
  //Device Map
 public final MapProperty<UUID, Device> deviceMapProperty() {
	  return deviceMap;
 }

 public final ObservableMap<UUID, Device> getDeviceMap() {
	  return deviceMap.get();
 }

 public final void setDeviceMap(ObservableMap<UUID, Device> map) {
	  deviceMap.set(map);
 }
  
 // User Map
 public final MapProperty<String, User> userMapProperty() {
	  return userMap;
 }

 public final ObservableMap<String, User> getUserMap() {
	  return userMap.get();
 }

 public final void setUserMap(ObservableMap<String, User> map) {
	  userMap.set(map);
 }

}
package ca.uvic.seng330.assn2.part1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Temperature.Unit;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;
import ca.uvic.seng330.assn2.user.AdminUser;
import ca.uvic.seng330.assn2.user.BasicUser;
import ca.uvic.seng330.assn3.CameraController;
import ca.uvic.seng330.assn3.LightbulbController;
import ca.uvic.seng330.assn3.SmartPlugController;
import ca.uvic.seng330.assn3.ThermostatController;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;

public class JSONReader {
	Hub aHub;
	
	public JSONReader(Hub pHub) {
		aHub = pHub;
	}

	 public void readUserJSON(JSONObject pJsonObj,AdminController ac) {
		  JSONArray users = pJsonObj.getJSONArray("users");
		  for (int i = 0; i < users.length(); i++) {
		      JSONObject userJson = users.getJSONObject(i);
		      String username = userJson.getString("username");
		      String password = userJson.getString("password");
		      String userType = userJson.getString("userType");
		      
		      if (userType.equals("BasicUser") && !username.equals("10000")) {
		    	  BasicUser basicUser = new BasicUser(aHub,ac, username, password);
		      } else if (userType.equals("AdminUser") && !username.equals("10086")) {
		    	  AdminUser adminUser = new AdminUser(aHub,ac, username, password);
		      }
		  }
	  }
	  
	  public void readDeviceJSON(JSONObject pJsonObj,AdminController ac,MainController mc) {
		  JSONArray devices = pJsonObj.getJSONArray("devices");
		  for (int i = 0; i < devices.length(); i++) {
			  JSONObject deviceJson = devices.getJSONObject(i);
			  String deviceType = deviceJson.getString("deviceType");
		      UUID deviceID = UUID.fromString(deviceJson.getString("UUID"));
		      
		      if (deviceType.equals("Camera")) {
		    	  Camera c = new Camera(aHub, deviceID);
		    	  CameraController cc = new CameraController(aHub,c,ac,mc);
		      } else if (deviceType.equals("Thermostat")) {
		    	  Thermostat t = new Thermostat(aHub, deviceID);
		    	  ThermostatController tc = new ThermostatController(aHub,t,ac,mc);
		    	  t.setTemperature(Double.parseDouble(deviceJson.getString("Thermostattemp")));
		    	  t.setUnit(Unit.valueOf(deviceJson.getString("Thermostatunit")));
		      } else if (deviceType.equals("Lightbulb")) {
		    	  Lightbulb l = new Lightbulb(aHub, deviceID);
		    	  LightbulbController lc = new LightbulbController(aHub,l,ac,mc);
		      } else if (deviceType.equals("SmartPlug")) {
		    	  SmartPlug s = new SmartPlug(aHub, deviceID);
		    	  SmartPlugController sc = new SmartPlugController(aHub,s,ac,mc);
		      } 
		  }
	  }
	  
	  public HashMap<String, List<UUID>> readUserDeviceJSON(JSONObject pJsonObj) {
		  HashMap<String, List<UUID>> userDeviceMap = new HashMap<String, List<UUID>>();
		  JSONArray userDevices = pJsonObj.getJSONArray("userDevices");
		  for (int i = 0; i < userDevices.length(); i++) {
			  JSONObject userDeviceJson = userDevices.getJSONObject(i);
			  String username = userDeviceJson.getString("username");
			  JSONArray uuidList = userDeviceJson.getJSONArray("uuid");
			  List<UUID> resultUuids = new ArrayList<UUID>();
			  for (int j = 0; j < uuidList.length(); j++) {
				  resultUuids.add(UUID.fromString(uuidList.getString(j)));
			  }
			  userDeviceMap.put(username, resultUuids);
		  }
		  return userDeviceMap;
	  }
}

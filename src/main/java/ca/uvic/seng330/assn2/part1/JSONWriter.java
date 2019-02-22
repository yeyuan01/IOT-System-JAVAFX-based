package ca.uvic.seng330.assn2.part1;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;
import ca.uvic.seng330.assn2.user.AdminUser;
import ca.uvic.seng330.assn2.user.BasicUser;
import ca.uvic.seng330.assn2.user.User;
import javafx.collections.ObservableMap;


public class JSONWriter {
	public JSONWriter() {

	}
	
	public JSONArray writeDeviceJSON(ObservableMap<UUID, Device> pDevicesMap) {
	  JSONArray deviceListJSON = new JSONArray();
	  for (Device d: pDevicesMap.values()) {
		  JSONObject device = new JSONObject();
		  UUID id = d.getIdentifier();
		  device.put("UUID", id.toString());
		  
		  String deviceType = "";
		  if (d instanceof Camera) {
			  deviceType = "Camera";
		  } else if (d instanceof Thermostat) {
			  deviceType = "Thermostat";
			  device.put("Thermostattemp", String.valueOf(((Thermostat) d).getTemp().getTemperature()));
			  device.put("Thermostatunit", String.valueOf(((Thermostat) d).getTemp().getUnit()));
		  } else if (d instanceof Lightbulb) {
			  deviceType = "Lightbulb";
		  } else if (d instanceof SmartPlug) {
			  deviceType = "SmartPlug";
		  }
		  device.put("deviceType", deviceType);
		  deviceListJSON.put(device);
 	  }
	  
	  //System.out.println(deviceListJSON.toString());
	  return deviceListJSON;
	}
  
	public JSONArray writeUserJSON(ObservableMap<String, User> pUsersMap) {
	  JSONArray userListJSON = new JSONArray();
	  for (User u: pUsersMap.values()) {
		  JSONObject user = new JSONObject();
		  user.put("username", u.getUsername());
		  user.put("password", u.getPassword());
		  if (u instanceof AdminUser) {
			  user.put("userType", "AdminUser");
		  } else if (u instanceof BasicUser) {
			  user.put("userType", "BasicUser");
		  }
		  userListJSON.put(user);
 	  }
	  return userListJSON;
	}

	public JSONArray writeUserDeviceJSON(Map<String, List<UUID>> pUserDeviceMap) {
	  JSONArray userDeviceListJSON = new JSONArray();
	  for (String username: pUserDeviceMap.keySet()) {
		  JSONObject userDevice = new JSONObject();
		  userDevice.put("username", username);
		  JSONArray uuidList = new JSONArray(pUserDeviceMap.get(username));
		  userDevice.put("uuid", uuidList);
		  
		  userDeviceListJSON.put(userDevice);
	  }
	  return userDeviceListJSON;
	}
}

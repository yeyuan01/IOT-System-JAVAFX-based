package ca.uvic.seng330.assn2.part1;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;
import ca.uvic.seng330.assn2.user.AdminUser;
import ca.uvic.seng330.assn2.user.BasicUser;
import ca.uvic.seng330.assn2.user.User;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;


public class JSONWriterTest {
JFXPanel jfxPanel = new JFXPanel();
	
	private Hub hub;
	private AdminController adminController;
	private MainController mainController;
	private AdminUser adminUser;
	private BasicUser basicUser;
	private Camera camera;
	private Thermostat thermostat;
	private JSONObject data;
	private JSONWriter writer;
	
	@Before
	public void setUp () throws IOException {
		hub = new Hub();
		
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("MainView.fxml"));
		Parent mainpane = loader2.load();
		Scene mainscene = new Scene(mainpane, 1100, 900);
		FXMLLoader loader3 = new FXMLLoader(getClass().getResource("AdminView.fxml"));
		Parent adminpane = loader3.load();
		Scene adminscene = new Scene(adminpane, 900, 700);
		
		mainController = (MainController) loader2.getController();
		mainController.setmodel(hub);
		adminController = (AdminController) loader3.getController();
		adminController.setmodel(hub);
 		adminController.setmaincontroller(mainController);
 		hub.setcontrollers(mainController, adminController);
 		
		adminUser = new AdminUser(hub, adminController, "admin", "password1");
	    basicUser = new BasicUser(hub, adminController, "basic", "password2");
	    
	    camera = new Camera(hub);
	    thermostat = new Thermostat(hub);
	    
	    // You can only assign devices to basic users
	    hub.assignDeviceToUser(basicUser.getUsername(), camera.getIdentifier());
	    
	    data = new JSONObject();
		writer = new JSONWriter();
		
	}
	
	@Test
	public void testWriteUserJSON() {
		data.put("users", writer.writeUserJSON(hub.getUserMap()));
		JSONObject user = data.getJSONArray("users").getJSONObject(0);
		if (user.getString("userType").equals("AdminUser")) {
			assertTrue(user.getString("username").equals("admin"));
			assertTrue(user.getString("password").equals("password1"));
			
		} else {
			assertTrue(user.getString("username").equals("basic"));
			assertTrue(user.getString("password").equals("password2"));
		}
	}
	
	@Test
	public void testWriteDeviceJSON() {
	    data.put("devices", writer.writeDeviceJSON(hub.getDeviceMap()));
	    JSONObject device = data.getJSONArray("devices").getJSONObject(0);
	    if (device.getString("deviceType").equals("Camera")) {
	    	assertTrue(device.getString("UUID").equals(camera.getIdentifier().toString()));
	    } else {
	    	assertTrue(device.getString("UUID").equals(thermostat.getIdentifier().toString()));
	    }
	}
	
	@Test
	public void testWriteUserDeviceJSON() {
		data.put("userDevices", writer.writeUserDeviceJSON(hub.getUserDeviceMap()));
		JSONObject userDevice = data.getJSONArray("userDevices").getJSONObject(0);
		System.out.println(userDevice.toString());
		assertTrue(userDevice.getJSONArray("uuid").getString(0).equals(camera.getIdentifier().toString()));
		assertTrue(userDevice.getString("username").equals("basic"));
	}
}

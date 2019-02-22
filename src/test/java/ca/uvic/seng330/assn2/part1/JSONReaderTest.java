package ca.uvic.seng330.assn2.part1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.lang.reflect.*;


public class JSONReaderTest {
JFXPanel jfxPanel = new JFXPanel();
	
	private Hub hub;
	private AdminController adminController;
	private MainController mainController;
	private AdminUser adminUser;
	private BasicUser basicUser;
	private Camera camera;
	private Thermostat thermostat;
	private JSONReader reader;
	private JSONObject jsonObj;
	
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

		String jsonData = hub.readFile("test.json");
		jsonObj = new JSONObject(jsonData);
		reader = new JSONReader(hub);
	}
	
	@Test
	public void testReadUserJSON() {
		reader.readUserJSON(jsonObj,adminController);
		assertTrue(hub.getUserMap().size() == 2);
		assertFalse(hub.isBasicUser("admin"));
		assertTrue(hub.isBasicUser("basic"));
		assertTrue(hub.getUserMap().get("admin").getPassword().equals("psw1"));
		assertTrue(hub.getUserMap().get("basic").getPassword().equals("psw2"));
	}
	
	@Test
	public void testReadDeviceJSON() {
		reader.readDeviceJSON(jsonObj,adminController,mainController);
		assertTrue(hub.getDeviceMap().size() == 4);
		JSONObject device = jsonObj.getJSONArray("devices").getJSONObject(0);
		String deviceID = device.getString("UUID");
		assertTrue(hub.getDeviceMap().containsKey(UUID.fromString(deviceID)));
	}
	
	@Test
	public void testReadUserDeviceJSON() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field userDeviceMapField = Hub.class.getDeclaredField("userDeviceMap");
		userDeviceMapField.setAccessible(true);
		userDeviceMapField.set(hub, reader.readUserDeviceJSON(jsonObj));
		assertTrue(hub.getUserDeviceMap().size() == 1);
		assertTrue(hub.getUserDeviceMap().containsKey("basic"));
		List<UUID> uuidList = hub.getUserDeviceMap().get("basic");
		assertTrue(uuidList.size() == 1);
		assertTrue(uuidList.contains(UUID.fromString("61a022d2-ebe3-4e58-b10a-1aa2bafd4995")));
	}
	
}

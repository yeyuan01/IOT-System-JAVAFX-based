package ca.uvic.seng330.assn2.part1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.json.JSONObject;

import static org.hamcrest.CoreMatchers.is;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;
import ca.uvic.seng330.assn2.user.AdminUser;
import ca.uvic.seng330.assn2.user.BasicUser;
import ca.uvic.seng330.assn2.user.User;
import ca.uvic.seng330.assn3.CameraController;
import ca.uvic.seng330.assn3.LightbulbController;
import ca.uvic.seng330.assn3.SmartPlugController;
import ca.uvic.seng330.assn3.ThermostatController;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HubTest {
	JFXPanel jfxPanel = new JFXPanel();
	private Hub hub;
	private AdminController adminController;
	private MainController mainController;
	private AdminUser adminUser;
	private BasicUser basicUser;
	private Camera camera;
	private Thermostat thermostat;
	private Lightbulb lightbulb;
	private SmartPlug smartPlug;
	private JSONObject jsonObj;
	private JSONReader reader;
	
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
	    lightbulb = new Lightbulb(hub);
	    smartPlug = new SmartPlug(hub);
	    
	    hub.assignDeviceToUser("basic", camera.getIdentifier());

	}
	
	@After
	public void tearDown () throws Exception {
		// delete data.json for clean test setup
		File file = new File("data.json"); 
        if(file.exists()) { 
        	file.delete();
        }
	}
	
	@Test
	public void testShutDown()
	{
		hub.shutdown();
		
		// test devices are turned off
		for (Device d: hub.getDeviceMap().values()) {
			if (d instanceof Camera) {
				assertFalse(((Camera)d).getIsOn());
			} else if (d instanceof Thermostat) {
				assertFalse(((Thermostat)d).getIsOn());
			} else if (d instanceof Lightbulb) {
				assertFalse(((Lightbulb)d).getIsOn());
			} else if (d instanceof SmartPlug) {
				assertFalse(((SmartPlug)d).getIsOn());
			}
		}
		
		// test json file exists
		File file = new File("data.json"); 
		assertTrue(file.exists());
		
		// JSON content test is already included in JSONWriterTest
	}
	
	/*
	 * test read logs.log file
	 * see other related tests in JSONReaderTest
	 */
	@Test
	public void testStartUp() throws HubRegistrationException, IOException {
		File file = new File("logs.log");
		if (file.exists()) {
			hub.readLogFile("logs.log");
			assertFalse(hub.getLogList().isEmpty());
		}
	}
	
}

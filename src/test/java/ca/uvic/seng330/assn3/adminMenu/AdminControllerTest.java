package ca.uvic.seng330.assn3.adminMenu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.control.ListViewMatchers.hasItems;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.junit.After;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

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
import ca.uvic.seng330.assn3.loginMenu.LoginController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import ca.uvic.seng330.example.ClickApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminControllerTest extends ApplicationTest {
	JFXPanel jfxPanel = new JFXPanel();
	Hub aHub;
	private MainController main;
	private AdminController adminc;
	private LoginController loginc;
	private Stage s;
	private Scene adminscene;
	private Scene mainscene;
	private Scene loginscene;
	@Override 
	public void start(Stage stage) throws IOException {
		aHub = new Hub();
		s = stage;
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("MainView.fxml"));
		Parent mainpane = loader2.load();
		mainscene = new Scene(mainpane, 1100, 900);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
		Parent loginpane = loader.load();
		loginscene = new Scene(loginpane, 600, 400);
		FXMLLoader loader3 = new FXMLLoader(getClass().getResource("AdminView.fxml"));
		Parent adminpane = loader3.load();
		adminscene = new Scene(adminpane, 900, 700);
		main = (MainController) loader2.getController();
		main.setmodel(aHub);
		adminc = (AdminController) loader3.getController();
		adminc.setmodel(aHub);
		adminc.setmaincontroller(main);
		aHub.setcontrollers(main, adminc);
		loginc = (LoginController) loader.getController();
		loginc.setAdminController(adminc);
		loginc.setMainController(main);
		loginc.setnextscenes(mainscene, adminscene);
		s.setScene(adminscene);
		s.show();
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
	public void testShutdownSystem() {
		clickOn("#Shutdown");
		verifyThat("#Shutdown", hasText("Shut Down"));
		File file = new File("data.json");
		assert(file.exists());
	}
	
	@Test 
	public void testAddCamera() {
		clickOn("#addCamera");
		verifyThat("#deviceList",hasTableCell("Camera"));
		Device device = aHub.getDeviceMap().values().iterator().next();
		assertTrue(device instanceof Camera);
		Camera camera = (Camera) device;
		String id = camera.getIdentifier().toString();
		verifyThat("#deviceList",hasTableCell(id));
	}
	
	@Test 
	public void testRemoveCamera() {
		clickOn("#addCamera");
		verifyThat("#deviceList",hasNumRows(1));
		assertEquals(1, aHub.getDeviceMap().size());
		clickOn("#CameraRemoveButton");
		verifyThat("#deviceList",hasNumRows(0));
		assertEquals(0, aHub.getDeviceMap().size());
	}
	
	@Test 
	public void testAddThermostat() {
		clickOn("#addThermostat");
		verifyThat("#deviceList",hasTableCell("Thermostat"));
		Device device = aHub.getDeviceMap().values().iterator().next();
		assertTrue(device instanceof Thermostat);
		Thermostat thermostat = (Thermostat) device;
		String id = thermostat.getIdentifier().toString();
		verifyThat("#deviceList",hasTableCell(id));
	}
	
	@Test 
	public void testRemoveThermostat() {
		clickOn("#addThermostat");
		verifyThat("#deviceList",hasNumRows(1));
		assertEquals(1, aHub.getDeviceMap().size());
		clickOn("#ThermostatRemoveButton");
		verifyThat("#deviceList",hasNumRows(0));
		assertEquals(0, aHub.getDeviceMap().size());
	}
	
	@Test 
	public void testAddLightbulb() {
		clickOn("#addLightbulb");
		verifyThat("#deviceList",hasTableCell("Lightbulb"));
		Device device = aHub.getDeviceMap().values().iterator().next();
		assertTrue(device instanceof Lightbulb);
		Lightbulb lightbulb = (Lightbulb) device;
		String id = lightbulb.getIdentifier().toString();
		verifyThat("#deviceList",hasTableCell(id));
	}
	
	@Test 
	public void testRemoveLightbulb() {
		clickOn("#addLightbulb");
		verifyThat("#deviceList",hasNumRows(1));
		assertEquals(1, aHub.getDeviceMap().size());
		clickOn("#LightbulbRemoveButton");
		verifyThat("#deviceList",hasNumRows(0));
		assertEquals(0, aHub.getDeviceMap().size());
	}
	
	@Test 
	public void testAddSmartPlug() {
		clickOn("#addSmartPlug");
		verifyThat("#deviceList",hasTableCell("SmartPlug"));
		Device device = aHub.getDeviceMap().values().iterator().next();
		assertTrue(device instanceof SmartPlug);
		SmartPlug smartPlug = (SmartPlug) device;
		String id = smartPlug.getIdentifier().toString();
		verifyThat("#deviceList",hasTableCell(id));
	}
	
	@Test 
	public void testRemoveSmartPlug() {
		clickOn("#addSmartPlug");
		verifyThat("#deviceList",hasNumRows(1));
		assertEquals(1, aHub.getDeviceMap().size());
		clickOn("#SmartPlugRemoveButton");
		verifyThat("#deviceList",hasNumRows(0));
		assertEquals(0, aHub.getDeviceMap().size());
	}
	
	
	@Test 
	public void testLogExists() {
		verifyThat("#logList",hasItems(0));
		assertTrue(aHub.getLogList().size() == 0);
		clickOn("#addCamera");
		verifyThat("#logList",hasItems(1));
		assertFalse(aHub.getLogList().size() == 0);
	}
	
	@Test 
	public void testAddUser() {
		verifyThat("#userList",hasNumRows(0));
		assertTrue(aHub.getUserMap().size() == 0);
		clickOn("#username").write("1");
		clickOn("#password").write("1");
		clickOn("#addUser");
		verifyThat("#userList",hasTableCell("1"));
		assertTrue(aHub.getUserMap().size() == 1);
	}
	
	@Test 
	public void testRemoveUser() {
		verifyThat("#userList",hasNumRows(0));
		assertTrue(aHub.getUserMap().size() == 0);
		clickOn("#username").write("1");
		clickOn("#password").write("1");
		clickOn("#addUser");
		verifyThat("#userList",hasTableCell("1"));
		assertTrue(aHub.getUserMap().size() == 1);
		clickOn("#UserRemoveButton");
		verifyThat("#userList",hasNumRows(0));
		assertTrue(aHub.getUserMap().size() == 0);
	}
	@Test 
	public void testAssignDeviceToUser() {
		clickOn("#username").write("1");
		clickOn("#password").write("1");
		clickOn("#addUser");
		clickOn("#addCamera");
		clickOn("#UsernameTextfield").write("1");
		clickOn("#DeviceAssignButton");

		assertTrue(aHub.getDeviceMap().containsKey(aHub.getDevicesForUser("1").get(0)));
		
	}
	
}

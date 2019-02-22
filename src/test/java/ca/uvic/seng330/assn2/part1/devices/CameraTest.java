package ca.uvic.seng330.assn2.part1.devices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import ca.uvic.seng330.assn2.part1.Status;
import ca.uvic.seng330.assn2.part1.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;


public class CameraTest {
	JFXPanel jfxPanel = new JFXPanel();
	
	private Hub hub;
	private AdminController adminController;
	private MainController mainController;
	private Camera camera;
	
	
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
 		
 		camera = new Camera(hub);

	}
	
	@Test
	public void testCameraFull() {
		try {
			camera.setFull();
			camera.record();
			fail();
		} catch (CameraFullException e) {
			// UI test: verify alert message
			// cannot simply use hub.getLogList().contains(...) since log has other components (date, log level)
			int count = 0;
			String target = "ALERT msg: Camera is full - from Device Camera id " + camera.getIdentifier();
			for (String log: hub.getLogList()) {
				if (log.contains(target)) 	count++;
			}
			assertTrue(count == 1);
		}
	}
	
	@Test
	public void testObjectDetected() {
		camera.objectDetected();
		assertTrue(camera.getObjectDetected() == true);
		int count = 0;
		String target = "ALERT msg: Camera detected an object - from Device Camera id " + camera.getIdentifier();
		for (String log: hub.getLogList()) {
			if (log.contains(target)) 	count++;
		}
		assertTrue(count == 1);
	}
  

}


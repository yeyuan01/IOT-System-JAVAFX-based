package ca.uvic.seng330.assn2.part1.devices;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.io.IOException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.testfx.framework.junit.ApplicationTest;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LightbulbTest {
	JFXPanel jfxPanel = new JFXPanel();
	
	private Hub hub;
	private AdminController adminController;
	private MainController mainController;
	private Lightbulb lightbulb;
	private Camera camera;
	
	
	@Before
	public void setUp() throws IOException {
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
		
 		lightbulb = new Lightbulb(hub);
 		camera = new Camera(hub);
 		hub.linkCameraLightbulb(camera.getIdentifier(), lightbulb.getIdentifier());

	}
	
	@Test
	public void testSomeoneEntersRoom() {
		camera.someoneEntersRoom();
		if ((camera.getSomeoneInRoom() == true) && hub.getCameralightbulbMap().get(camera.getIdentifier()).equals(lightbulb.getIdentifier()))  {
			if (lightbulb.getIsOn() == false) {
				lightbulb.toggle();
			} else {
			    hub.alert(lightbulb, "lightbulb is now true");
			}
		}
		assertEquals(true, lightbulb.getIsOn());
	}
	
	@Test
	public void testNoOneInRoom() {
		camera.noOneInRoom();
		if ((camera.getSomeoneInRoom() == false) && hub.getCameralightbulbMap().get(camera.getIdentifier()).equals(lightbulb.getIdentifier()))  {
			if (lightbulb.getIsOn() == true) {
				lightbulb.toggle();
			} else {
			    hub.alert(lightbulb, "lightbulb is now false");
			}
		}
		assertEquals(false, lightbulb.getIsOn());
	}
  
}

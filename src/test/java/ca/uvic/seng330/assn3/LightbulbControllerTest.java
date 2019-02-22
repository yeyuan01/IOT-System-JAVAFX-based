package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextMatchers.hasText;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LightbulbControllerTest extends ApplicationTest {
	JFXPanel jfxPanel = new JFXPanel();
	
	private Hub hub;
	private AdminController adminController;
	private MainController mainController;
	private Lightbulb lightbulb;
	
	@Override 
	public void start(Stage stage) throws IOException {
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
 		LightbulbController cc = new LightbulbController(hub,lightbulb,adminController,mainController);
 		mainController.BuildAdminTable();
		stage.setScene(mainscene);
		stage.show();
	}
	
	@Test
	public void testToggleLightbulb() {
		verifyThat("#LightbulbOnoff", hasText("Off"));
		assertFalse(lightbulb.getIsOn());
		clickOn("#LightbulbToggleButton");
		verifyThat("#LightbulbOnoff", hasText("On"));
		assertTrue(lightbulb.getIsOn());
		clickOn("#LightbulbToggleButton");
		verifyThat("#LightbulbOnoff", hasText("Off"));
		assertFalse(lightbulb.getIsOn());
		
	}
	
}

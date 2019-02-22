package ca.uvic.seng330.assn2.part1.devices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.api.FxAssert.verifyThat;
import java.io.IOException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import ca.uvic.seng330.assn2.part1.Status;
import ca.uvic.seng330.assn2.part1.devices.Temperature.Unit;
import ca.uvic.seng330.assn2.user.AdminUser;
import ca.uvic.seng330.assn2.user.BasicUser;
import ca.uvic.seng330.assn3.ThermostatController;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ThermostatTest extends ApplicationTest{
	JFXPanel jfxPanel = new JFXPanel();
	
	private Hub hub;
	private AdminController adminController;
	private MainController mainController;
	private Thermostat thermostat;
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
 		
 		thermostat = new Thermostat(hub);
 		ThermostatController tc = new ThermostatController(hub, thermostat, adminController, mainController);
 		mainController.BuildAdminTable();
 		stage.setScene(mainscene);
 		stage.show();
	}

	@Test
	public void testSetModelTemperature() {
		assertEquals(72, thermostat.getTemp().getTemperature(), 0.1);
		assertEquals(Unit.FAHRENHEIT, thermostat.getTemp().getUnit());
		clickOn("#ThermostatToggleButton");
		verifyThat("#ThermostatTemptext",TextInputControlMatchers.hasText("72.0"));
		verifyThat("#ThermostatUnit",LabeledMatchers.hasText("FAHRENHEIT"));
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
			    thermostat.setTemperature(20);
			    thermostat.setUnit(Unit.CELSIUS);
			    assertEquals(20, thermostat.getTemp().getTemperature(), 0.1);
				assertEquals(Unit.CELSIUS, thermostat.getTemp().getUnit());
				verifyThat("#ThermostatTemptext",TextInputControlMatchers.hasText("20.0"));
				verifyThat("#ThermostatUnit",LabeledMatchers.hasText("CELSIUS"));
		    }
		});
	}
	
}
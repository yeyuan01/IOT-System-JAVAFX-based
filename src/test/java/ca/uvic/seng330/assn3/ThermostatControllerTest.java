package ca.uvic.seng330.assn3;

import java.util.UUID;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Temperature;
import ca.uvic.seng330.assn2.part1.devices.Temperature.TemperatureOutofBoundsException;
import ca.uvic.seng330.assn2.part1.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;


public class ThermostatControllerTest extends ApplicationTest {
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
 		ThermostatController tController = new ThermostatController(hub, thermostat, adminController, mainController);
 		mainController.BuildAdminTable();
		stage.setScene(mainscene);
		stage.show();
	}
	
	@Test
	public void testStartThermostat() {
		// before start thermostat
		verifyThat("#ThermostatToggleButton", LabeledMatchers.hasText("Start"));
		verifyThat("#ThermostatTemptext", isInvisible());
		verifyThat("#ThermostatUnit", isInvisible());
		assertFalse(thermostat.getIsOn());
		
		// start thermostat
		clickOn("#ThermostatToggleButton");
		verifyThat("#ThermostatToggleButton", LabeledMatchers.hasText("Turn Off"));
		verifyThat("#ThermostatTemptext", TextInputControlMatchers.hasText("72.0"));
		verifyThat("#ThermostatUnit", LabeledMatchers.hasText("FAHRENHEIT"));
		assertTrue(thermostat.getIsOn());
	}
	
	@Test
	public void testTurnoffThermostat() {
		clickOn("#ThermostatToggleButton");
		clickOn("#ThermostatToggleButton");
		verifyThat("#ThermostatToggleButton", LabeledMatchers.hasText("Start"));
		verifyThat("#ThermostatTemptext", isInvisible());
		verifyThat("#ThermostatUnit", isInvisible());
		assertFalse(thermostat.getIsOn());
	}
	
	@Test
	public void testChangeUnitToCelsius() {
		clickOn("#ThermostatToggleButton");
		verifyThat("#ThermostatTemptext", TextInputControlMatchers.hasText("72.0"));
		verifyThat("#ThermostatUnit", LabeledMatchers.hasText("FAHRENHEIT"));

		clickOn("#ThermostatUnit");
		String temperature = thermostat.getTemptext().getText();
		assertThat(temperature, containsString("22.22222222222222"));
		verifyThat("#ThermostatUnit", LabeledMatchers.hasText("CELSIUS"));
		// test model
		String tempModel = Double.toString(thermostat.getTemp().getTemperature());
		String unitModel = thermostat.getTemp().getUnit().toString();
		String tempView = thermostat.getTemptext().getText();
		String unitView = thermostat.getUnit().getText();
		assertEquals(tempModel, tempView);
		assertEquals(unitModel, unitView);
	}
	
	@Test
	public void testChangeUnitToFahrenheit() {
		clickOn("#ThermostatToggleButton");
		clickOn("#ThermostatUnit");
		clickOn("#ThermostatUnit");
		verifyThat("#ThermostatTemptext", TextInputControlMatchers.hasText("72.0"));
		verifyThat("#ThermostatUnit", LabeledMatchers.hasText("FAHRENHEIT"));
		// test model
		String tempModel = Double.toString(thermostat.getTemp().getTemperature());
		String unitModel = thermostat.getTemp().getUnit().toString();
		String tempView = thermostat.getTemptext().getText();
		String unitView = thermostat.getUnit().getText();
		assertEquals(tempModel, tempView);
		assertEquals(unitModel, unitView);
	}
	
	@Test
	public void testSetTempInTextbox() {
		clickOn("#ThermostatToggleButton");
		
		clickOn("#ThermostatTemptext").write("3");
		String tempModel = Double.toString(thermostat.getTemp().getTemperature());
		String tempView = thermostat.getTemptext().getText();
		assertEquals(tempModel, tempView);
	}

	
	
}

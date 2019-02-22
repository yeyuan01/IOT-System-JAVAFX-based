package ca.uvic.seng330.assn3.mainMenu;



import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import static org.testfx.matcher.control.ListViewMatchers.hasItems;
import static org.testfx.matcher.control.ListViewMatchers.hasListCell;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.CameraFullException;
import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Temperature;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;
import ca.uvic.seng330.assn2.user.AdminUser;
import ca.uvic.seng330.assn2.part1.devices.Temperature.TemperatureOutofBoundsException;
import ca.uvic.seng330.assn2.part1.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.CameraController;
import ca.uvic.seng330.assn3.LightbulbController;
import ca.uvic.seng330.assn3.SmartPlugController;
import ca.uvic.seng330.assn3.ThermostatController;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import javafx.beans.binding.MapExpression;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;  

public class MainControllerTest extends ApplicationTest {
	private Hub aHub;
	private Camera a;
	@Override public void start(Stage stage) throws IOException {
		aHub = new Hub();
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("MainView.fxml"));
		Parent mainpane = loader2.load();
		Scene mainscene = new Scene(mainpane, 1100, 900);
		FXMLLoader loader3 = new FXMLLoader(getClass().getResource("AdminView.fxml"));
		Parent adminpane = loader3.load();
		Scene adminscene = new Scene(adminpane, 900, 700);
		MainController main = (MainController) loader2.getController();
		main.setmodel(aHub);
		AdminController adminc = (AdminController) loader3.getController();
		adminc.setmodel(aHub);
		adminc.setmaincontroller(main);
		aHub.setcontrollers(main, adminc);
		a = new Camera(aHub);
		CameraController ac = new CameraController(aHub,a,adminc,main);
		AdminUser aUser = new AdminUser(aHub,adminc,"10086","123456");
		main.BuildAdminTable();
		stage.setScene(mainscene);
		stage.show();
	}
	
	@Test
	public void testAlert() throws InterruptedException {
		verifyThat("#alertlist",hasItems(0));
		clickOn("#CameraToggleButton");
		verifyThat("#alertlist",hasItems(1));
		// check log list contains this alert
		int count = 0;
		String target = "ALERT msg: Camera is started - from Device Camera id " + a.getIdentifier();
		for (String log: aHub.getLogList()) {
			if (log.contains(target)) 	count++;
		}
		assertTrue(count == 1);
	}
}

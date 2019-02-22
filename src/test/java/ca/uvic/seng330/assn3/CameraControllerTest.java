package ca.uvic.seng330.assn3;

import java.io.IOException;
import java.util.UUID;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;
import static org.testfx.api.FxAssert.verifyThat;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.CameraFullException;
import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CameraControllerTest extends ApplicationTest {
	private Camera camera;
	
	@Override 
	public void start(Stage stage) throws IOException {
		Hub aHub = new Hub();
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

		camera = new Camera(aHub);
		CameraController cc = new CameraController(aHub,camera,adminc,main);
		main.BuildAdminTable();
		stage.setScene(mainscene);
		stage.show();

	  }
	
	@Test
	public void testStartCamera() {
		// before clicking start button
		verifyThat("#CameraToggleButton",hasText("Start"));
		verifyThat("#CameraRecordButton", isInvisible());
		verifyThat("#CameraVideo", isInvisible());
		assertFalse(camera.getIsOn());
		
		// start camera
		clickOn("#CameraToggleButton");
		verifyThat("#CameraToggleButton",hasText("Turn Off"));
		verifyThat("#CameraRecordButton",hasText("Record"));
		verifyThat("#CameraVideo", isVisible());
		assertTrue(camera.getIsOn());		
	}
	
	@Test
	public void testRecord() {
		clickOn("#CameraToggleButton");
		assertFalse(camera.isRecording());
		clickOn("#CameraRecordButton");
		verifyThat("#CameraRecordButton",hasText("Stop Recording"));
		assertTrue(camera.isRecording());
		clickOn("#CameraRecordButton");
	}
	
	@Test
	public void testStopRecord() {
		clickOn("#CameraToggleButton");
		assertFalse(camera.isRecording());
		clickOn("#CameraRecordButton");
		assertTrue(camera.isRecording());
		clickOn("#CameraRecordButton");
		assertFalse(camera.isRecording());
		verifyThat("#CameraRecordButton",hasText("Record"));
	}
	
	@Test
	public void testTurnoffCamera() {
		// start camera and record, and then turn it off
		clickOn("#CameraToggleButton");
		assertTrue(camera.getIsOn());
		clickOn("#CameraRecordButton");
		clickOn("#CameraToggleButton");
		verifyThat("#CameraToggleButton",hasText("Start"));
		verifyThat("#CameraRecordButton", isInvisible());
		verifyThat("#CameraVideo", isInvisible());
		assertFalse(camera.getIsOn());
		assertFalse(camera.isRecording());
		
		// start camera again, camera should not be recording
		clickOn("#CameraToggleButton");
		assertFalse(camera.isRecording());
		verifyThat("#CameraRecordButton",hasText("Record"));
	}
}

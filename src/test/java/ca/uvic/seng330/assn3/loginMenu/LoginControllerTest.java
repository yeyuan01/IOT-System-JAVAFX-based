package ca.uvic.seng330.assn3.loginMenu;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextMatchers.hasText;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.user.AdminUser;
import ca.uvic.seng330.assn2.user.BasicUser;
import ca.uvic.seng330.assn2.user.User;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginControllerTest extends ApplicationTest {
	JFXPanel jfxPanel = new JFXPanel();
	
	private Hub aHub;
	private AdminUser adminUser;
	private BasicUser basicUser;
	private AdminController adminc;
	private MainController main;
	private LoginController login;
	
	@Override 
	public void start(Stage stage) throws IOException {
		aHub = new Hub();
	
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("MainView.fxml"));
		Parent mainpane = loader2.load();
		Scene mainscene = new Scene(mainpane, 1100, 900);
		FXMLLoader loader3 = new FXMLLoader(getClass().getResource("AdminView.fxml"));
		Parent adminpane = loader3.load();
		Scene adminscene = new Scene(adminpane, 900, 700);
		main = (MainController) loader2.getController();
		main.setmodel(aHub);
		adminc = (AdminController) loader3.getController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
		Parent loginpane = loader.load();
		Scene loginscene = new Scene(loginpane, 600, 400);
		login =(LoginController) loader.getController();
		login.setmodel(aHub);
		login.setnextscenes(adminscene,mainscene);
		login.setAdminController(adminc);
		login.setMainController(main);
		adminc.setmodel(aHub);
		adminc.setmaincontroller(main);
		aHub.setcontrollers(main, adminc);
		
		if(!aHub.getUserMap().containsKey("10086") || !aHub.getUserMap().containsKey("10000") ) {
		  	adminUser = new AdminUser(aHub,adminc,"10086","123456");
			basicUser = new BasicUser(aHub,adminc,"10000","654321");
		}
	    
		stage.setScene(loginscene);
		stage.show();

	}

	@Test 
	public void testLoginAdminUser() {
		clickOn("#account").write("10086");
		clickOn("#password").write("123456");
		clickOn("#signin");
		assertTrue(aHub.getCurrentUser().equals("10086"));
	}
	
	@Test 
	public void testLoginBasicUser() {
		clickOn("#account").write("10000");
		clickOn("#password").write("654321");
		clickOn("#signin");
		assertTrue(aHub.getCurrentUser().equals("10000"));
	}
	
	@Test 
	public void testWrongPassword() {
		clickOn("#account").write("10000");
		clickOn("#password").write("6");
		clickOn("#signin");
		verifyThat("#errorMessage", hasText("Login error, retry"));
	}
	
	
}
package ca.uvic.seng330.assn2.user;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class UserTest {
	JFXPanel jfxPanel = new JFXPanel();
	
	private Hub hub;
	private AdminController adminController;
	private MainController mainController;
	private AdminUser adminUser;
	private BasicUser basicUser;
	
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
	}
	
	@Test
	public void testUserExists() {
	    assertNotNull(adminUser);
	    assertNotNull(basicUser);
	}
	
	@Test
	public void testGetUsername() {
		assertEquals(adminUser.getUsername(),"admin");
		assertEquals(basicUser.getUsername(),"basic");
	}
	
	@Test
	public void testGetPassword() {
		assertEquals(adminUser.getPassword(),"password1");
		assertEquals(basicUser.getPassword(),"password2");
	}
	
	@Test
	public void testGetUserType() {
		assertEquals(adminUser.getUserType(),"AdminUser");
		assertEquals(basicUser.getUserType(),"BasicUser");
	}
	
	@Test
	public void testAddUser() throws HubRegistrationException {
		assertEquals(2, hub.getUserMap().size());
		assertEquals(adminUser, hub.getUserMap().get("admin"));
		assertEquals(basicUser, hub.getUserMap().get("basic"));
	}
	
	@Test
	public void testAddSameUser() {
		try {
			hub.register(adminUser);
			fail();
		} catch (HubRegistrationException e) {
			System.out.println("Test passes");
		}
	}
	
	@Test
	public void testRemoveUser() throws HubRegistrationException {
		hub.unregister(adminUser);
		assertEquals(1, hub.getUserMap().size());
		assertEquals(basicUser, hub.getUserMap().get("basic"));
		assertFalse(hub.getUserMap().containsKey("admin"));
	}
	
	@Test
	public void testRemoveSameUser() {
		try {
			hub.unregister(adminUser);
			hub.unregister(adminUser);
			fail();
		} catch (HubRegistrationException e) {
			System.out.println("Test passes");
		}
	}
	
}

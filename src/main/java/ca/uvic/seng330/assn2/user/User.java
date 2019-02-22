package ca.uvic.seng330.assn2.user;

import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import javafx.scene.control.Button;

public abstract class User {
	private final String aUsername;
	private final String aPassword;
	private Button RemoveButton = new Button("Remove");
	private String UserType;
	private Mediator aModel;
	private AdminController aAdminController;
	
	public User(Mediator pMed,AdminController ac,String pUsername, String pPassword) {
		aUsername = pUsername;
		aPassword = pPassword;
		aModel = pMed;
		aAdminController = ac;
	}
	
	public String getUserType() {
		if(this instanceof AdminUser) {
			return UserType = new String("AdminUser");
		}else {
			return UserType = new String("BasicUser");
		}
	}
	
	public String getUsername() {
		return aUsername;
	}
	
	public String getPassword() {
		return aPassword;
	}
	
	public Button getRemoveButton() {
		if(aUsername =="10000" || aUsername =="10086") {
			RemoveButton.setDisable(true);
		}
		RemoveButton.setOnAction(actionEvent -> {
			try {
				aModel.unregister(this);
				aAdminController.buildUserTable();
			} catch (HubRegistrationException e) {
				e.printStackTrace();
			}
		});
		RemoveButton.setId("UserRemoveButton");
		return RemoveButton;
	}
	
	@Override
	public String toString() {
		return aUsername;
	}
}

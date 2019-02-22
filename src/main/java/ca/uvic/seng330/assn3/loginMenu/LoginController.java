package ca.uvic.seng330.assn3.loginMenu;

import java.io.File;
import java.io.IOException;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.user.AdminUser;
import ca.uvic.seng330.assn2.user.User;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
	@FXML private Parent root;
	@FXML private TextField account;
	@FXML private TextField password;
	@FXML private Button signin;
	@FXML private Text errorMessage = new Text();
	
	private  Hub model;
	private String username;
	private String psw;
	private User user;
	private Scene adminscene;
	private Scene mainscene;
	private MainController aMainController;
	private AdminController aAdminController;
	
	public void setmodel(Hub h) {
		model = h;
		password.textProperty().addListener((obs,oldValue,newValue) -> {
			if (newValue.equals("")) {
				errorMessage.setText("");
			}
		});
	}
	
	public void setMainController(MainController mc) {
		aMainController = mc;
	}
	
	public void setAdminController(AdminController ac) {
		aAdminController = ac;
	}
	
	public void setnextscenes(Scene scene,Scene scene2) {
		adminscene = scene;
		mainscene = scene2;
	}
	
	@FXML 
	private void signin(ActionEvent event) throws IOException {
		username = account.getText();
		psw = password.getText();
		File file = new File("data.json");
		if(file.exists() && model.getDeviceMap().isEmpty() && model.getUserMap().size()==2) {
			model.startup();
		}
		if(model.getUsersMap().containsKey(username)) {
			
			user = model.getUsersMap().get(username);
			if (user.getPassword().equals(psw)) {	//login successful, showNextView()
				model.setCurrentUser(username); // set current user for app
				
				if(user instanceof AdminUser) {
					aMainController.BuildAdminTable();
					aAdminController.buildDeviceTable();
					aAdminController.buildUserTable();
					aAdminController.buildLogList(model.getLogList());
					Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			        primaryStage.setScene(adminscene);
			        Stage anotherStage = new Stage();
			        anotherStage.setScene(mainscene);
			        anotherStage.show();
				}else {
					aMainController.BuildBasicTable(username);
					Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			        primaryStage.setScene(mainscene);
				}
			}else {
				errorMessage.setText("Login error, retry");
			}
			
		} else {
			errorMessage.setText("Login error, retry");
		}
	}
	
}
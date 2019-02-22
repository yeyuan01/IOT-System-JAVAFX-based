package ca.uvic.seng330.assn3;

import java.io.IOException;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.user.AdminUser;
import ca.uvic.seng330.assn2.user.BasicUser;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.loginMenu.LoginController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	  @Override
	  public void start(Stage primaryStage) throws IOException {

		  	Hub h = new Hub();
			
		  	// load FXML files
		  	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("loginMenu/LoginView.fxml"));
			Parent loginpane = loader.load();
			Scene loginscene = new Scene(loginpane, 600, 400);	

			FXMLLoader loader2 = new FXMLLoader(this.getClass().getResource("mainMenu/MainView.fxml"));
			Parent mainpane = loader2.load();
			Scene mainscene = new Scene(mainpane, 1100, 900);
			
			FXMLLoader loader3 = new FXMLLoader(this.getClass().getResource("adminMenu/AdminView.fxml"));
			Parent adminpane = loader3.load();
			Scene adminscene = new Scene(adminpane, 900, 700);
			
			// set login controller
			LoginController login =(LoginController) loader.getController();
			login.setmodel(h);
			login.setnextscenes(adminscene,mainscene);
			
			// set main controller
			MainController main = (MainController) loader2.getController();
			main.setmodel(h);
			
			// set admin controller
			AdminController adminc = (AdminController) loader3.getController();
			h.setcontrollers(main, adminc);
			adminc.setmodel(h);
			adminc.setmaincontroller(main);
			
			login.setMainController(main);
			login.setAdminController(adminc);
			
			if(!h.getUserMap().containsKey("10086") || !h.getUserMap().containsKey("10000") ) {
			  	AdminUser admin = new AdminUser(h,adminc,"10086","123456");
				BasicUser buser = new BasicUser(h,adminc,"10000","654321");
			}
			
	        primaryStage.setScene(loginscene);
	        primaryStage.show();
	  }

	  public static void main(String[] args) {
	    launch(args);
	  }
}

package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class LightbulbController {
	@FXML private Button toggle;
	@FXML private Text isOn;
	
	private Hub aHub;
	private Lightbulb aLightbulb;	// lightbulb of interest
	private MainController aMainController;
	private  AdminController aAdminController ;
	
	public LightbulbController(Hub pHub,Lightbulb lightbulb,AdminController ac,MainController mc ) {
		aAdminController = ac;
		aMainController = mc;
		aHub = pHub;
		aLightbulb  = lightbulb;
		
		// update view after system shutdown
		aLightbulb.isOnProperty().addListener((obs,oldValue,newValue) -> {
			if (newValue == false) {
				aLightbulb.getOnoff().setText("Off");
			}
		});
		
		setButtons();
	}
	
	private void setButtons() {
	    aLightbulb.getToggleButton().setOnAction(actionEvent -> toggle());
	    aLightbulb.getRemoveButton().setOnAction(actionEvent -> remove());
	    aLightbulb.getAssignButton().setOnAction(actionEvent -> {
	    	if (!aLightbulb.getUsernameTextfield().getText().equals("")) {
	    		aHub.assignDeviceToUser(aLightbulb.getUsernameTextfield().getText(), aLightbulb.getIdentifier());
	    	}
	    });
	}
	
	private void remove() {
    	try {
			aHub.unregister(aLightbulb);
			aAdminController.buildDeviceTable();
			aMainController.BuildAdminTable();
		} catch (HubRegistrationException e) {
			e.printStackTrace();
		}
	}
	
	private void toggle() {
		aLightbulb.toggle();
		if (aLightbulb.getIsOn()) {
			aLightbulb.getOnoff().setText("On");
		} else {
			aLightbulb.getOnoff().setText("Off");
		}
	}
	
	
	
}

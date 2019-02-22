package ca.uvic.seng330.assn3;


import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;


public class SmartPlugController {
	
	private Hub aHub;
	private SmartPlug aSmartPlug;	// smartplug of interest
	private  AdminController aAdminController ;
	private  MainController aMainController ;
	
	public SmartPlugController(Hub pHub,SmartPlug smartplug,AdminController ac,MainController mc) {
		aAdminController = ac;
		aMainController = mc;
		aHub = pHub;
		aSmartPlug = smartplug;
		
		// update view after system shutdown
		aSmartPlug.isOnProperty().addListener((obs,oldValue,newValue) -> {
			if (newValue == false) {
				aSmartPlug.getOnoff().setText("Off");
			}
		});
		
		setButtons();
	}
	
	private void setButtons() {
	    aSmartPlug.getToggleButton().setOnAction(actionEvent -> toggle());
	    aSmartPlug.getRemoveButton().setOnAction(actionEvent -> remove());
	    aSmartPlug.getAssignButton().setOnAction(actionEvent -> {
	    	if (!aSmartPlug.getUsernameTextfield().getText().equals("")) {
	    		aHub.assignDeviceToUser(aSmartPlug.getUsernameTextfield().getText(), aSmartPlug.getIdentifier());
	    	}
	    });
	}
	
	private void remove() {
    	try {
			aHub.unregister(aSmartPlug);
			aAdminController.buildDeviceTable();
			aMainController.BuildAdminTable();
		} catch (HubRegistrationException e) {
			e.printStackTrace();
		}
	}
	
	private void toggle() {
		aSmartPlug.toggle();
		if (aSmartPlug.getIsOn()) {
			aSmartPlug.getOnoff().setText("On");
		} else {
			aSmartPlug.getOnoff().setText("Off");
		}
	}

	
}

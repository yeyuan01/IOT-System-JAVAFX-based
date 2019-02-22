package ca.uvic.seng330.assn3;


import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;
import ca.uvic.seng330.assn2.part1.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.scene.control.TextField;

/* NOTE!!
 * Set new temperature for thermostat
 * only keep track of the same Temperature object
 * 
 * use: 
 * aThermostat.getTemp().setTemperature(88);
 * aThermostat.getTemp().setUnit(Unit.CELSIUS);
 * 
 * do not use:
 * Temperature t = new Temperature(88, Unit.CELSIUS);
 * aThermostat.setTemp(t);
 */

public class ThermostatController {	
	private Hub aHub;
	private Thermostat aThermostat;	// thermostat of interest
	private MainController aMainController;
	private  AdminController aAdminController ;
	
	public ThermostatController(Hub pHub, Thermostat pThermostat,AdminController ac,MainController mc) {
		aHub = pHub;
		aThermostat  =pThermostat;
		aAdminController = ac;
		aMainController = mc;
		
		// update view after system shutdown
		aThermostat.isOnProperty().addListener((obs,oldValue,newValue) -> {
			if (newValue == false) {
				aThermostat.getToggleButton().setText("Start");
				// hide textbox and unit button
				aThermostat.getTemptext().setVisible(false);
				aThermostat.getUnit().setVisible(false);
			}
		});
				
		initializeView();
		syncTextfieldAndModel();
		setButtons();
	}

	
	private void initializeView() {
	    // set on/off button, hide textbox and tempUnit button
	    aThermostat.getToggleButton().setText("Start");
	    aThermostat.getTemptext().setVisible(false);
	    aThermostat.getUnit().setVisible(false);
	
	    // set default temperature
	    aThermostat.getTemptext().setText(((Double)aThermostat.getTemp().getTemperature()).toString());
	    aThermostat.getUnit().setText(aThermostat.getTemp().getUnit().toString());
	}
	
	/*
	 * Synchronize temperature shown in text field and temperature stored in model
	 */
	private void syncTextfieldAndModel() {
	    // if text field changes, update model
	    aThermostat.getTemptext().textProperty().addListener((obs, oldTemp, newTemp) -> {
	    	if (!newTemp.equals("")) {
	    		aThermostat.setTemperature(Double.parseDouble(newTemp));
	    	}
	    });
	    
	    // if model changes, update text field
	    aThermostat.getTemp().temperatureProperty().addListener((obs, oldTemp, newTemp) -> updateTempTextfieldIfNeeded(newTemp, aThermostat.getTemptext()));
	    aThermostat.getTemp().unitProperty().addListener((obs, oldUnit, newUnit) -> aThermostat.getUnit().setText(newUnit.toString()));
	}
	
	private void updateTempTextfieldIfNeeded(Number value, TextField field) {
		    String s = value.toString() ;
		    if (! field.getText().equals(s)) {
		      field.setText(s);
		    }
	}
	  
	private void setButtons() {
	    aThermostat.getToggleButton().setOnAction(actionEvent -> start());
	    aThermostat.getUnit().setOnAction(actionEvent -> toggleUnit());
	    aThermostat.getRemoveButton().setOnAction(actionEvent -> remove());
	    aThermostat.getAssignButton().setOnAction(actionEvent -> {
	    	if (!aThermostat.getUsernameTextfield().getText().equals("")) {
	    		aHub.assignDeviceToUser(aThermostat.getUsernameTextfield().getText(), aThermostat.getIdentifier());
	    	}
	    });
	}
	
	// turn on/off thermostat
	private void start() {
		if (aThermostat.isOn()) {
			aThermostat.turnoff();
			aThermostat.getToggleButton().setText("Start");
			// hide textbox and unit button
			aThermostat.getTemptext().setVisible(false);
			aThermostat.getUnit().setVisible(false);
		} else {
			aThermostat.turnon();
			aThermostat.getToggleButton().setText("Turn Off");
			// show textbox and unit button
			aThermostat.getTemptext().setVisible(true);
			aThermostat.getUnit().setVisible(true);
		}
	}
	
	private void remove() {
    	try {
			aHub.unregister(aThermostat);
			aAdminController.buildDeviceTable();
			aMainController.BuildAdminTable();
		} catch (HubRegistrationException e) {
			e.printStackTrace();
		}
	}
	
	private void toggleUnit() {
		if(aThermostat.getTemp().getUnit() == Unit.CELSIUS) {
			double tempInC = aThermostat.getTemp().getTemperature();
			double tempInF = (double)(tempInC * (9.0)/(5.0) + 32.0);
			aThermostat.setTemperature(tempInF);
			aThermostat.getTemptext().setText(Double.toString(tempInF));
			aThermostat.setUnit(Unit.FAHRENHEIT);
			aThermostat.getUnit().setText("FAHRENHEIT");
		} else {
			double tempInF = aThermostat.getTemp().getTemperature();
			double tempInC = (double)((tempInF - 32.0) * ((5.0) / (9.0)));
			aThermostat.setTemperature(tempInC);
			aThermostat.getTemptext().setText(Double.toString(tempInC));
			aThermostat.setUnit(Unit.CELSIUS);
			aThermostat.getUnit().setText("CELSIUS");
		}
	}

}

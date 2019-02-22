package ca.uvic.seng330.assn2.part1.devices;

import java.util.UUID;

import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import ca.uvic.seng330.assn2.part1.Status;
import ca.uvic.seng330.assn2.part1.devices.Temperature.Unit;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Thermostat extends Device {
  private final Mediator aMed;
  private Status status = Status.FUNCTIONING;
  private final BooleanProperty isOn = new SimpleBooleanProperty();
  private Button ToggleButton = new Button("Start");
  private Button RemoveButton = new Button("Remove");
  private Temperature setPoint;
  private TextField Temptext = new TextField();
  private Button Unit = new Button("FAHRENHEIT");

  {
    try {
      setPoint = new Temperature(72, Temperature.Unit.FAHRENHEIT);
    } catch (Temperature.TemperatureOutofBoundsException e) {
      e.printStackTrace();
    }
  }

  public Thermostat(Mediator pMed) {
    super();
    aMed = pMed;
    Temptext.setEditable(true);
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }
  
  public Thermostat(Mediator pMed, UUID pid) {
    super();
    setIdentifier(pid);
    aMed = pMed;
    Temptext.setEditable(true);
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }

  // Do not use this method. Cannot listen on Temperature object.
  public void setTemp(Temperature t) {
    setPoint = t;
    aMed.alert(this, "Thermostat setting temp to " + t.getTemperature());
  }
  
  public Temperature getTemp() {
	return setPoint;
  }
  
  // Helper method: temperature and unit changes can be listened
  public void setTemperature(double pTemp) {
	  setPoint.setTemperature(pTemp);
	  aMed.alert(this, "Thermostat setting temperature to " + pTemp);
  }
  
  public void setUnit(Unit pUnit) {
	  setPoint.setUnit(pUnit);
	  aMed.alert(this, "Thermostat setting unit to " + pUnit);
  }
  
  
  public void turnon() {
	  setIsOn(true);
	  aMed.alert(this, "Thermostat is turned on");
  }
  
  public void turnoff() {
	  setIsOn(false);
	  aMed.alert(this, "Thermostat is turned off");
  }
  
  
  /*
   * synchronized getIsOn for check status thread
   */
  public boolean isOn() {
	  synchronized(this) {
		  return getIsOn();
	  }
  }
  
  public final BooleanProperty isOnProperty() {
    return isOn;
  }

  public final boolean getIsOn() {
    return isOnProperty().get();
  }

  public final void setIsOn(final boolean pIsOn) {
    this.isOnProperty().set(pIsOn);
  }
  
  
  /*
   * helper methods for JavaFX elements
   */
  public Button getRemoveButton() {
	  RemoveButton.setId("ThermostatRemoveButton");
	  return RemoveButton;
  }
  
  public Button getToggleButton() {
	  ToggleButton.setId("ThermostatToggleButton");
	  return ToggleButton;
  }

  
  public TextField getTemptext() {
	  Temptext.setId("ThermostatTemptext");
	return Temptext;
  }
  
  public Button getUnit() {
	  Unit.setId("ThermostatUnit");
	  return Unit;
  }
  
  
  @Override
  public Status getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return "Thermostat id " + super.getIdentifier().toString();
  }
}
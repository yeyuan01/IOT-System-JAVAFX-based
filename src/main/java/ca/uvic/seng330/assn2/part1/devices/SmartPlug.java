package ca.uvic.seng330.assn2.part1.devices;

import java.util.UUID;

import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class SmartPlug extends Device implements SwitchableDevice {

  private final Mediator aMed;
  private final BooleanProperty isOn = new SimpleBooleanProperty();
  private Button ToggleButton = new Button("Toggle");
  private Button RemoveButton = new Button("Remove");
  private Text Onoff = new Text("Off");
  
  
  public SmartPlug(Mediator med) {
    super();
    aMed = med;
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }
  
  public SmartPlug(Mediator pMed, UUID pid) {
    super();
    setIdentifier(pid);
    aMed = pMed;
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }
  
  public Button getRemoveButton() {
	  RemoveButton.setId("SmartPlugRemoveButton");
	  return RemoveButton;
  }
  
  public Button getToggleButton() {
	  ToggleButton.setId("SmartPlugToggleButton");
	  return ToggleButton;
  }
  
  public Text getOnoff() {
	  Onoff.setId("SmartPlugOnoff");
	  return Onoff;
  }

  @Override
  public void toggle() {
	if (getIsOn() == true)	setIsOn(false);
	else	setIsOn(true);
    String status = "smartplug is now ";
    aMed.alert(this, status + Boolean.toString(getIsOn()));
  }

  public final BooleanProperty isOnProperty() {
    return isOn;
  }

  public final boolean getIsOn() {
	  synchronized(this) {
		  return isOnProperty().get();
	  }
  }

  public final void setIsOn(final boolean pIsOn) {
    this.isOnProperty().set(pIsOn);
  }
	  
  @Override
  public String toString() {
    return "Smartplug id " + super.getIdentifier().toString();
  }
}

package ca.uvic.seng330.assn2.part1.devices;

import java.util.UUID;

import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class Lightbulb extends Device implements SwitchableDevice {
  private final BooleanProperty isOn = new SimpleBooleanProperty();
  private final Mediator aMed;
  private Button ToggleButton = new Button("Toggle");
  private Button RemoveButton = new Button("Remove");
  private Text Onoff = new Text("Off");
  
  public Lightbulb(Mediator pMed) {
    super();
    aMed = pMed;
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }
  
  public Lightbulb(Mediator pMed, UUID pid) {
    super();
    setIdentifier(pid);
    aMed = pMed;
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void toggle() {
	if (getIsOn() == true)	setIsOn(false);
	else	setIsOn(true);
    String status = "lightbulb is now ";
    aMed.alert(this, status + Boolean.toString(getIsOn()));
  }

  public Button getRemoveButton() {
	  RemoveButton.setId("LightbulbRemoveButton");
	  return RemoveButton;
  }
  
  public Button getToggleButton() {
	  ToggleButton.setId("LightbulbToggleButton");
	  return ToggleButton;
  }
  
  public Text getOnoff() {
	  Onoff.setId("LightbulbOnoff");
	  return Onoff;
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
    return "Lightbulb id " + super.getIdentifier().toString();
  }
}

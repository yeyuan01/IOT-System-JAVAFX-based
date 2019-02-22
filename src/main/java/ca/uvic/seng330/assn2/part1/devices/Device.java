package ca.uvic.seng330.assn2.part1.devices;

import ca.uvic.seng330.assn2.part1.Status;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.UUID;

public abstract class Device {

  private UUID Identifier = UUID.randomUUID();
  private Status aStatus; // This can't be NULL!
  private String DeviceName;
  private Button AssignButton = new Button("Assign");
  private TextField UsernameTextfield = new TextField();
  public UUID getIdentifier() {
    return Identifier;
  }
  
  public void setIdentifier(UUID pid) {
    Identifier = pid;
  }

  public Status getStatus() {
    // Since the status can't be NULL, then check IF NULL and IF return dummy
    // status.
    return aStatus == null ? Status.NOT_AVAILABLE : aStatus;
  }
  
  public Button getAssignButton() {
	  AssignButton.setId("DeviceAssignButton");
	  return AssignButton;
  }
  
  public String getDeviceName() {
	  if(this instanceof Camera) {
		  return DeviceName = new String("Camera");
	  } else if(this instanceof Lightbulb) {
		  return DeviceName = new String("Lightbulb");
	  } else if(this instanceof SmartPlug) {
		  return DeviceName = new String("SmartPlug");
	  } else {
		  return DeviceName = new String("Thermostat");
	  }	  
  }
  
  public void setStatus(Status status) {
	  this.aStatus = status;
  }
  
  public TextField getUsernameTextfield() {
	  UsernameTextfield.setId("UsernameTextfield");
	  return UsernameTextfield;
  }
  
  @Override
  public String toString() {
    return Identifier.toString();
  }

}

package ca.uvic.seng330.assn2.part1.devices;

import java.util.UUID;

import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import ca.uvic.seng330.assn2.part1.Status;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;

public class Camera extends Device {

  private boolean isRecording = false;
  private final BooleanProperty isOn = new SimpleBooleanProperty();
  private boolean someoneInRoom = false;
  private boolean objectDetected = false;
  private Status status = Status.FUNCTIONING;
  private int diskSize = 999;
  private int currSize = 0;
//  private ImageView data = new ImageView();
//  private Image image;
  private StackPane stackPane = new StackPane();
  private MediaView video = new MediaView();
  private final Mediator aMed;
  private Button ToggleButton = new Button("Start");
  private Button RemoveButton = new Button("Remove");
  private Button RecordButton = new Button("Record");
  
  
  public Camera(Mediator med) {
    super();
    aMed = med;
//    image = new Image(new File("cat.png").toURI().toString());
//    data.setImage(image);
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
    	e.printStackTrace();
    }
  }

  public Camera(Mediator pMed, UUID pid) {
    super();
    setIdentifier(pid);
    aMed = pMed;
//    image = new Image(new File("cat.png").toURI().toString());
//    data.setImage(image);
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }
  
  // dummy method for testing purpose
  public void setFull() {
	  currSize = diskSize;
  }
  
  public void turnon() {
	  setIsOn(true);
	  isRecording = false;
	  aMed.alert(this, "Camera is started");
  }
  
  public void turnoff() {
	  setIsOn(false);
	  isRecording = false;
	  aMed.alert(this, "Camera is turned off");
  }
  
  public void record() throws CameraFullException {
	  if(currSize >= diskSize) {
		  aMed.alert(this, "Camera is full");
		  throw new CameraFullException("Camera Full");
	  } else {
		  isRecording = true;
		  currSize += 400;
		  aMed.alert(this, "Camera started recording");
	  }
  }
  
  public void stopRecord() {
	  isRecording = false;
	  aMed.alert(this, "Camera stopped recording");
  }
  
  public boolean isRecording() {
	  synchronized(this) {
		  return isRecording;
	  }
  }
  
  
  public void objectDetected() {
	  objectDetected = true;
	  aMed.alert(this, "Camera detected an object");
  }
  
  public boolean getObjectDetected() {
	  return objectDetected;
  }
  
  
  public void noOneInRoom() {
	  someoneInRoom = false;
	  aMed.alert(this, "Camera sees no one in room");
  }
  
  public void someoneEntersRoom() {
	  someoneInRoom = true;
	  aMed.alert(this, "Camera sees someone enters room");
  }
  
  public boolean getSomeoneInRoom() {
	  return someoneInRoom;
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
  
  
  /*
   * Helper methods for JavaFX elements
   */
  public Button getRemoveButton() {
	  RemoveButton.setId("CameraRemoveButton");
	  return RemoveButton;
  }
  
  public Button getToggleButton() {
	  ToggleButton.setId("CameraToggleButton");
	  return ToggleButton;
  }
  
  public Button getRecordButton() {
	  RecordButton.setId("CameraRecordButton");
	  return RecordButton;
  }
  
  /*
  public ImageView getData() {
	  data.setId("CameraData");
	  return data;
  }
  */
  
  public MediaView getVideo() {
	  video.setId("CameraVideo");
	  return video;
  }
  
  public StackPane getStackPane() {
	  stackPane.setId("CameraStackPane");
	  return stackPane;
  }
  
  
  @Override
  public Status getStatus() {
	  return status;
  }

  @Override
  public String toString() {
	  return "Camera id " + super.getIdentifier().toString();
  }

}


package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn2.part1.Hub;
import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.CameraFullException;
import ca.uvic.seng330.assn3.adminMenu.AdminController;
import ca.uvic.seng330.assn3.mainMenu.MainController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class CameraController {
	private Hub aHub;
	private Camera aCamera;	// camera of interest
	private MainController aMainController;
	private AdminController aAdminController;
	private MediaPlayer mediaPlayer;
	private Media media;
	private static final String MEDIA_URL = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
	
	public void initializeMedia() {
		aCamera.getStackPane().setPrefSize(200, 100);
		aCamera.getStackPane().getChildren().add(aCamera.getVideo());
		media = new Media(MEDIA_URL);
		mediaPlayer = new MediaPlayer(media);
		aCamera.getVideo().setMediaPlayer(mediaPlayer);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		aCamera.getVideo().fitWidthProperty().bind(aCamera.getStackPane().widthProperty());
		aCamera.getVideo().fitHeightProperty().bind(aCamera.getStackPane().heightProperty());
	}
	
	public CameraController(Hub pHub, Camera d,AdminController ac,MainController mc) {
		aHub = pHub;
		aCamera  = d;
		aMainController = mc;
		aAdminController = ac;
		initializeMedia();
		
		// update view after system shutdown
		aCamera.isOnProperty().addListener((obs,oldValue,newValue) -> {
			if (newValue == false) {
				initializeView();
				mediaPlayer.stop();
			}
		});
		
		initializeView();
		setbuttons();
	}


	/*
	 * hide record button and data
	 */
	private void initializeView() {
	    aCamera.getToggleButton().setText("Start");
	    aCamera.getRecordButton().setVisible(false);
	    //aCamera.getData().setVisible(false);
	    aCamera.getVideo().setVisible(false);
	}
	
	private void setbuttons() {
		aCamera.getToggleButton().setOnAction(actionEvent -> start());
		aCamera.getRecordButton().setOnAction(actionEvent -> record());
		aCamera.getRemoveButton().setOnAction(actionEvent -> remove());
		aCamera.getAssignButton().setOnAction(actionEvent -> {
			if (!aCamera.getUsernameTextfield().getText().equals("")) {
				aHub.assignDeviceToUser(aCamera.getUsernameTextfield().getText(), aCamera.getIdentifier());
			}
			
		});
	}
	
	private void remove() {
    	try {
			aHub.unregister(aCamera);
			aAdminController.buildDeviceTable();
			aMainController.BuildAdminTable();
		} catch (HubRegistrationException e) {
			e.printStackTrace();
		} 
	}
	
	private void record() {
		if (aCamera.isRecording()) {
			aCamera.stopRecord();
			aCamera.getRecordButton().setText("Record");
			mediaPlayer.pause();
		} else {
			try {
				aCamera.record();
				aCamera.getRecordButton().setText("Stop Recording");
				mediaPlayer.play();
			} catch (CameraFullException e) {
				aHub.alert(aCamera, "Camera is full");
			}
		}
	}
	
	private void start() {
		if (aCamera.getIsOn()) {
			aCamera.turnoff();
			aCamera.getToggleButton().setText("Start");
			aCamera.getRecordButton().setText("Record");
			aCamera.getRecordButton().setVisible(false);
			//aCamera.getData().setVisible(false);
			aCamera.getVideo().setVisible(false);
			mediaPlayer.seek(mediaPlayer.getStartTime());
			mediaPlayer.stop();
		} else {
			aCamera.turnon();
			aCamera.getToggleButton().setText("Turn Off");
			aCamera.getRecordButton().setVisible(true);
			//aCamera.getData().setVisible(true);
			aCamera.getVideo().setVisible(true);
		}
	}

}

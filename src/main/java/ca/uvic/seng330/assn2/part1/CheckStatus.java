package ca.uvic.seng330.assn2.part1;

import ca.uvic.seng330.assn2.part1.devices.Camera;
import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn2.part1.devices.Lightbulb;
import ca.uvic.seng330.assn2.part1.devices.SmartPlug;
import ca.uvic.seng330.assn2.part1.devices.Thermostat;
import ca.uvic.seng330.assn2.part1.devices.Temperature.Unit;

public class CheckStatus implements Runnable {
	Hub aHub;
	
	public void setModel(Hub pHub) {
		aHub = pHub;
	}

	public void run() {
		  while (true) {
			  for (Device d: aHub.getDeviceMap().values()) {
				if (d instanceof Camera) {
					checkCameraStatus(d);
				} else if (d instanceof Thermostat) {
					checkThermostatStatus(d);
				} else if (d instanceof SmartPlug) {
					checkSmartPlugStatus(d);
				} else if (d instanceof Lightbulb) {
					checkLightbulbStatus(d);
				}
			  }
			  try {
		        Thread.sleep(1*60*1000);
		      } catch (InterruptedException e) {
		        return;
		      }
		  }
	}

	private void checkCameraStatus(Device d) {
	    boolean isOn = ((Camera)d).getIsOn();
	    boolean isRecording = ((Camera)d).isRecording();
	    if (isOn && isRecording) {
	    	aHub.log("Camera "+ d.getIdentifier() + " is on and recording");
	    } else if (isOn && !isRecording) {
	    	aHub.log("Camera "+ d.getIdentifier() + " is on but not recording");
	    } else if (!isOn) {
	    	aHub.log("Camera "+ d.getIdentifier() + " is off");
	    }
	}
	
	private void checkThermostatStatus(Device d) {
	    boolean isOn = ((Thermostat)d).getIsOn();
	    double temp = ((Thermostat)d).getTemp().getTemperature();
	    Unit unit = ((Thermostat)d).getTemp().getUnit();
	    if (isOn) {
	    	aHub.log("Thermostat "+ d.getIdentifier() + " is on and temperature is " + temp + " " + unit.toString());
	    } else {
	    	aHub.log("Thermostat "+ d.getIdentifier() + " is off");
	    }
	}
	
	private void checkSmartPlugStatus(Device d) {
	    boolean isOn = ((SmartPlug)d).getIsOn();
	    if (isOn) {
	    	aHub.log("SmartPlug "+ d.getIdentifier() + " is on");
	    } else {
	    	aHub.log("SmartPlug "+ d.getIdentifier() + " is off");
	    }
	}
	
	private void checkLightbulbStatus(Device d) {
	    boolean isOn = ((Lightbulb)d).getIsOn();
	    if (isOn) {
	    	aHub.log("Lightbulb "+ d.getIdentifier() + " is on");
	    } else {
	    	aHub.log("Lightbulb "+ d.getIdentifier() + " is off");
	    }
	}
  
}

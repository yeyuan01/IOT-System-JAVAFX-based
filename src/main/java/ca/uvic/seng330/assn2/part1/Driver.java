package ca.uvic.seng330.assn2.part1;

import ca.uvic.seng330.assn2.part1.devices.*;

public class Driver {

  public Driver() {
    Mediator med = new Hub();
    Thermostat t = new Thermostat(med);
    Lightbulb l = new Lightbulb(med);
    Camera c = new Camera(med);
    SwitchableDevice s = new SmartPlug(med);
    // ideally, we would load these in from a config file
  }

  public static void main(String[] args) {
    new Driver();
  }
}

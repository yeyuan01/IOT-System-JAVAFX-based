package ca.uvic.seng330.assn2.part1.devices;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Temperature {

  public enum Unit {
    CELSIUS, FAHRENHEIT
  }

  private final ObjectProperty<Unit> unit = new SimpleObjectProperty<Unit>();
  private final DoubleProperty temperature = new SimpleDoubleProperty();

  public Temperature() {
	  
  }
  
  public Temperature(double pTemp, Unit pUnit) throws TemperatureOutofBoundsException {
    if (pTemp > 1000) {
      throw new TemperatureOutofBoundsException("Absurd temperature!");
    }
    unitProperty().set(pUnit);
    temperatureProperty().set(pTemp);
  }
  
  public final ObjectProperty<Unit> unitProperty() {
    return unit;
  }
  
  public final DoubleProperty temperatureProperty() {
    return temperature;
  }
  
  public final double getTemperature() {
    return this.temperatureProperty().get();
  }

  public final Unit getUnit() {
    return this.unitProperty().get();
  }
  
  public final void setTemperature(double pTemp) {
	  temperatureProperty().set(pTemp);
  }
  
  public final void setUnit(Unit pUnit) {
	  unitProperty().set(pUnit);
  }
  
  public class TemperatureOutofBoundsException extends Exception {
    public TemperatureOutofBoundsException(String s) {
      super(s);
    }
  }
}

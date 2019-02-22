package ca.uvic.seng330.assn2.part1;

import ca.uvic.seng330.assn2.part1.devices.Device;
import ca.uvic.seng330.assn2.part2.Client;
import ca.uvic.seng330.assn2.user.User;

public interface Mediator {

  public void unregister(Device device) throws HubRegistrationException;

  public void unregister(Client client) throws HubRegistrationException;

  public void unregister(User pUser) throws HubRegistrationException;
  
  //not in spec, do not test
  public void register(Device pDevice) throws HubRegistrationException;

  public void register(Client pClient) throws HubRegistrationException;
  
  public void register(User pUser) throws HubRegistrationException;

  public void alert(Device pDevice, String pMessage);
}

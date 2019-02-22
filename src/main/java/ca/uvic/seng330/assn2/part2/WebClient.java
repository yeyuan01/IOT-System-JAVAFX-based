package ca.uvic.seng330.assn2.part2;

import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import org.json.JSONObject;

public class WebClient extends Client {

  private final Mediator aMed;
  private JSONObject aJsonObj;

  public WebClient(Mediator pMed) {
    aMed = pMed;
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void notify(JSONObject json) {
    super.notify(json);
    this.aJsonObj = json;
    display();
  }

  @Override
  public void notify(String msg) {
    super.notify(msg);
    System.out.println("WebClient is displaying message: " + msg);
  }
  
  private void display() {
    System.out.println("WebClient is displaying content from : " + aJsonObj.getString("node_id"));
    //TODO  should be on web page
  }

  @Override
  public String toString() {
    return "WebClient: " + getIdentifier().toString();
  }
}

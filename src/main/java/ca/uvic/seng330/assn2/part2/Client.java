package ca.uvic.seng330.assn2.part2;

import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uvic.seng330.assn2.part1.Hub;

public abstract class Client {

  private final UUID uuid = UUID.randomUUID();
  private Logger logger = LoggerFactory.getLogger(Hub.class);

  public UUID getIdentifier() {
    return uuid;
  }

  public void notify(JSONObject json) {
    logger.info(json.toString());
  }
  
  public void notify(String msg) {
    logger.info(msg);
  }

  @Override
  public String toString() {
    return getIdentifier().toString();
  }
}

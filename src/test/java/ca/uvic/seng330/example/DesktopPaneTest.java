package ca.uvic.seng330.example;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import static org.testfx.api.FxAssert.verifyThat;

public class DesktopPaneTest extends ApplicationTest {

  private Scene scene;

  @Override
  public void start(Stage primaryStage) {
    AdditionModel model = new AdditionModel();
    AdditionController controller = new AdditionController(model);
    AdditionView view = new AdditionView(controller, model);

    scene = new Scene(view.asParent(), 400, 400);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Test public void should_contain_button() {
    // expect:
    verifyThat(".button", LabeledMatchers.hasText("click me!"));

  }

  @Test public void should_contain_field() {
    //given:
    clickOn("#yField").write("33");
    // expect:
    verifyThat("#yField" , TextInputControlMatchers.hasText("33"));
  }
}
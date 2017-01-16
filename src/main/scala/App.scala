import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.geometry.Rectangle2D
import javafx.scene.{Parent, Scene}
import javafx.stage.{Screen, Stage}

class App extends Application {

  override def start(stage: Stage): Unit = {
    val screen: Screen = Screen.getPrimary();
    val bounds: Rectangle2D  = screen.getVisualBounds();

    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setWidth(bounds.getWidth());
    stage.setHeight(bounds.getHeight());

    stage.setTitle("Hello World! I'm a scala program ;)")
    val root: Parent = FXMLLoader.load(getClass().getResource("presentation/mainView/main.fxml"));
    val scene = new Scene(root)
    stage.setScene(scene)
    stage.show()
  }
}

object App{
  def main(args: Array[String]) {
    Application.launch(classOf[App], args: _*)
  }
}
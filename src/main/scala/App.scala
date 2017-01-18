import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.geometry.Rectangle2D
import javafx.scene.Scene
import javafx.stage.{Screen, Stage}

import model.fuzzyModel.{DefaultConfig, FuzzyCarController}
import model.physicalModel.Car
import model.{BackgroundThread, Fuzzy}
import presentation.mainView.MainPresenter

class App extends Application {

  var loader: FXMLLoader = _

  override def start(stage: Stage): Unit = {
    val screen: Screen = Screen.getPrimary();
    val bounds: Rectangle2D  = screen.getVisualBounds();

    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setWidth(bounds.getWidth());
    stage.setHeight(bounds.getHeight());

    stage.setTitle("Fuzzy-Controller")
    loader = new FXMLLoader(getClass().getResource("presentation/mainView/main.fxml"))
    val scene = new Scene(loader.load())
    stage.setScene(scene)
    stage.show()

    BackgroundThread.mainPresenter = loader.getController[MainPresenter]
  }
}

object App{

  def main(args: Array[String]) {
    Application.launch(classOf[App], args: _*)
    BackgroundThread.fuzzyThread.interrupt()
  }
}


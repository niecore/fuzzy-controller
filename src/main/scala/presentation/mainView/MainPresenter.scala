package presentation.mainView

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control.{Slider, TextField}
import javafx.scene.layout.Pane
import javafx.stage.Stage

import presentation.configEditor.ConfigEditorPresenter

/**
  * Created by joel on 13.01.17.
  */
class MainPresenter extends Initializable {

  @FXML
  var sliderNewton: Slider = _
  @FXML
  var tfNewton1: TextField = _

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    tfNewton1.textProperty().bind(sliderNewton.valueProperty().asString())

  }

  def onPlay: Unit = {
    val loader = new FXMLLoader(getClass.getResource("presentation/configEditor/configEditor.fxml"))

    val stage = new Stage()
    stage.setScene(loader.load())
    stage.show()

    val controller = loader.getController[ConfigEditorPresenter]
  }
}

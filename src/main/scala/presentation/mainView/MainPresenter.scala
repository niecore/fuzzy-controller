package presentation.mainView

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Slider, TextField}

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
}

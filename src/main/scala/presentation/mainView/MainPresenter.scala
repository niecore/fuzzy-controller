package presentation.mainView

import java.net.URL
import java.util.ResourceBundle
import javafx.beans.binding.{Bindings, NumberBinding}
import javafx.beans.property._
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Label, Slider, TextField}
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.util.converter.NumberStringConverter

import model.fuzzyModel.DefaultConfig
import presentation.configEditor.ConfigEditorPresenter

/**
  * Created by joel on 13.01.17.
  */
class MainPresenter extends Initializable {

  @FXML
  var sliderNewton: Slider = _

  @FXML
  var tfNewton1: TextField = _

  @FXML
  var tfAcc1: TextField = _

  @FXML
  var tfSpeed1: TextField = _

  @FXML
  var tfPos1: TextField = _

  @FXML
  var tfNewton2: TextField = _

  @FXML
  var tfAcc2: TextField = _

  @FXML
  var tfSpeed2: TextField = _

  @FXML
  var tfPos2: TextField = _

  @FXML
  var tfDist: Label = _

  var newton1: DoubleProperty = new SimpleDoubleProperty()
  var acc1: DoubleProperty = new SimpleDoubleProperty()
  var speed1: DoubleProperty = new SimpleDoubleProperty()
  var pos1: DoubleProperty = new SimpleDoubleProperty()

  var newton2: DoubleProperty = new SimpleDoubleProperty()
  var acc2: DoubleProperty = new SimpleDoubleProperty()
  var speed2: DoubleProperty = new SimpleDoubleProperty()
  var pos2: DoubleProperty = new SimpleDoubleProperty()

  var dist: DoubleProperty = new SimpleDoubleProperty()

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    Bindings.bindBidirectional(tfNewton1.textProperty(), newton1, new NumberStringConverter())
    Bindings.bindBidirectional(tfAcc1.textProperty(), acc1, new NumberStringConverter())
    Bindings.bindBidirectional(tfSpeed1.textProperty(), speed1, new NumberStringConverter())
    Bindings.bindBidirectional(tfPos1.textProperty(), pos1, new NumberStringConverter())

    Bindings.bindBidirectional(tfNewton2.textProperty(), newton2, new NumberStringConverter())
    Bindings.bindBidirectional(tfAcc2.textProperty(), acc2, new NumberStringConverter())
    Bindings.bindBidirectional(tfSpeed2.textProperty(), speed2, new NumberStringConverter())
    Bindings.bindBidirectional(tfPos2.textProperty(), pos2, new NumberStringConverter())

    Bindings.bindBidirectional(tfDist.textProperty(), dist, new NumberStringConverter())

    newton2.addListener((o, t, n) => {println(s"new value: $n")})

    newton1.bindBidirectional(sliderNewton.valueProperty())
  }

  def onPlay: Unit = {
    val loader = new FXMLLoader(getClass.getResource("../configEditor/configEditor.fxml"))
    val stage = new Stage()
    stage.setScene(new Scene(loader.load()))
    stage.show()
    loader.getController[ConfigEditorPresenter].initData(DefaultConfig, this)
  }
}

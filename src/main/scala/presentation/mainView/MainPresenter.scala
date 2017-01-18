package presentation.mainView

import java.net.URL
import java.util.ResourceBundle
import javafx.beans.binding.{Bindings, NumberBinding}
import javafx.beans.property._
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.chart.{AreaChart, NumberAxis, XYChart}
import javafx.scene.{Group, Node, Parent, Scene}
import javafx.scene.control.{Label, Slider, Tab, TextField}
import javafx.scene.layout.Pane
import javafx.stage.{Modality, Stage}
import javafx.util.converter.NumberStringConverter

import model.BackgroundThread
import model.fuzzyModel.DefaultConfig
import model.fuzzyModel.entity.FuzzyConfig
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

  @FXML
  var posChartTab: Tab = _

  @FXML
  var speedChartTab: Tab = _

  var newton1: DoubleProperty = new SimpleDoubleProperty()
  var acc1: DoubleProperty = new SimpleDoubleProperty()
  var speed1: DoubleProperty = new SimpleDoubleProperty()
  var pos1: DoubleProperty = new SimpleDoubleProperty()

  var newton2: DoubleProperty = new SimpleDoubleProperty()
  var acc2: DoubleProperty = new SimpleDoubleProperty()
  var speed2: DoubleProperty = new SimpleDoubleProperty()
  var pos2: DoubleProperty = new SimpleDoubleProperty()

  var dist: DoubleProperty = new SimpleDoubleProperty()


  var carFrontposition: XYChart.Series[Number, Number] = _
  var carBackposition: XYChart.Series[Number, Number] = _


  var carFrontSpeed: XYChart.Series[Number, Number] = _
  var carBackSpeed: XYChart.Series[Number, Number] = _

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

    newton1.bindBidirectional(sliderNewton.valueProperty())

    initSpeedChart
    initPosChart
  }
  def initSpeedChart: Unit ={
    val xAxisPositionChart  = new NumberAxis()
    val yAxisPositionChart = new NumberAxis()
    val speedChart = new AreaChart[Number, Number](xAxisPositionChart, yAxisPositionChart)

    carFrontSpeed = new XYChart.Series[Number, Number]()
    carBackSpeed = new XYChart.Series[Number, Number]()

    speedChart.getData.addAll(carFrontSpeed, carBackSpeed)
    speedChartTab.setContent(speedChart)
  }

  def initPosChart: Unit ={
    val xAxisPositionChart  = new NumberAxis()
    val yAxisPositionChart = new NumberAxis()

    val positionChart = new AreaChart[Number, Number](xAxisPositionChart, yAxisPositionChart)
    carFrontposition = new XYChart.Series[Number, Number]()
    carBackposition = new XYChart.Series[Number, Number]()

    positionChart.getData.addAll(carFrontposition, carBackposition)
    posChartTab.setContent(positionChart)
  }

  def addnewSpeedToGraph(carFront: Double, carBack: Double, tick: Int): Unit = {
    carFrontSpeed.getData().add(new XYChart.Data(tick, carFront));
    carBackSpeed.getData().add(new XYChart.Data(tick, carBack));
  }

  def addnewPositionsToGraph(carFront: Double, carBack: Double, tick: Int): Unit ={
    carFrontposition.getData().add(new XYChart.Data(tick, carFront));
    carBackposition.getData().add(new XYChart.Data(tick, carBack));
  }

  def onPlay(event: ActionEvent): Unit = {
    val loader = new FXMLLoader(getClass.getResource("../configEditor/configEditor.fxml"))
    val stage = new Stage()
    stage.setScene(new Scene(loader.load()))
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(event.getSource match {
      case e: Node => e.getScene.getWindow
    })
    stage.show()
    loader.getController[ConfigEditorPresenter].initData(DefaultConfig, this)
  }

  def resetCharts = {
    carFrontSpeed.getData.clear
    carBackSpeed.getData.clear
    carFrontposition.getData.clear
    carBackposition.getData.clear
  }

  def onReset(event: ActionEvent): Unit ={
    BackgroundThread.fuzzyThread.interrupt()
  }
}

package presentation.mainView

import java.net.URL
import java.util.ResourceBundle
import javafx.beans.binding.{Bindings, NumberBinding}
import javafx.beans.property._
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.chart.{AreaChart, LineChart, NumberAxis, XYChart}
import javafx.scene.{Group, Node, Parent, Scene}
import javafx.scene.control._
import javafx.scene.layout.Pane
import javafx.stage.{Modality, Stage}
import javafx.util.converter.NumberStringConverter

import model.BackgroundThread
import model.fuzzyModel.DefaultConfig
import model.fuzzyModel.entity.{Fuzzy, FuzzyBool, FuzzyConfig, FuzzyValueConnector}
import model.physicalModel.Car
import presentation.configEditor.ConfigEditorPresenter

/**
  * Created by joel on 13.01.17.
  */
class MainPresenter extends Initializable {

  val forceOutput = new FuzzyValueConnector("Force", -8000, 4000, false)
  var plot: LineChart[Number, Number] = _

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

  @FXML
  var forceChartTab: Tab = _

  @FXML
  var accChartTab: Tab = _

  @FXML
  var split: SplitPane = _

  var newton1: DoubleProperty = new SimpleDoubleProperty()
  var acc1: DoubleProperty = new SimpleDoubleProperty()
  var speed1: DoubleProperty = new SimpleDoubleProperty()
  var pos1: DoubleProperty = new SimpleDoubleProperty()

  var newton2: DoubleProperty = new SimpleDoubleProperty()
  var acc2: DoubleProperty = new SimpleDoubleProperty()
  var speed2: DoubleProperty = new SimpleDoubleProperty()
  var pos2: DoubleProperty = new SimpleDoubleProperty()

  var dist: DoubleProperty = new SimpleDoubleProperty()


  var carFrontPosition: XYChart.Series[Number, Number] = _
  var carBackPosition: XYChart.Series[Number, Number] = _

  var carFrontSpeed: XYChart.Series[Number, Number] = _
  var carBackSpeed: XYChart.Series[Number, Number] = _

  var carFrontForce: XYChart.Series[Number, Number] = _
  var carBackForce: XYChart.Series[Number, Number] = _

  var carFrontAcc: XYChart.Series[Number, Number] = _
  var carBackAcc: XYChart.Series[Number, Number] = _

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
    initForceChart
    initAccChart

    initDefuzzyChart
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
    carFrontPosition = new XYChart.Series[Number, Number]()
    carBackPosition = new XYChart.Series[Number, Number]()

    positionChart.getData.addAll(carFrontPosition, carBackPosition)
    posChartTab.setContent(positionChart)
  }

  def initForceChart: Unit ={
    val xAxisPositionChart  = new NumberAxis()
    val yAxisPositionChart = new NumberAxis()

    val forceChart = new AreaChart[Number, Number](xAxisPositionChart, yAxisPositionChart)
    carFrontForce = new XYChart.Series[Number, Number]()
    carBackForce = new XYChart.Series[Number, Number]()

    forceChart.getData.addAll(carFrontForce, carBackForce)
    forceChartTab.setContent(forceChart)
  }

  def initAccChart: Unit ={
    val xAxisPositionChart  = new NumberAxis()
    val yAxisPositionChart = new NumberAxis()

    val accChart = new AreaChart[Number, Number](xAxisPositionChart, yAxisPositionChart)
    carFrontAcc = new XYChart.Series[Number, Number]()
    carBackAcc = new XYChart.Series[Number, Number]()

    accChart.getData.addAll(carFrontAcc, carBackAcc)
    accChartTab.setContent(accChart)
  }

  def addDataToGraph(carFront: Car, carBack: Car, tick: Int): Unit = {
    carFrontSpeed.getData().add(new XYChart.Data(tick, carFront.speed));
    carBackSpeed.getData().add(new XYChart.Data(tick, carBack.speed));

    carFrontPosition.getData().add(new XYChart.Data(tick, carFront.position));
    carBackPosition.getData().add(new XYChart.Data(tick, carBack.position));

    carFrontForce.getData().add(new XYChart.Data(tick, carFront.engineForce));
    carBackForce.getData().add(new XYChart.Data(tick, carBack.engineForce));

    carFrontAcc.getData().add(new XYChart.Data(tick, carFront.acceleration));
    carBackAcc.getData().add(new XYChart.Data(tick, carBack.acceleration));
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
    carFrontPosition.getData.clear
    carBackPosition.getData.clear
    carFrontForce.getData.clear
    carBackForce.getData.clear
    carFrontAcc.getData.clear
    carBackAcc.getData.clear
  }

  val tickUnit = (min:Int, max:Int, maxTicks: Int) => (Math.abs(min - max) / maxTicks).toInt match {
    case x if x == 0 => 1
    case x => x
  }

  def plotDefuzzy(func: ((Double) => FuzzyBool)): Unit ={
    plot.getData.clear()
    var dataSeries = new XYChart.Series[Number, Number]()

    for(x <- (forceOutput.minVal to forceOutput.maxVal by tickUnit(forceOutput.minVal, forceOutput.maxVal, 500)))
      dataSeries.getData().add(new XYChart.Data(x, func(x).value))
    plot.getData.add(dataSeries)

  }

  def initDefuzzyChart: Unit ={
    val xAxisPositionChart  = new NumberAxis()
    xAxisPositionChart.setLowerBound(forceOutput.minVal)
    xAxisPositionChart.setUpperBound(forceOutput.maxVal)
    xAxisPositionChart.setTickUnit(tickUnit(forceOutput.minVal, forceOutput.maxVal, 50))
    xAxisPositionChart.setAutoRanging(false)

    val yAxisPositionChart = new NumberAxis()
    yAxisPositionChart.setLowerBound(Fuzzy.MIN_VALUE)
    yAxisPositionChart.setUpperBound(Fuzzy.MAX_VALUE)
    yAxisPositionChart.setAutoRanging(false)

    plot = new LineChart[Number, Number](xAxisPositionChart, yAxisPositionChart)
    plot.setAnimated(false)

    split.getItems.add(plot)
  }

  def onReset(event: ActionEvent): Unit ={
    BackgroundThread.fuzzyThread.interrupt()
  }
}

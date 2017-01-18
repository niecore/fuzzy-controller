package presentation.memberFunctionEditor

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Node
import javafx.scene.chart.{AreaChart, LineChart, NumberAxis, XYChart}
import javafx.scene.control.{ComboBox, ListView, TextField}
import javafx.scene.layout.BorderPane

import model.fuzzyModel.MembershipFunctions
import model.fuzzyModel.entity._

import scala.util.Try

/**
  * Created by joel on 17.01.17.
  */
class MemberFunctionEditorPresenter extends Initializable{

  var config: FuzzyConfig = _
  var valueConnector: FuzzyValueConnector = _
  var plot: LineChart[Number, Number] = _

  @FXML
  var termsList: ListView[FuzzyTerm] = _

  @FXML
  var rootPane: BorderPane = _

  @FXML
  var tfName: TextField = _
  @FXML
  var tfleft: TextField = _
  @FXML
  var tfMid: TextField = _
  @FXML
  var tfRight: TextField = _

  @FXML
  var functionsComboBox: ComboBox[String] = _

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    functionsComboBox.getItems.add("Triangle")

  }

  val tickUnit = (min:Int, max:Int, maxTicks: Int) => (Math.abs(min - max) / maxTicks).toInt match {
    case x if x == 0 => 1
    case x => x
  }

  def initData(cfg: FuzzyConfig, fvc: FuzzyValueConnector) = {
    config = cfg
    println("size of " + fvc.name + " " + config.filterFuzzyTerms(fvc).size)
    config.filterFuzzyTerms(fvc).foreach(ft => termsList.getItems.add(ft))
    valueConnector = fvc
    initSpeedChart
    val term = new FuzzyTerm("isVeryClose", valueConnector, (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, 100, Some(200))))
    termsList.getSelectionModel.selectedItemProperty().addListener((o, ol, n) => {
      plotMemberFunction(n)
    })
  }

  def initSpeedChart: Unit ={
    val xAxisPositionChart  = new NumberAxis()
    xAxisPositionChart.setLowerBound(valueConnector.minVal)
    xAxisPositionChart.setUpperBound(valueConnector.maxVal)
    xAxisPositionChart.setTickUnit(tickUnit(valueConnector.minVal, valueConnector.maxVal, 50))
    xAxisPositionChart.setAutoRanging(false)

    val yAxisPositionChart = new NumberAxis()
    yAxisPositionChart.setLowerBound(Fuzzy.MIN_VALUE)
    yAxisPositionChart.setUpperBound(Fuzzy.MAX_VALUE)
    yAxisPositionChart.setAutoRanging(false)

    plot = new LineChart[Number, Number](xAxisPositionChart, yAxisPositionChart)

    rootPane.setTop(plot)
  }

  def plotMemberFunction(fuzzyTerm: FuzzyTerm): Unit ={
    plot.getData.clear()
    var dataSeries = new XYChart.Series[Number, Number]()

    for(x <- (valueConnector.minVal to valueConnector.maxVal by tickUnit(valueConnector.minVal, valueConnector.maxVal, 500)))
      dataSeries.getData().add(new XYChart.Data(x, fuzzyTerm.func(x).value))
    plot.getData.add(dataSeries)

  }

  def addMemberFunction(event: ActionEvent): Unit ={
    val left = Try(tfleft.getText.toDouble).toOption
    val mid = tfMid.getText.toDouble
    val right = Try(tfRight.getText.toDouble).toOption

    val func = (x: Double) => new FuzzyBool(MembershipFunctions.triangle(x, left, mid, right))
    termsList.getItems.add(FuzzyTerm(tfName.getText, valueConnector, func))
  }

  def deleteMemberFunction(event: ActionEvent): Unit ={
    termsList.getItems.remove(termsList.getSelectionModel.getSelectedItem)
  }

  val close = (e: ActionEvent) => e.getSource match{
    case n:Node => n.getScene.getWindow.hide()
  }

  def onAbort(event: ActionEvent): Unit ={
    close(event)
  }
  def onOk(event: ActionEvent): Unit ={
    config.fuzzyTerms = config.fuzzyTerms.filterNot(ft => config.filterFuzzyTerms(valueConnector) contains  ft)
    termsList.getItems.forEach(ft => config.fuzzyTerms = ft +: config.fuzzyTerms)
    close(event)
  }
}

package presentation.ruleEditor

import java.net.URL
import java.util.ResourceBundle
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Node
import javafx.scene.control.ListView

import model.fuzzyModel.MembershipFunctions
import model.fuzzyModel.entity._
import presentation.mainView.MainPresenter

import scala.util.Try

/**
  * Created by joel on 17.01.17.
  */
class RuleEditorPresenter extends Initializable{

  var config: FuzzyConfig = _

  val distanceInput = new FuzzyValueConnector("Distance", 0, 500, true)
  val speedInput = new FuzzyValueConnector("Speed", 0, 250, true)
  val forceOutput = new FuzzyValueConnector("Force", -8000, 4000, false)

  var distanceTerms: List[FuzzyTerm] = _
  var speedTerms: List[FuzzyTerm] = _
  var forceTerms: List[FuzzyTerm] = _

  @FXML
  var listRules: ListView[FuzzyRule] = _

  @FXML
  var listViewSpeed: ListView[String] = _

  @FXML
  var listViewDistance: ListView[String] = _

  @FXML
  var listViewForce: ListView[String] = _

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {

  }

  def initData(cfg: FuzzyConfig): Unit ={
    config = cfg
    distanceTerms = config.filterFuzzyTerms(distanceInput)
    speedTerms = config.filterFuzzyTerms(speedInput)
    forceTerms = config.filterFuzzyTerms(forceOutput)

    distanceTerms.foreach(tf => listViewDistance.getItems.add(tf.name))
    speedTerms.foreach(tf => listViewSpeed.getItems.add(tf.name))
    forceTerms.foreach(tf => listViewForce.getItems.add(tf.name))

    (listViewDistance :: listViewSpeed :: Nil).foreach(lv => lv.getItems.add("none"))

    config.rules.foreach(r => listRules.getItems.add(r))
  }

  def findMf(listView: ListView[String], fuzzyTerms: List[FuzzyTerm]): List[FuzzyTerm] = listView.getSelectionModel.getSelectedItem match {
    case "none" => Nil
    case s:String => fuzzyTerms.find(ft => ft.name == s).get :: Nil
  }

  def addRule(event: ActionEvent): Unit ={
    val mfDistance = findMf(listViewDistance, distanceTerms)
    val mfSpeed = findMf(listViewSpeed, speedTerms)
    val mfForce :: _ = findMf(listViewForce, forceTerms)

    listRules.getItems.add(new FuzzyRule("name", mfDistance ++ mfSpeed, mfForce))
  }

  def deleteRule(event: ActionEvent): Unit ={
    listRules.getItems.remove(listRules.getSelectionModel.getSelectedItem)
  }

  val close = (event: ActionEvent) => event.getSource match {
    case n:Node => n.getScene.getWindow.hide()
  }

  def onAbort(event: ActionEvent): Unit ={
    close(event)
  }

  def onOk(event: ActionEvent): Unit ={
    config.rules = listRules.getItems.toArray().toList match {
      case l: List[FuzzyRule] => l
    }
    close(event)
  }
}

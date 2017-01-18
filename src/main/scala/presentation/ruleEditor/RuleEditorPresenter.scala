package presentation.ruleEditor

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Node
import javafx.scene.control.ListView

import model.fuzzyModel.entity.{FuzzyConfig, FuzzyRule, FuzzyTerm, FuzzyValueConnector}
import presentation.mainView.MainPresenter

/**
  * Created by joel on 17.01.17.
  */
class RuleEditorPresenter extends Initializable{

  var config: FuzzyConfig = _

  val distanceInput = new FuzzyValueConnector("Distance", 0, 500, true)
  val speedInput = new FuzzyValueConnector("Speed", 0, 250, true)
  val forceOutput = new FuzzyValueConnector("Force", -8000, 4000, false)

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
    config.filterFuzzyTerms(distanceInput).foreach(tf => listViewDistance.getItems.add(tf.name))
    config.filterFuzzyTerms(speedInput).foreach(tf => listViewSpeed.getItems.add(tf.name))
    config.filterFuzzyTerms(forceOutput).foreach(tf => listViewForce.getItems.add(tf.name))

    config.rules.foreach(r => listRules.getItems.add(r))
  }

  val close = (event: ActionEvent) => event.getSource match {
    case n:Node => n.getScene.getWindow.hide()
  }

  def onAbort(event: ActionEvent): Unit ={
    close(event)
  }

  def onOk(event: ActionEvent): Unit ={
    close(event)
  }
}

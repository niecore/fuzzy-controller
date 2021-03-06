package presentation.configEditor

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control.{Button, ComboBox}
import javafx.scene.{Node, Scene}
import javafx.stage.{Modality, Stage}

import model.{BackgroundThread, Fuzzy}
import model.fuzzyModel.entity.{DefuzzyficationFunctions, FuzzyConfig, FuzzyValueConnector}
import presentation.mainView.MainPresenter
import presentation.memberFunctionEditor.MemberFunctionEditorPresenter
import presentation.ruleEditor.RuleEditorPresenter

/**
  * Created by joel on 17.01.17.
  */
class ConfigEditorPresenter extends Initializable{
  var config: FuzzyConfig = _
  var mainPresenter: MainPresenter = _

  @FXML
  var defuzzyAlgo: ComboBox[String] = _

  val distanceInput = new FuzzyValueConnector("Distance", 0, 500, true)
  val speedInput = new FuzzyValueConnector("Speed", 0, 250, true)
  val forceOutput = new FuzzyValueConnector("Force", -8000, 4000, false)

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {

  }

  def initData(cfg: FuzzyConfig, mp: MainPresenter): Unit ={
    config = cfg
    mainPresenter = mp
    DefuzzyficationFunctions.functionList.foreach(dfa => defuzzyAlgo.getItems.add(dfa.name))
    defuzzyAlgo.getSelectionModel.select(config.defuzzy.name)
  }

  def openMfEditor(event: ActionEvent, valueConnector: FuzzyValueConnector): Unit ={
    val loader = new FXMLLoader(getClass.getResource("/memberFunctionEditor.fxml"))

    val stage = new Stage()
    stage.setScene(new Scene(loader.load()))

    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(event.getSource match {
      case e: Node => e.getScene.getWindow
    })
    val controller = loader.getController[MemberFunctionEditorPresenter]
    controller.initData(config, valueConnector)

    stage.setTitle(valueConnector.name)
    stage.showAndWait()
    config = controller.config
    println(s"after: $config")
  }

  def openSpeedMfEditor(event: ActionEvent): Unit = {
    openMfEditor(event, speedInput)
  }

  def openDistanceMfEditor(event: ActionEvent): Unit = {
    openMfEditor(event, distanceInput)
  }

  def openForceMfEditor(event: ActionEvent): Unit = {
    openMfEditor(event, forceOutput)
  }

  val close = (event: ActionEvent) => event.getSource match {
    case n:Node => n.getScene.getWindow.hide()
  }

  def go(event: ActionEvent): Unit = {
    close(event)
    config.defuzzy = DefuzzyficationFunctions.functionList.find({
      p => p.name == defuzzyAlgo.getSelectionModel.getSelectedItem
    }).get
    println(s"after: $config")
    BackgroundThread.startNewThread(config)
  }
  def abort(event: ActionEvent): Unit = {
    close(event)
  }

  def openRuleEditor(event: ActionEvent): Unit ={
    val loader = new FXMLLoader(getClass.getResource("/ruleEditor.fxml"))

    val stage = new Stage()
    stage.setScene(new Scene(loader.load()))

    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(event.getSource match {
      case e: Node => e.getScene.getWindow
    })
    val ruleEditorPresenter = loader.getController[RuleEditorPresenter]
    ruleEditorPresenter.initData(config)

    stage.showAndWait()
    config = ruleEditorPresenter.config
    println(s"after: $config")
  }
}

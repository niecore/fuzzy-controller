package presentation.configEditor

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.{FXMLLoader, Initializable}
import javafx.scene.{Node, Scene}
import javafx.stage.{Modality, Stage}

import model.fuzzyModel.entity.{FuzzyConfig, FuzzyValueConnector}
import presentation.mainView.MainPresenter
import presentation.memberFunctionEditor.MemberFunctionEditorPresenter

/**
  * Created by joel on 17.01.17.
  */
class ConfigEditorPresenter extends Initializable{
  var config: FuzzyConfig = _
  var mainPresenter: MainPresenter = _

  val distanceInput = new FuzzyValueConnector("Distance", 0, 300, true)
  val speedInput = new FuzzyValueConnector("Speed", 0, 250, true)
  val forceOutput = new FuzzyValueConnector("Force", -8000, 8000, false)

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {

  }

  def initData(cfg: FuzzyConfig, mp: MainPresenter): Unit ={
    config = cfg
    mainPresenter = mp
  }

  def openSpeedMfEditor(event: ActionEvent): Unit = {
    val loader = new FXMLLoader(getClass.getResource("../memberFunctionEditor/memberFunctionEditor.fxml"))

    val stage = new Stage()
    stage.setScene(new Scene(loader.load()))

    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(event.getSource match {
      case e: Node => e.getScene.getWindow
    })
    val controller = loader.getController[MemberFunctionEditorPresenter]
    controller.initData(config, speedInput)
    println(s"before: $config")
    stage.showAndWait()
    config = controller.config
    println(s"after: $config")
  }

  def openDistanceMfEditor: Unit = {

  }

  def openForceMfEditor: Unit = {

  }

  def go: Unit = {

  }
}

package presentation.configEditor

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXMLLoader, Initializable}
import javafx.scene.Scene
import javafx.stage.Stage

import model.fuzzyModel.entity.FuzzyConfig
import presentation.mainView.MainPresenter
import presentation.memberFunctionEditor.MemberFunctionEditorPresenter

/**
  * Created by joel on 17.01.17.
  */
class ConfigEditorPresenter extends Initializable{
  var config: FuzzyConfig = _
  var mainPresenter: MainPresenter = _

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {

  }

  def initData(cfg: FuzzyConfig, mp: MainPresenter): Unit ={
    config = cfg
    mainPresenter = mp
  }

  def openSpeedMfEditor: Unit = {
    val loader = new FXMLLoader(getClass.getResource("../memberFunctionEditor/memberFunctionEditor.fxml"))
    val stage = new Stage()
    stage.setScene(new Scene(loader.load()))
    stage.show()
    loader.getController[MemberFunctionEditorPresenter].initData(config)
  }

  def openDistanceMfEditor: Unit = {

  }

  def openForceMfEditor: Unit = {

  }

  def go: Unit = {

  }
}

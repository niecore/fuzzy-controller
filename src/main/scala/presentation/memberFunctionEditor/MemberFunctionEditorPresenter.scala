package presentation.memberFunctionEditor

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Node
import javafx.scene.control.{ComboBox, ListView, TextField}

import model.fuzzyModel.MembershipFunctions
import model.fuzzyModel.entity.{FuzzyBool, FuzzyConfig, FuzzyTerm, FuzzyValueConnector}

import scala.util.Try

/**
  * Created by joel on 17.01.17.
  */
class MemberFunctionEditorPresenter extends Initializable{

  var config: FuzzyConfig = _
  var valueConnector: FuzzyValueConnector = _

  @FXML
  var termsList: ListView[FuzzyTerm] = _

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

  def initData(cfg: FuzzyConfig, fvc: FuzzyValueConnector) = {
    config = cfg
    println("size of " + fvc.name + " " + config.filterFuzzyTerms(fvc).size)
    config.filterFuzzyTerms(fvc).foreach(ft => termsList.getItems.add(ft))
    valueConnector = fvc
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

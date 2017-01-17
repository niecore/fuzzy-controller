package presentation.memberFunctionEditor

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.Initializable

import model.fuzzyModel.entity.FuzzyConfig

/**
  * Created by joel on 17.01.17.
  */
class MemberFunctionEditorPresenter extends Initializable{
  var config: FuzzyConfig = _

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {

  }

  def initData(cfg: FuzzyConfig): Unit ={
    config = cfg
  }
}

package model

import javafx.application.Platform

import model.fuzzyModel.FuzzyCarController
import model.physicalModel.Car
import presentation.mainView.MainPresenter

/**
  * Created by joel on 17.01.17.
  */
class Fuzzy(carFront: Car, carBack:Car, controller: FuzzyCarController, mainPresenter: MainPresenter) extends Runnable {



  override def run(): Unit = {
    while(true){
      carFront.engineForce = mainPresenter.newton1.get()
      Platform.runLater(() => {
        mainPresenter.acc1.set(carFront.acceleration)
        mainPresenter.speed1.set(carFront.speedKmh)
        mainPresenter.pos1.set(carFront.position)

        mainPresenter.newton2.set(carBack.engineForce)
        mainPresenter.acc2.set(carBack.acceleration)
        mainPresenter.speed2.set(carBack.speedKmh)
        mainPresenter.pos2.set(carBack.position)

        mainPresenter.dist.set(carFront.position - carBack.position)
      })
      controller.tick()
      Thread.sleep(100)
    }
  }
}

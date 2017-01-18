package model

import javafx.application.Platform

import model.fuzzyModel.entity.FuzzyConfig
import model.fuzzyModel.{DefaultConfig, FuzzyCarController}
import model.physicalModel.Car
import presentation.mainView.MainPresenter

/**
  * Created by joel on 17.01.17.
  */
class Fuzzy(config: FuzzyConfig) extends Runnable {

  val carFront, carBack = new Car
  val controller = new FuzzyCarController(config, carBack, carFront)
  carFront.position = 300;
  val mainPresenter = BackgroundThread.mainPresenter

  var tick = 0

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
        mainPresenter.addnewPositionsToGraph(carFront.position, carBack.position, tick)
        mainPresenter.addnewSpeedToGraph(carFront.speed, carBack.speed, tick)
      })
      controller.tick()
      tick += 1
      Thread.sleep(100)
    }
  }
}

object BackgroundThread{
  def startNewThread(config: FuzzyConfig): Unit ={
    if(fuzzyThread == null || fuzzyThread.isAlive == false){
      println("jo neuer thread")
      mainPresenter.resetCharts
      fuzzyThread = new Thread(new Fuzzy(config))
      fuzzyThread start
    }
  }

  var fuzzyThread: Thread = _
  var mainPresenter: MainPresenter = _
}

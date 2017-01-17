import model.physicalModel.Car
import model.fuzzyModel.{DefaultConfig, FuzzyCarController}

/**
  * Created by nico on 12.01.17.
  */

object Main extends App {

  val carFront, carBack = new Car
  val controller = new FuzzyCarController(DefaultConfig, carBack, carFront)

  carFront.engineForce = 6000;

  while(true){
    if(carFront.speed >= 28)
      carFront.engineForce = 0
    controller.tick()
    Thread.sleep(100)
    println("----------------------------------------------------------------------")
  }
}

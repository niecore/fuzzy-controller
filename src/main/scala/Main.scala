import service.physicalModel.Car
import service.fuzzyModel.{FuzzyCarController, FuzzyKnowledgeBaseCar}

/**
  * Created by nico on 12.01.17.
  */

object Main extends App {


  val carFront, carBack = new Car
  val controller = new FuzzyCarController(FuzzyKnowledgeBaseCar, carBack, carFront)

  carFront.engineForce = 6000;

  while(true){
    if(carFront.speed >= 28)
      carFront.engineForce = 0
    carFront.tick()
    //carBack.tick()
    controller.tick()
    Thread.sleep(100)
  }
}

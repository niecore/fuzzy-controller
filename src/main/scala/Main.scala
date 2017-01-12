import rx.lang.scala.Observable

import scala.concurrent.duration.Duration
import scala.language.postfixOps
/**
  * Created by nico on 12.01.17.
  */

object Main extends App {
  var o = Observable.just(10, 11, 12)
  o.subscribe(n => println("n = " + n))
  Observable.just(1, 2, 3, 4).reduce(_ + _)
}

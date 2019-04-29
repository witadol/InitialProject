import scala.annotation.tailrec
import scala.concurrent.duration._

object initial {

  def main(args: Array[String]): Unit = {
    var result = retry[Int](
      block = () => 1+1,
      acceptResult = res => res%2 == 0,
      retries = List(0.seconds, 1.seconds, 2.seconds)
    )
    println(result)
  }

  @tailrec
  def retry[A](block: () => A, acceptResult: A => Boolean, retries: List[FiniteDuration]): A = {

      val blockerResult = block()
      if (retries.isEmpty || acceptResult(blockerResult)) {
        blockerResult
      } else {
        Thread.sleep(retries.head.toMillis)
        retry(block, acceptResult, retries.tail)
      }
  }
}

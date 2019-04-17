import scala.annotation.tailrec
import scala.concurrent.duration._

object initial {

  def main(args: Array[String]): Unit = {
    retry[Int](
      block = () => 1+1,
      acceptResult = res => res%2 == 0,
      retries = List(0.seconds, 1.seconds, 2.seconds)
    )
  }

  def retry[A](block: () => A, acceptResult: A => Boolean, retries: List[FiniteDuration]): A = {
    @tailrec
    def retryT(block: () => A, acceptResult: A => Boolean, retries: List[FiniteDuration]): A = {
      val blockerResult = block()
      if (retries.isEmpty || acceptResult(blockerResult)) {
        blockerResult
      } else {
        Thread.sleep(retries.head.toMillis)
        retryT(block, acceptResult, retries.tail)
      }
    }
    retryT(block=block, acceptResult=acceptResult, retries=retries)
  }
}


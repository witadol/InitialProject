import org.scalatest._
import initial._
import scala.concurrent.duration._


class ProjectTests extends FunSuite with DiagrammedAssertions {
  test("Empty retry list") {
    val expected = 2
    val result = retry[Int](
      block = () => 1+1,
      acceptResult = res => res%2 == 0,
      retries = List()
    )
    assert(expected == result)
  }

  test("Really big retry list") {
    val expected = 2
    val result = retry[Int](
      block = () => 1+1,
      acceptResult = res => false,
      retries = List.tabulate(1000000)(n => 0.seconds)
    )
    assert(expected == result)
  }

  test("Interrupt after first attempt") {
    val expected = 2
    val result = retry[Int](
      block = () => 1+1,
      acceptResult = res => res%2 == 0,
      retries = List(0.seconds, 1.seconds, 2.seconds)
    )
    assert(expected == result)
  }
}
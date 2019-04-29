
import java.time.Duration
import java.time.LocalDateTime

import org.scalatest._
import initial._
import scala.concurrent.duration._


class RetryTests extends FunSuite with DiagrammedAssertions {
  test("Empty retry list") {
    val expected = 2
    val result = retry[Int](
      block = () => 1+1,
      acceptResult = res => res%2 == 0,
      retries = List()
    )
    assert(expected == result)
  }

  test("Interrupt after first attempt") {
    val attemptNumber = 1
    val expected = 1

    var attempt = 0
    val result = retry[Int](
      block = () => {attempt += 1; attempt},
      acceptResult = res => res == attemptNumber,
      retries = List(0.seconds, 1.seconds, 2.seconds)
    )
    assert(expected == result)
  }

  test("Interrupt after second attempt") {
    val attemptNumber = 2
    val expected = 2

    var attempt = 0
    val result = retry[Int](
      block = () => {attempt += 1; attempt},
      acceptResult = res => res == attemptNumber,
      retries = List(0.seconds, 1.seconds, 2.seconds)
    )
    assert(expected == result)
  }

  test("Really big retry list") {
    val attemptNumber = 1000000
    val expected = 1000000

    var attempt = 0
    val result = retry[Int](
      block = () => {attempt += 1; attempt},
      acceptResult = res => res == attemptNumber,
      retries = List.tabulate(attemptNumber)(n => 0.seconds)
    )
    assert(expected == result)
  }

  test("Time profiler") {

    val expected = 6

    val start = LocalDateTime.now
    val result = retry[Int](
      block = () => 2 + 2,
      acceptResult = res => res == 5,
      retries = List(1.seconds, 2.seconds, 3.seconds)
    )
    val end = LocalDateTime.now

    val attemptsDuration = Duration.between(start, end).toSeconds
    assert(expected <= attemptsDuration)

  }
}

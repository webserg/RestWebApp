package webserg.test.testing

import akka.testkit.{TestActorRef, TestKit}
import akka.actor.ActorSystem
import org.scalatest.{Suite, BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.util.Timeout
import scala.util.Success
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import webserg.test.{ Reading, Writer, PostValues}
import scala.concurrent.Await

class ActorTest extends TestKit(ActorSystem("testsystem"))  with WordSpecLike with Matchers with StopSystemAfterAll {

  "Reading Actor" must {
    "change state when it receives a message, single threaded" in {
      val reading = TestActorRef[Reading]
      implicit val timeout = Timeout(5 seconds)
      val future = reading ? 0
      val Success(result: Int) = future.value.get
      result should be(1)
    }
    "change state when it receives a message, multi-threaded" in {

    }
  }

  "Writing Actor" must {
    "should save result and return ack" in {
      val writer = TestActorRef[Writer]
      implicit val timeout = Timeout(5 seconds)
      val future = writer ? new PostValues(0,0,0)
      val Success(result: Int) = future.value.get
      result should be(0)
    }
  }

}

trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>
  override protected def afterAll() {
    super.afterAll()
    system.shutdown()
  }
}

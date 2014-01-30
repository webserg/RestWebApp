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

class ActorTest extends TestKit(ActorSystem("testsystem"))  with WordSpecLike with Matchers with StopSystemAfterAll {

  "Reading Actor" must {
    "change state when it receives a message, single threaded" in {
      val silentActor = TestActorRef[Reading]
      implicit val timeout = Timeout(15 seconds)
      val future = silentActor ? 1
      val Success(result: Int) = future.value.get
      result should be(3)
    }
    "change state when it receives a message, multi-threaded" in {

    }
  }

  "Writing Actor" must {
    "should save result and return ack" in {
      val writer = TestActorRef[Writer]
      implicit val timeout = Timeout(15 seconds)
      val future = writer ? new PostValues(1,1,1)
      val Success(result: Int) = future.value.get
      result should be(1)
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

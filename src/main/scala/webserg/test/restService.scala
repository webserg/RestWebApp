package webserg.test

import javax.ws.rs._
import akka.actor._
import scala.concurrent.Await
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global
import javax.ws.rs.core.Context
import akka.routing.{Broadcast, RoundRobinRouter}
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import java.io._
import akka.routing.Broadcast
import scala.util.{Failure, Success}
import org.junit
import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import org.scalatest.{WordSpec, WordSpecLike, Matchers, BeforeAndAfterAll}
import org.scalatest.matchers.{Matcher, MustMatchers}

@Path("/helloactor")
class RestService {

  val readingRouter = WebRunner.system.actorSelection("/user/ReadingRobinRouter")
  val writer = WebRunner.system.actorSelection("/user/Writer")

  @GET
  def hello(@QueryParam("v1") v1: String) = {
    implicit val timeout = Timeout(15 seconds)
    val future = readingRouter ? v1.toInt
    val result = Await.result(future, timeout.duration)
    <result>{result}</result>.toString()
  }

  @POST
  @Path("/post")
  def hello(@QueryParam("v2") v2: String, @QueryParam("v3") v3: String, @QueryParam("v4") v4: String) = {
    implicit val timeout = Timeout(15 seconds)
    val future = writer ? new PostValues(v2.toInt, v3.toInt, v4.toInt)
    val result = Await.result(future, timeout.duration)
    <result>{result}</result>.toString()
    //    future onComplete {
    //      case Success(result) => <result>result</result>.toString()
    //      case Failure(t) => <result>0</result>.toString()
    //    }
  }

}

class Reading extends Actor with ActorLogging {
  var f2 = WebRunner.getDataFromF2.map(_.toInt)

  def receive = {
    case v1: Int => {
      log.info(self.path.name)
      val v = f2(v1)
      val res = if (v > 10) v - 10 else v
      sender ! res
    }
    case Update => f2 = WebRunner.getDataFromF2.map(_.toInt)
  }
}

case object Update

case class PostValues(v2: Int, v3: Int, v4: Int)

class Writer extends Actor with ActorLogging {
  var f1 = Source.fromFile(WebRunner.f1Name).getLines.flatMap(_.split(",")).toArray.map(_.toInt)
  var f2 = WebRunner.getDataFromF2
  val readingRouter = WebRunner.system.actorSelection("/user/ReadingRobinRouter")

  def receive = {
    case p: PostValues =>
      log.info(self.path.name)
      val res = if ((f1(p.v3) + p.v2) < 10) f1(p.v3) + p.v2 + 10 else f1(p.v3) + p.v2
      f2(p.v4) = res.toString
      log.info(res.toString)
      setDataToFile(WebRunner.f2Name, f2)
      readingRouter ! Broadcast(Update)
      sender ! 1


  }

  def setDataToFile(filename: String, data: Array[String]) {
    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(data.mkString(","))
    bw.close()
  }
}




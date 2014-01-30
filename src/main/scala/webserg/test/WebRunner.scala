package webserg.test

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.servlet.ServletException

import java.io.IOException

import org.eclipse.jetty.server.{Server, Request}
import org.eclipse.jetty.server.handler.{HandlerList, AbstractHandler}
import org.eclipse.jetty.servlet.{ServletHolder, ServletContextHandler}
import com.sun.jersey.spi.container.servlet.{ServletContainer => JettyServletContainer}

import javax.ws.rs._
import akka.actor._
import scala.concurrent.Await

import scala.io.Source
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import javax.ws.rs.core.Context
import akka.routing.{Broadcast, RoundRobinRouter}

import scala.util.{Failure, Success}
import org.junit
import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import org.scalatest.{WordSpec, WordSpecLike, Matchers, BeforeAndAfterAll}
import org.scalatest.matchers.{Matcher, MustMatchers}

object WebRunner {

  implicit val system = ActorSystem("System")
  val f1Name = "c://devel//csv1.csv"
  val f2Name = "c://devel//csv2.csv"

  def main(args: Array[String]) {

    val server = new Server(8080)

    val context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS)
    context.setContextPath("/")

    val holder: ServletHolder = new ServletHolder(classOf[JettyServletContainer])
    holder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass",
      "com.sun.jersey.api.core.PackagesResourceConfig")
    holder.setInitParameter("com.sun.jersey.config.property.packages", "webserg.test")
    context.addServlet(holder, "/*")
    val handlers = new HandlerList()
    handlers.setHandlers(Array(context))
    server.setHandler(handlers)
    val reading1 = system.actorOf(Props[Reading])
    val reading2 = system.actorOf(Props[Reading])
    val routees = Vector[ActorRef](reading1, reading2)
    system.actorOf(Props.empty.withRouter(RoundRobinRouter(routees = routees)), "ReadingRobinRouter")
    system.actorOf(Props[Writer], "Writer")
    server.start
    server.join
    system.shutdown()
  }

  val getDataFromF2: Array[String] = Source.fromFile(WebRunner.f2Name).getLines.flatMap(_.split(",")).toArray
}


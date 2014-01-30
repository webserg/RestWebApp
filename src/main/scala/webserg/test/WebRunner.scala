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
import org.eclipse.jetty.util.thread.QueuedThreadPool
import java.nio.file
import java.nio.file.Paths


object WebRunner {

  implicit val system = ActorSystem("System")
  val f1Name = "csv1.csv"
  val f2Name = "csv2.csv"

  def main(args: Array[String]) {
    println(Paths.get(".").toAbsolutePath)
    val server = new Server(8080)
    val pool = new QueuedThreadPool()
    server.addBean(pool)

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
}


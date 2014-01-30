package webserg.test.testing


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource
import org.junit
;

class IntegrationTest {

  @junit.Test
  def testGet() = {
    val client = Client.create()
    val webResource = client.resource("http://localhost:8080/helloactor?v1=1")
    val response = webResource.accept("application/xml").get(classOf[ClientResponse])
    if (response.getStatus != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
        + response.getStatus)
    }
    //    String output = response.getEntity(String.class);
    val output = response.getEntity(classOf[String])
    System.out.println("Output xml client .... \n")
    System.out.println(output)
    junit.Assert.assertEquals("<result>3</result>", output.trim)
  }

  @junit.Test
  def testPost() = {
    val client = Client.create()
    val webResource = client.resource("http://localhost:8080/helloactor/post?v2=1&v3=1&v4=1")
    val response = webResource.accept("application/xml").post(classOf[ClientResponse])
    if (response.getStatus != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
        + response.getStatus)
    }
    //    String output = response.getEntity(String.class);
    val output = response.getEntity(classOf[String])
    System.out.println("Output xml client .... \n")
    System.out.println(output)
    junit.Assert.assertEquals("<result>1</result>", output)
  }
}





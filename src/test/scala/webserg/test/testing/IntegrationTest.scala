package webserg.test.testing


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource
import org.junit
;

class IntegrationTest {

  @junit.Test
  def testPost() = {
    val client = Client.create()
    val webResource = client.resource("http://localhost:8080/helloactor/post?v2=2&v3=2&v4=2")
    val response = webResource.accept("application/xml").post(classOf[ClientResponse])
    if (response.getStatus != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
        + response.getStatus)
    }
    //    String output = response.getEntity(String.class);
    val output = response.getEntity(classOf[String])
    System.out.println("Output xml client .... \n")
    System.out.println(output)
    junit.Assert.assertEquals("<result>0</result>", output)
  }

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
    junit.Assert.assertEquals("<result>2</result>", output.trim)
  }
}





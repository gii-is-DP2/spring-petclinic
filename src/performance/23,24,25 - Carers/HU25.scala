package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU25 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.png""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map(
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "*/*",
		"User-Agent" -> "python-requests/2.7.0 CPython/2.7.13 Windows/10",
		"content-type" -> "application/json")

	val headers_3 = Map(
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

    val uri2 = "http://192.168.0.12:8008/ssdp/device-desc.xml"

	object Home {
		var home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(16)
	}

	object Login {
    val login = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(16)
    .exec(
      http("Logged")
        .post("/login")
        .headers(headers_3)
        .formParam("username", "admin")
        .formParam("password", "admin")        
        .formParam("_csrf", "${stoken}")
    ).pause(13)
  }

  object ListCarers {
	  var listCarers = exec(http("ListCarers")
			.get("/carers")
			.headers(headers_0))
		.pause(17)
  }

  object DeleteCarer {
	  var deleteCarer = exec(http("delete")
			.get("/carers/3/delete")
			.headers(headers_0))
		.pause(10)
  }

  object ShowCarer {
	  val showCarer = exec(http("show")
			.get("/carers/2")
			.headers(headers_0))
		.pause(7)
  }

   object AddCarer {
	  var addCarer = exec(http("addCarer")
	  		.get("/carers/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
   		 ).pause(16)
		.exec(http("createdCarer")
			.post("/carers/new")
			.headers(headers_3)
			.formParam("firstName", "manuel")
			.formParam("lastName", "ortiz")
			.formParam("salary", "20.0")
			.formParam("dni", "123123123")
			.formParam("telephone", "123123123")
			.formParam("email", "mobla@us.es")
			.formParam("isHairdresser", "Yes")
			.formParam("_csrf", "${stoken}")
			).pause(36)
  }
	
	val deleteScn = scenario("ListAddDelete").exec(Home.home,
									  Login.login,
									  ListCarers.listCarers,
									  AddCarer.addCarer,
									  DeleteCarer.deleteCarer
									  )
	val showDeleteScn = scenario("List").exec(Home.home,
									  Login.login,
									  ListCarers.listCarers,
									  )


	setUp(
		deleteScn.inject(rampUsers(2000) during (60 seconds)),
		showDeleteScn.inject(rampUsers(2000) during (60 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}
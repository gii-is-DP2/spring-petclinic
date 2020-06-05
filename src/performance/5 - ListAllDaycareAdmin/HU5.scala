package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU5 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.png""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

    val uri2 = "http://cdn.content.prod.cms.msn.com/singletile/summary/alias/experiencebyname/today"

	object Home {
		var home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(11)
	}

	object Login {
    val login = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(13)
    .exec(
      http("Logged")
        .post("/login")
        .headers(headers_0)
        .formParam("username", "admin")
        .formParam("password", "admin")        
        .formParam("_csrf", "${stoken}")
    ).pause(11)
  }

  object ListDaycares {
	  var listDaycares  = exec(http("ListDaycares")
			.get("/daycares")
			.headers(headers_0))
		.pause(17)
  }

  object ShowDaycare {
	  var showDaycare = exec(http("ShowDaycare")
			.get("/daycares/1")
			.headers(headers_0))
		.pause(12)
  }

  object DeleteDaycare {
	  var deleteDaycare = exec(http("request_5")
			.get("/daycares/1/delete")
			.headers(headers_0))
		.pause(3)
  }

	val deleteScn = scenario("Delete").exec(Home.home,
									  Login.login,
									  ListDaycares.listDaycares,
									  ShowDaycare.showDaycare,
									  DeleteDaycare.deleteDaycare
									  )
	val listScn = scenario("List").exec(Home.home,
									  Login.login,
									  ListDaycares.listDaycares)


	setUp(
		deleteScn.inject(rampUsers(5000) during (60 seconds)),
		listScn.inject(rampUsers(5000) during (60 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}
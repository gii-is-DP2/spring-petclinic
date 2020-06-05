package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DaycareShow extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.jpg""", """.*\.ico""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	object Home{
		val home= exec(http("Home")
			.get("/"))
		.pause(8)
	}

	object Login{
		val login= exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(16)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "george")
			.formParam("password", "george")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}

	object ShowDaycares{
		val showdaycares= exec(http("ShowDaycares")
			.get("/daycares/owner"))
		.pause(11)
	}

	object ShowDaycare{
		val showDaycare= exec(http("ShowDaycare")
			.get("/daycares/1"))
		.pause(12)
	}

	object WrongUser{
		val wrongUser= exec(http("WrongUser")
			.get("/daycares/1"))
		.pause(25)
		.exec(http("request_1")
			.get("/daycares/2"))
		.pause(12)
	}

	val scnOwners = scenario("Owners").exec(Home.home, Login.login, 
	ShowDaycares.showdaycares, ShowDaycare.showDaycare)

	val scnWrongOwner = scenario("WrongOwner").exec(Home.home, Login.login, 
	WrongUser.wrongUser)

	val scnAdmins = scenario("Admins").exec(Home.home, Login.login, 
	ShowDaycares.showdaycares, ShowDaycare.showDaycare)

	setUp(scnOwners.inject(rampUsers(3500) during (10 seconds)),
	scnWrongOwner.inject(rampUsers(3500) during (10 seconds)),
	scnAdmins.inject(rampUsers(3500) during (10 seconds))).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}
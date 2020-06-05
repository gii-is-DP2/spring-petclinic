package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DaycareUpdate extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.jpg""", """.*\.ico""", """.*\.png"""), WhiteList())
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

object Home{
		val home= exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
	}

	object Login{
		val login= exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(15)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "george")
			.formParam("password", "george")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	object ShowDaycares{
		val showdaycares= exec(http("ShowDaycares")
			.get("/daycares/owner")
			.headers(headers_0))
		.pause(11)
	}

	object ShowDaycare{
		val showDaycare= exec(http("ShowDaycare")
			.get("/daycares/2")
			.headers(headers_0))
		.pause(10)
	}	

	object UpdateDaycareForm{
		val updateDaycareForm= exec(http("UpdateDaycareForm")
			.get("/daycares/2/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("UpdatedDaycare")
			.post("/daycares/2/edit")
			.headers(headers_2)
			.formParam("date", "2033/02/01")
			.formParam("description", "Descripcion de prueba diferente")
			.formParam("petName", "Leo")
			.formParam("capacity", "10")
			.formParam("_csrf", "${stoken}"))
		.pause(28)
	}

	object WrongData{
		val wrongData= exec(http("UpdateDaycareForm")
			.get("/daycares/2/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("WrongData")
			.post("/daycares/4/edit")
			.headers(headers_2)
			.formParam("date", "2013/01/31")
			.formParam("description", "Descripcion de prueba diferente")
			.formParam("petName", "Leo")
			.formParam("capacity", "10")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
		.exec(http("UpdatedDaycare")
			.post("/daycares/2/edit")
			.headers(headers_2)
			.formParam("date", "2033/02/01")
			.formParam("description", "Descripcion de prueba diferente")
			.formParam("petName", "Leo")
			.formParam("capacity", "10")
			.formParam("_csrf", "${stoken}"))
		.pause(28)
	}

	val scnOK = scenario("DaycareUpdate").exec(Home.home, Login.login, 
	ShowDaycares.showdaycares, UpdateDaycareForm.updateDaycareForm)

	val scnError = scenario("WrongData").exec(Home.home, Login.login, 
	ShowDaycares.showdaycares, WrongData.wrongData)

	setUp(scnOK.inject(rampUsers(7500) during (100 seconds)),
	scnError.inject(rampUsers(7500) during (100 seconds))).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}
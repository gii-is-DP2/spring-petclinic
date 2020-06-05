package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia8Diagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png""", """.*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_4 = Map(
		"A-IM" -> "x-bm,gzip",
		"Proxy-Connection" -> "keep-alive")

    val uri1 = "http://clientservices.googleapis.com/chrome-variations/seed"


	object Home{
		var home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(16)
	}

	object Login{
		var login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
		.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(21)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "george")
			.formParam("password", "george")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	object ListHairdressings{
		var listHairdressings = exec(http("ListHairdressings")
			.get("/hairdressings/owner")
			.headers(headers_0))
		.pause(18)
	}

	object ShowHairdressing{
		var showHairdressing = exec(http("ShowHairdressing")
			.get("/hairdressings/99")
			.headers(headers_0))
		.pause(15)
	}

	object EditHairdressing{
		var editHairdressing = exec(http("EditHairdressingForm")
			.get("/hairdressings/99/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(34)
		.exec(http("EditHairdressing")
			.post("/hairdressings/99/edit")
			.headers(headers_3)
			.formParam("date", "2023/02/02")
			.formParam("description", "TEST modify")
			.formParam("time", "7:00")
			.formParam("cuidado", "ESTETICA")
			.formParam("petName", "Leo")
			.formParam("_csrf", "${stoken}"))
		.pause(24)
	}

	object EditHairdressingEmpty{
		var editHairdressingEmpty = exec(http("EditHairdressingForm")
			.get("/hairdressings/99/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(34)
		.exec(http("EditHairdressing")
			.post("/hairdressings/99/edit")
			.headers(headers_3)
			.formParam("date", "")
			.formParam("description", "")
			.formParam("petName", "Leo")
			.formParam("_csrf", "${stoken}"))
		.pause(24)
	}

	object EditHairdressingUnauthorised{
		var editHairdressingUnauthorised = exec(http("EditHairdressingUnauthorised")
			.get("/hairdressings/90/edit")
			.headers(headers_0))
		.pause(33)
	}


	val CasoPositivoScn = scenario("Historia8CasoPositivo").exec(Home.home,
																 Login.login,
																 ListHairdressings.listHairdressings,
																 ShowHairdressing.showHairdressing,
																 EditHairdressing.editHairdressing)

	val CasoNegativo1Scn = scenario("Historia8CasoNegativo1").exec(Home.home,
																 Login.login,
																 EditHairdressingUnauthorised.editHairdressingUnauthorised)

	val CasoNegativo2Scn = scenario("Historia8CasoNegativo2").exec(Home.home,
																 Login.login,
																 ListHairdressings.listHairdressings,
																 ShowHairdressing.showHairdressing,
																 EditHairdressingEmpty.editHairdressingEmpty,
																 EditHairdressing.editHairdressing)
		

	setUp(
		CasoPositivoScn.inject(rampUsers(2700) during (10 seconds)),
		CasoNegativo1Scn.inject(rampUsers(2700) during (10 seconds)),
		CasoNegativo2Scn.inject(rampUsers(2700) during (10 seconds))
		).protocols(httpProtocol)
}
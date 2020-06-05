package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia9Diagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png""", """.*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")


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

	object ShowHairdressingUnauthorised{
		var showHairdressingUnauthorised = exec(http("request_6")
			.get("/hairdressings/90")
			.headers(headers_0))
		.pause(15)
	}


	val CasoPositivoScn = scenario("Historia9CasoPositivo").exec(Home.home,
																 Login.login,
																 ListHairdressings.listHairdressings,
																 ShowHairdressing.showHairdressing)

	val CasoNegativoScn = scenario("Historia9CasoNegativo").exec(Home.home,
																 Login.login,
																 ListHairdressings.listHairdressings,
																 ShowHairdressingUnauthorised.showHairdressingUnauthorised)
		

	setUp(
		CasoPositivoScn.inject(rampUsers(5500) during (10 seconds)),
		CasoNegativoScn.inject(rampUsers(5500) during (10 seconds))
		).protocols(httpProtocol)
}
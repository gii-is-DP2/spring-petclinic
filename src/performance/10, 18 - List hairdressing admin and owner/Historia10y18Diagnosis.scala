package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia10y18Diagnosis extends Simulation {

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
		.pause(10)
	}

	object LoginOwner{
		var loginOwner = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(13)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "george")
			.formParam("password", "george")
			.formParam("_csrf", "$[stoken]"))
		.pause(10)
	}

	object LoginAdmn{
		var loginAdmn = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(13)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "$[stoken]"))
		.pause(10)
	}

	object ListHairdressingsOwner{
		var listHairdressingsOwner = exec(http("ListHairdressingsOwner")
			.get("/hairdressings/owner")
			.headers(headers_0))
		.pause(19)
	}

	object ListHairdressingsAdmn{
		var listHairdressingsAdmn = exec(http("ListHairdressingsAdmn")
			.get("/hairdressings")
			.headers(headers_0))
		.pause(16)
	}

	val CasoPositivoScn = scenario("Historia10y18CasoPositivo").exec(Home.home,
																 LoginOwner.loginOwner,
																 ListHairdressingsOwner.listHairdressingsOwner)

	val CasoNegativoScn = scenario("Historia10y18CasoNegativo").exec(Home.home,
																 LoginAdmn.loginAdmn,
																 ListHairdressingsAdmn.listHairdressingsAdmn)
		

	setUp(
		CasoPositivoScn.inject(rampUsers(5500) during (10 seconds)),
		CasoNegativoScn.inject(rampUsers(5500) during (10 seconds))
		).protocols(httpProtocol)
}
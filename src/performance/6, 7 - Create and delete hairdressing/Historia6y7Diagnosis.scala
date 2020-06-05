package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia6y7Diagnosis extends Simulation {

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

	val headers_8 = Map(
		"A-IM" -> "x-bm,gzip",
		"Proxy-Connection" -> "keep-alive")

    val uri1 = "http://clientservices.googleapis.com/chrome-variations/seed"


	object Home{
		var home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(10)
	}

	object Login{
		var login = exec(http("Login")
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

	object ListHairdressings{
		var listHairdressings = exec(http("ListHairdressings")
			.get("/hairdressings/owner")
			.headers(headers_0))
		.pause(13)
	}

	object AddHairdressing{
		var addHairdressing = exec(http("AddHairdressingForm")
			.get("/hairdressings/new")
			.headers(headers_0)
			.headers(headers_2)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(36)
		.exec(http("AddHairdressing")
			.post("/hairdressings/new")
			.headers(headers_3)
			.formParam("date", "2050/06/26")
			.formParam("description", "Hola")
			.formParam("time", "6:00")
			.formParam("cuidado", "ESTETICA")
			.formParam("petName", "Leo")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
	}

	object AddHairdressingEmpty{
		var addHairdressingEmpty = exec(http("AddHairdressingForm")
			.get("/hairdressings/new")
			.headers(headers_0)
			.headers(headers_2)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(22)
		.exec(http("AddHairdressing")
			.post("/hairdressings/new")
			.headers(headers_3)
			.formParam("date", "")
			.formParam("description", "")
			.formParam("petName", "Leo")
			.formParam("_csrf", "${stoken}"))
		.pause(71)
	}

	object ShowHairdressing{
		var showHairdressing = exec(http("ShowHairdressing")
			.get("/hairdressings/100")
			.headers(headers_0))
		.pause(6)
	}

	object DeleteHairdressing{
		var deleteHairdressing = exec(http("DeleteHairdressing")
			.get("/hairdressings/100/delete")
			.headers(headers_0))
		.pause(16)
	}

	object DeleteHairdressingUnauthorised{
		var deleteHairdressingUnauthorised = exec(http("DeleteHairdressingUnauthorised")
			.get("/hairdressings/90/delete")
			.headers(headers_0))
		.pause(15)
	}


	

	val CasoPositivoScn = scenario("Historia6y7CasoPositivo").exec(Home.home,
																 Login.login,
																 ListHairdressings.listHairdressings,
																 AddHairdressing.addHairdressing,
																 ShowHairdressing.showHairdressing,
																 DeleteHairdressing.deleteHairdressing)
	
	val CasoNegativoScn = scenario("Historia6y7CasoNegativo").exec(Home.home,
																 Login.login,
																 ListHairdressings.listHairdressings,
																 AddHairdressingEmpty.addHairdressingEmpty,
																 DeleteHairdressingUnauthorised.deleteHairdressingUnauthorised)


	setUp(
		CasoPositivoScn.inject(rampUsers(5500) during (10 seconds)),
		CasoNegativoScn.inject(rampUsers(5500) during (10 seconds))
		).protocols(httpProtocol)
}
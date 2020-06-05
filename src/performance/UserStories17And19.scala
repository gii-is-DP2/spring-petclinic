package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ListTrainingsAdminAndTrainer extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.css""", """.*.js"""), WhiteList())
		.acceptHeader("*/*")
		.userAgentHeader("Microsoft-Delivery-Optimization/10.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"DNT" -> "1",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"DNT" -> "1",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")


	object Home{
		val home = exec(http("Home")
			.get("/"))
		.pause(10)
	}

	object Login{
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(15)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	object ListTrainers{
		val listTrainers = exec(http("ListTrainers")
			.get("/trainers")
			.headers(headers_0))
		.pause(26)
	}
	
	object SelectTrainer{
		val selectTrainer = exec(http("ShowTrainer")
			.get("/trainers/1")
			.headers(headers_0))
		.pause(14)
	}

	object ListTrainingsOfTrainer{
		val listTrainingsOfTrainer = exec(http("ListTrainingsOfTrainer")
			.get("/trainers/1/trainings")
			.headers(headers_0))
		.pause(21)
	}

	object ListTrainings{
		val listTrainings = exec(http("ListTrainings")
			.get("/trainings")
			.headers(headers_0))
		.pause(25)
	}
		
	val trainerTrainingsScn = scenario("ListTrainingsOfTrainer").exec(Home.home, Login.login, ListTrainers.listTrainers, SelectTrainer.selectTrainer,
																	 ListTrainingsOfTrainer.listTrainingsOfTrainer)

	val adminTrainingsScn = scenario("ListTrainingsOfAll").exec(Home.home, Login.login, ListTrainings.listTrainings)

	setUp(
		adminTrainingsScn.inject(rampUsers(4000) during (10 seconds)),
		trainerTrainingsScn.inject(rampUsers(4000) during (10 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )

}
package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ShowTrainersTwoScenarios extends Simulation {

	val httpProtocol = http
		.baseUrl("http://dp2:8046")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,es;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://dp2:8046",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

  object Home {
    val home = exec(http("Home")
			.get("/")
			.headers(headers_0)
    ).pause(6)
  }

  object Login {
    val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(9)
		// Login
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "${stoken}")
    ).pause(13)
  }

  object TrainersList {
    val trainersList = exec(http("TrainersList")
			.get("/trainers")
			.headers(headers_0)
    ).pause(22)
  }

  object FindTrainer {
    val findTrainer = exec(http("FindByLastName")
			.get("/trainers/find?lastName=Balotelli")
			.headers(headers_0)
		).pause(18)
  }

  object TrainerInformation {
    val trainerInformation = exec(http("TrainerInformation")
			.get("/trainers/1")
			.headers(headers_0)
    ).pause(48)
  }

  object NewTrainerForm {
    val newTrainerForm = exec(http("NewTrainerForm")
			.get("/trainers/new")
			.headers(headers_0)
      .check(css("input[name=_csrf]", "value").saveAs("stoken")))
  }

  val findTrainerScn = scenario("FindTrainerScenario").exec(
    Home.home,
    Login.login,
    TrainersList.trainersList,
    FindTrainer.findTrainer,
    TrainerInformation.trainerInformation
  )

  val newTrainerScn = scenario("NewTrainerScenario").exec(
    Home.home,
    Login.login,
    TrainersList.trainersList,
    NewTrainerForm.newTrainerForm
  )

	setUp(
    findTrainerScn.inject(rampUsers(6500) during (100 seconds)),
    newTrainerScn.inject(rampUsers(6500) during (100 seconds))
  ).protocols(httpProtocol)
  .assertions(
    global.responseTime.max.lt(5000),    
    global.responseTime.mean.lt(1000),
    global.successfulRequests.percent.gt(95)
  )
}
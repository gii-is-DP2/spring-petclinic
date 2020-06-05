package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateTrainerTwoScenarios extends Simulation {

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
    ).pause(5)
  }

  object Login {
    val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(9)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "${stoken}")
    ).pause(10)
  }

  object NewTrainerForm {
    val newTrainerForm = exec(http("AddTrainerForm")
			.get("/trainers/new")
			.headers(headers_0)
      .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(57)
  }

  object NewTrainer {
    val newTrainer = exec(http("CreatedTrainer")
			.post("/trainers/new")
			.headers(headers_3)
			.formParam("firstName", "Martin")
			.formParam("lastName", "Colella")
			.formParam("salary", "45")
			.formParam("dni", "47842799")
			.formParam("telephone", "625096669")
			.formParam("email", "martinc@gmail.com")
			.formParam("specialty", "Coordinacion")
			.formParam("description", "Muy puntual")
			.formParam("_csrf", "${stoken}") 
    ).pause(46)
  }

  object NewInvalidTrainer {
    val newInvalidTrainer = exec(http("CreateTrainerInvalid")
			.post("/trainers/new")
			.headers(headers_3)
			.formParam("firstName", "")
			.formParam("lastName", "")
			.formParam("salary", "0.0")
			.formParam("dni", "")
			.formParam("telephone", "")
			.formParam("email", "")
			.formParam("specialty", "")
			.formParam("description", "")
			.formParam("_csrf", "${stoken}") 
    ).pause(37)
  }

  object NewValidTrainer {
    val newValidTrainer = exec(http("CreatedTrainerValid")
			.post("/trainers/new")
			.headers(headers_3)
			.formParam("firstName", "Martin")
			.formParam("lastName", "Ferrer")
			.formParam("salary", "46")
			.formParam("dni", "46464646")
			.formParam("telephone", "46464646")
			.formParam("email", "mfb@gmail.com")
			.formParam("specialty", "Velocidad")
			.formParam("description", "Tranquilo")
			.formParam("_csrf", "${stoken}") 
    ).pause(8)
  }

  val validScn = scenario("ValidScenario").exec(
    Home.home,
    Login.login,
    NewTrainerForm.newTrainerForm,
    NewTrainer.newTrainer
  )

  val invalidScn = scenario("InvalidScenario").exec(
    Home.home,
    Login.login,
    NewTrainerForm.newTrainerForm,
    NewInvalidTrainer.newInvalidTrainer,
    NewValidTrainer.newValidTrainer
  )

	setUp(
    validScn.inject(rampUsers(4000) during (100 seconds)),
    invalidScn.inject(rampUsers(4000) during (100 seconds))
  ).protocols(httpProtocol)
  .assertions(
    global.responseTime.max.lt(5000),    
    global.responseTime.mean.lt(1000),
    global.successfulRequests.percent.gt(95)
  )
}
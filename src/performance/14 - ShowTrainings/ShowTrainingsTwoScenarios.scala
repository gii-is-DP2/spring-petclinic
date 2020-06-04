package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ShowTrainings extends Simulation {

	val httpProtocol = http
		.baseUrl("http://dp2:8046")
		.disableFollowRedirect
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
      .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(9)
    .exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "fede")
			.formParam("password", "fede")
			.formParam("_csrf", "${stoken}")
			.resources(http("request_4")
			.get("/")
			.headers(headers_0))
			.check(status.is(302))
    ).pause(7)
  }

  object TrainingList {
    val trainingList = exec(http("TrainingsList")
      .get("/trainings/owner")
      .headers(headers_0)
    ).pause(15)
  }

  object TrainingInformation {
    val trainingInformation = exec(http("TrainingInformation")
			.get("/trainings/1")
			.headers(headers_0)
		).pause(11)
  }

  object NewTrainingForm {
    val newTrainingForm = exec(http("NewTrainingForm")
			.get("/trainings/new")
			.headers(headers_0)
      .check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(9)
  }

	val trainingInfoScn = scenario("TrainingInformationScenario").exec(
    Home.home,
    Login.login,
    TrainingList.trainingList,
    TrainingInformation.trainingInformation
  )

  val newTrainingScn = scenario("NewTrainingScenario").exec(
    Home.home,
    Login.login,
    TrainingList.trainingList,
    NewTrainingForm.newTrainingForm
  )

  setUp(
    trainingInfoScn.inject(rampUsers(8000) during (100 seconds)),
    newTrainingScn.inject(rampUsers(8000) during (100 seconds))
  ).protocols(httpProtocol)
  .assertions(
    global.responseTime.max.lt(5000),    
    global.responseTime.mean.lt(1000),
    global.successfulRequests.percent.gt(95)
  )
}
package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EditTraining extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.css""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.doNotTrackHeader("1")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

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
			.formParam("username", "george")
			.formParam("password", "george")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	object ListTrainings{
		val listTrainings = exec(http("ListTrainings")
			.get("/trainings/owner"))
		.pause(13)
	}

	object NewTraining{
		val newTraining = exec(http("NewTrainingForm")
			.get("/trainings/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(66)
		.exec(http("NewTrainingCreated")
			.post("/trainings/new")
			.headers(headers_2)
			.formParam("date", "2020/06/25")
			.formParam("description", "alala")
			.formParam("ground", "3")
			.formParam("groundType", "AGILIDAD")
			.formParam("petName", "Leo")
			.formParam("trainerId", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(17)

	}

	object ShowTraining{
		val showTraining = exec(http("ShowTraining")
			.get("/trainings/3"))
		.pause(10)
	}

	object EditTraining{
		val editTraining = exec(http("EditForm")
			.get("/trainings/4/edit")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("TrainingEdited")
			.post("/trainings/4/edit")
			.headers(headers_2)
			.formParam("date", "2020/06/26")
			.formParam("description", "alala")
			.formParam("ground", "5")
			.formParam("groundType", "OBSTACULOS")
			.formParam("petName", "Leo")
			.formParam("trainerId", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(14)
	}

	val directEditScn = scenario("DirectEditTraining").exec(Home.home, Login.login, ListTrainings.listTrainings, NewTraining.newTraining, EditTraining.editTraining)

	val listEditScn = scenario("ListEditTraining").exec(Home.home, Login.login, ListTrainings.listTrainings,
													 NewTraining.newTraining,ListTrainings.listTrainings,ShowTraining.showTraining,EditTraining.editTraining)

	setUp(
		directEditScn.inject(rampUsers(3000) during (10 seconds)),
		listEditScn.inject(rampUsers(3000) during (10 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )


}
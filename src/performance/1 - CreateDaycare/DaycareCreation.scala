package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DaycareCreation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.jpg""", """.*\.ico""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	object Home{
		val home= exec(http("Home")
			.get("/"))
		.pause(9)
	}

	object Login{
		val login= exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "george")
			.formParam("password", "george")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	object ShowDaycares{
		val showdaycares= exec(http("ShowDaycares")
			.get("/daycares/owner"))
		.pause(16)
	}

	object NewDaycareForm{
		val newDaycareForm= exec(http("NewDaycareForm")
			.get("/daycares/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
			.pause(22)
			.exec(http("NewDaycareCreated")
				.post("/daycares/new")
				.headers(headers_2)
				.formParam("date", "2020/06/05")
				.formParam("description", "des")
				.formParam("petName", "Leo")
				.formParam("capacity", "10")
				.formParam("_csrf", "${stoken}"))
			.pause(9)
	}

	object WrongData{
		val wrongData= exec(http("NewDaycareForm")
			.get("/daycares/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
			.pause(22)
			.exec(http("WrongData")
				.post("/daycares/new")
				.headers(headers_2)
				.formParam("date", "2020/05/01")
				.formParam("description", "des")
				.formParam("petName", "Leo")
				.formParam("capacity", "10")
				.formParam("_csrf", "${stoken}"))
			.pause(14)
			.exec(http("NewDaycareCreated")
				.post("/daycares/new")
				.headers(headers_2)
				.formParam("date", "2020/06/05")
				.formParam("description", "des")
				.formParam("petName", "Leo")
				.formParam("capacity", "10")
				.formParam("_csrf", "${stoken}"))
			.pause(9)
	}

	object NewDaycareCreated{
		val newDaycareCreated= exec(http("NewDaycareCreated")
			.post("/daycares/new")
			.headers(headers_2)
			.formParam("date", "2020/06/05")
			.formParam("description", "des")
			.formParam("petName", "Leo")
			.formParam("capacity", "10")
			.formParam("_csrf", "b0d44508-b97b-4580-83c0-365d2bfd3945"))
		.pause(9)
	}

	val scnOK = scenario("OK").exec(Home.home, Login.login, 
	ShowDaycares.showdaycares, NewDaycareForm.newDaycareForm)

	val scnError = scenario("Error").exec(Home.home, Login.login, 
	ShowDaycares.showdaycares, WrongData.wrongData)

	setUp(scnOK.inject(rampUsers(6500) during (100 seconds)),
	scnError.inject(rampUsers(6500) during (100 seconds))).protocols(httpProtocol)

}
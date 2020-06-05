package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AddAndDeleteReview extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.ico""", """.*.css""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8,pt;q=0.7,zh-CN;q=0.6,zh;q=0.5,la;q=0.4")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

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

	object Home {
		var home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
	}

	object OwnerLogin {
		var ownerLogin = exec(http("Login")
			.get("/login")
			.headers(headers_0)
      		.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(1)
		.exec(http("LoggedIn")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "fede")
			.formParam("password", "fede")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}

	object AdminLogin {
		var adminLogin = exec(http("Login")
			.get("/login")
			.headers(headers_0)
      		.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(1)
		.exec(http("LoggedIn")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}

	object ListReviews {
		var listReviews = exec(http("ListReviews")
			.get("/reviews")
			.headers(headers_0))
		.pause(8)
	}

	object AddReview {
		var addReview = exec(http("AddReviewForm")
			.get("/reviews/new")
			.headers(headers_0)
      		.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(13)
		.exec(http("AddReview")
			.post("/reviews/new")
			.headers(headers_3)
			.formParam("comments", "Tre men do")
			.formParam("rating", "5")
			.formParam("serviceType", "TRAINING")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	object DeleteReview {
		var deleteReview = exec(http("DeleteReview")
			.get("/reviews/6/delete")
			.headers(headers_0))
		.pause(10)
	}

	val addReviewScn = scenario("AddReview").exec(
		Home.home,
		OwnerLogin.ownerLogin,
		ListReviews.listReviews,
		AddReview.addReview
	)

	val deleteReviewScn = scenario("DeleteReview").exec(
		Home.home,
		AdminLogin.adminLogin,
		ListReviews.listReviews,
		DeleteReview.deleteReview
	)

	setUp(
		addReviewScn.inject(rampUsers(1800) during (5 seconds)),
		deleteReviewScn.inject(rampUsers(1800) during (5 seconds))
	)
	.protocols(httpProtocol)
    .assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
    )

}
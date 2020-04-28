<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets, trainers or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav navbar-left">

				<petclinic:menuItem active="${name eq 'owners'}" url="/owners/find"
					title="owners">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Owners</span>
				</petclinic:menuItem>

				<petclinic:menuItem active="${name eq 'vets'}" url="/vets"
					title="veterinarians">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Veterinarians</span>
				</petclinic:menuItem>
				
				<sec:authorize access="hasAuthority('admin')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> 
						<span class="glyphicon glyphicon-user"></span> Trainers <span class="glyphicon glyphicon-chevron-down"></span></a>
						<ul class="dropdown-menu">
							
								<li>
									<petclinic:menuItem active="${name eq 'trainers'}" url="/trainers" title="trainers">
										<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
										<span>Trainers</span>
									</petclinic:menuItem>
								</li>
								<li>
									<petclinic:menuItem active="${name eq 'addtrainer'}" url="/trainers/new" title="Add trainer">
										<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
										<span>Add Trainer</span>
									</petclinic:menuItem>
								</li>
						</ul>
					</li>
				</sec:authorize>
				
				<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> 
					<span class="glyphicon glyphicon-check"></span> Reservations <span class="glyphicon glyphicon-chevron-down"></span></a>
					<ul class="dropdown-menu">
							<li>
								<sec:authorize access="hasAuthority('admin')">
									<petclinic:menuItem active="${name eq 'trainings'}" url="/trainings" title="trainings">
										<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
										<span>Trainings</span>
									</petclinic:menuItem>
								</sec:authorize>
								<sec:authorize access="hasAuthority('owner')">
									<petclinic:menuItem active="${name eq 'trainings'}" url="/trainings/owner" title="trainings">
										<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
										<span>Trainings</span>
									</petclinic:menuItem>
								</sec:authorize>
								
								<sec:authorize access="hasAuthority('admin')">
									<petclinic:menuItem active="${name eq 'daycares'}" url="/daycares" title="daycares">
										<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
										<span>Daycares</span>
									</petclinic:menuItem>
								</sec:authorize>
								<sec:authorize access="hasAuthority('owner')">
									<petclinic:menuItem active="${name eq 'daycares'}" url="/daycares/owner" title="daycares">
										<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
										<span>Daycares</span>
									</petclinic:menuItem>
								</sec:authorize>
								<sec:authorize access="hasAuthority('admin')">
									<petclinic:menuItem active="${name eq 'hairdressings'}" url="/hairdressings" title="hairdressings">
										<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
										<span>Hairdressings</span>
									</petclinic:menuItem>
								</sec:authorize>
								<sec:authorize access="hasAuthority('owner')">
									<petclinic:menuItem active="${name eq 'hairdressings'}" url="/hairdressings/owner" title="hairdressings">
										<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
										<span>Hairdressings</span>
									</petclinic:menuItem>
								</sec:authorize>
							</li>
					</ul>
				</li>
				
				<petclinic:menuItem active="${name eq 'reviews'}" url="/reviews"
					title="owners">
					<span class="glyphicon glyphicon-star" aria-hidden="true"></span>
					<span>Reviews</span>
				</petclinic:menuItem>
				
				<li class="dropdown">
					<sec:authorize access="!isAuthenticated()">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>User 
							<span class="glyphicon glyphicon-chevron-down"></span>
						</a>
						<ul class="dropdown-menu">
							<li><a href="<c:url value="/login" />">Login</a></li>
							<li><a href="<c:url value="/owners/new" />">Register</a></li>
						</ul>
					</sec:authorize>
					<sec:authorize access="isAuthenticated()">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown"> 
							<span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> 
							<span class="glyphicon glyphicon-chevron-down"></span>
						</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left"><strong><sec:authentication property="name" /></strong></p>
											<p class="text-left"><a href="<c:url value="/logout" />" class="btn btn-primary btn-block btn-sm">Logout</a></p>
										</div>
									</div>
								</div>
							</li>
						</ul>
					</sec:authorize>
				</li>
			</ul>
		</div>
	</div>
</nav>

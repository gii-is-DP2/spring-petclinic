<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ attribute name="currentPage" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-expand-lg navbar-dark" style="background-color: #074b95 !important;">
    <a class="navbar-brand" href="#">C O R O N A</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item ${currentPage eq 'home' ? 'active' : ''}">
                <a class="nav-link" href="/">Home</a>
            </li>
            <li class="nav-item ${currentPage eq 'owners' ? 'active' : ''}">
                <a class="nav-link" href="/owners/find">Find owners</a>
            </li>
			<li class="nav-item ${currentPage eq 'vets' ? 'active' : ''}">
                <a class="nav-link" href="/vets">Veterinarians</a>
            </li>
			<li class="nav-item ${currentPage eq 'error' ? 'active' : ''}">
                <a class="nav-link" href="/oups">Error</a>
            </li>
        </ul>
        <ul class="navbar-nav">
            <sec:authorize access="!isAuthenticated()">
                <li class="nav-item ${currentPage eq 'clients' ? 'active' : ''}">
                    <a class="nav-link btn btn-sm btn-primary mr-2" href="/clients">Área clientes</a>
                </li>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <sec:authentication property="name" />
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item disabled" href="#">My profile</a>
                        <a class="dropdown-item" href="/logout">Logout</a>
                    </div>
                </li>
            </sec:authorize>
        </ul>
    </div>
</nav>
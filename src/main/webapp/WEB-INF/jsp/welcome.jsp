<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="home">
    <div class="row">
    	<h2> Welcome to PetClinic! </h2>
    </div>
    <div class="row">
    	<p> We are a company with more than 10 years of experience working with pets. </p>
    </div>
    <div class="row">
    	<p> We offer the following services for you and your pet: </p>
    	<ul>
    		<li> veterinary </li>
    		<li> training </li>
    		<li> hairdressing </li>
    		<li> daycare </li>
    	</ul>
    </div>
    <div class="row">
        <div class="col-md-12">
            <spring:url value="/resources/images/pets.png" htmlEscape="true" var="petsImage"/>
            <img class="img-responsive" src="${petsImage}"/>
        </div>
    </div>
</petclinic:layout>

<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="accessDenied">

    <spring:url value="/resources/images/pets.png" var="petsImage"/>
    <img src="${petsImage}"/>

    <h2>Element not found!</h2>

</petclinic:layout>


<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trainings">

    <h2>Training Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Date</th>
            <td><b><c:out value="${training.date}"/></b></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><b><c:out value="${training.description}"/></b></td>
        </tr>
        <tr>
            <th>Pista</th>
            <td><b><c:out value="${training.pista}"/></b></td>
        </tr>
        <tr>
            <th>Tipo pista</th>
            <td><b><c:out value="${training.tipoPista}"/></b></td>
        </tr>
    </table>
    
   	<a class="btn btn-default" href='<spring:url value="/trainings/${training.id}/edit" htmlEscape="true"/>'>Edit training</a>
   	
   	
   	
   	<spring:url value="/trainings/{trainingId}/delete" var="trainingUrl">
   		<spring:param name="trainingId" value="${training.id }"></spring:param>
   	</spring:url>
   	
   	
   	<a class="btn btn-default" href="${fn:escapeXml(trainingUrl)}">Cancel training</a>

</petclinic:layout>

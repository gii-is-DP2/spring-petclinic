<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trainings">

    <h2>Hairdressing Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Date</th>
            <td><b><c:out value="${hairdressing.date}"/></b></td>
        </tr>
        <tr>
            <th>Time</th>
            <td><b><c:out value="${hairdressing.time}"/></b></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><b><c:out value="${hairdressing.description}"/></b></td>
        </tr>
        <tr>
            <th>Pet</th>
            <td><b><c:out value="${hairdressing.pet.name}"/></b></td>
        </tr>
        <tr>
            <th>Pet owner</th>
            <td><b><c:out value="${hairdressing.pet.owner.user.username}"/></b></td>
        </tr>
    </table>
    
   	<a class="btn btn-default" href='<spring:url value="/hairdressings/${hairdressing.id}/edit" htmlEscape="true"/>'>Edit hairdressing</a>
   	
   	
   	
   	<spring:url value="/hairdressings/{hairdressingId}/delete" var="hairdressingUrl">
   		<spring:param name="hairdressingId" value="${hairdressing.id }"></spring:param>
   	</spring:url>
   	
   	
   	<a class="btn btn-default" href="${fn:escapeXml(hairdressingUrl)}">Cancel hairdressing</a>

</petclinic:layout>

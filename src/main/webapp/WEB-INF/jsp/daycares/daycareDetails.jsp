<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="daycare">

    <h2>Daycare Information</h2>


    <table class="table table-striped">
    	<tr>
            <th>Capacity</th>
            <td><b><c:out value="${daycare.capacity}"/></b></td>
        </tr>
        <tr>
            <th>Date</th>
            <td><b><c:out value="${daycare.date}"/></b></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><b><c:out value="${daycare.description}"/></b></td>
        </tr>
    </table>
    
    <spring:url value="/owners/{ownerId}/pets/{petId}/daycares/{daycareId}/edit" var="editUrl">
        <spring:param name="daycareId" value="${daycare.id}"/>
        <spring:param name="ownerId" value="${owner.id}"/>
        <spring:param name="petId" value="${pet.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Daycare</a>

</petclinic:layout>
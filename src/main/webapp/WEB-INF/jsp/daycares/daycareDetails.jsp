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
        <tr>
            <th>Pet</th>
            <td><b><c:out value="${daycare.pet.name}"/></b></td>
        </tr>
    </table>
    <c:if test="${owner}">
    <a class="btn btn-default" href='<spring:url value="/daycares/${daycare.id}/edit" htmlEscape="true"/>'>Edit daycare</a>
   	</c:if>
   	<a class="btn btn-default" href='<spring:url value="/daycares/${daycare.id}/delete" htmlEscape="true"/>'>Cancel daycare</a>

</petclinic:layout>
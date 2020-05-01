<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="profile">

    <h2>Profile</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Address</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>City</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>

    <spring:url value="owners/{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Profile</a>

    <spring:url value="owners/{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>

    <br/>
    <br/>
    <br/>

    <h2>Pets</h2>
    	<table class="table table-striped">
        	<tr>
            	<th>Name</th>
            	<th>Birth Date</th>
            	<th>Type</th>
            	<th>Actions</th>
        	</tr>
        	<c:forEach var="pet" items="${owner.pets}">
        		<tr>
            		<td><c:out value="${pet.name}"/></td>
            		<td><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></td>
        			<td><c:out value="${pet.type.name}"/></td>
        			<td>
            			<spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
            				<spring:param name="ownerId" value="${owner.id}"/>
            				<spring:param name="petId" value="${pet.id}"/>
            			</spring:url>
            			<a href="${fn:escapeXml(petUrl)}">Edit</a>
            		</td>
        		</tr>
        	</c:forEach>
    	</table>
</petclinic:layout>

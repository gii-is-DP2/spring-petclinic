<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="daycares">
    <h2>Daycares</h2>
    
    <c:if test="${owner}">
    <a class="btn btn-default" href='<spring:url value="/daycares/new" htmlEscape="true"/>'>Add daycare</a>
	</c:if>
	
    <table id="daycaresTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Pet</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${daycares}" var="daycare">
            <tr>
            	<td>
                    <spring:url value="/daycares/{daycareId}" var="daycareUrl">
                        <spring:param name="daycareId" value="${daycare.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(daycareUrl)}"><c:out value="${daycare.date}"/></a>
                </td>
                <td>
                    <c:out value="${daycare.description}"/>
                </td>
                <td>
                    <c:out value="${daycare.pet.name}"/>
                </td>      
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>

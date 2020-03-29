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

    <table id="daycaresTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 200px;">Description</th>
            <th>Date</th>
            <th>Capacity</th>
            <td>
            	<spring:url value="/daycares/delete/{daycareId}" var="daycareUrl">
               		<spring:param name="daycareId" value="${daycare.id}"/>
                </spring:url>
            	<a href="${fn:escapeXml(daycareUrl)}"></a>

        </tr>
        </thead>
        <tbody>
        <c:forEach items="${daycares}" var="daycare">
            <tr>
                <td>
                    <c:out value="${daycare.description}"/>
                </td>
                <td>
                    <c:out value="${Date.date}"/>
                </td>
                <td>
                    <c:out value="25"/>
                </td>
                <td>
                    <c:forEach var="pet" items="${owner.pets}">
                        <c:out value="${pet.name} "/>
                    </c:forEach>
                </td>               
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br/>
    <a class="btn btn-default" href='<spring:url value="/daycares/delete" htmlEscape="true"/>'>Delete Visit</a>
</petclinic:layout>

<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="hairdressings">
    <h2>Hairdressings</h2>
    <c:if test="${owner}">
		<a class="btn btn-default" href='<spring:url value="/hairdressings/new" htmlEscape="true"/>'>Add hairdressing</a>
	</c:if>
	

    <table id="hairdressingsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Time</th>
            <th>Pet</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${hairdressings}" var="hairdressing">
            <tr>
                <td>
                    <spring:url value="/hairdressings/{hairdressingId}" var="hairdressingUrl">
                        <spring:param name="hairdressingId" value="${hairdressing.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(hairdressingUrl)}"><c:out value="${hairdressing.date}"/></a>
                </td>
                <td>
                    <c:out value="${hairdressing.description}"/>
                </td>
                <td>
                    <c:out value="${hairdressing.time}"/>
                </td>
                <td>
                    <c:out value="${hairdressing.pet}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>

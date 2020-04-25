<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trainings">
    <h2>Trainings</h2>
    
	<a class="btn btn-default" href='<spring:url value="/trainings/new" htmlEscape="true"/>'>Add training</a>
	<br><br>

    <table id="trainingsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Training Ground</th>
            <th>Training Ground Type</th>
            <th>Pet</th>
            <th>Trainer</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${trainings}" var="training">
            <tr>
                <td>
                    <spring:url value="/trainings/{trainingId}" var="trainingUrl">
                        <spring:param name="trainingId" value="${training.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(trainingUrl)}"><c:out value="${training.date}"/></a>
                </td>
                <td>
                    <c:out value="${training.description}"/>
                </td>
                <td>
                    <c:out value="${training.ground}"/>
                </td>
                <td>
                    <c:out value="${training.groundType}"/>
                </td>
                <td>
                    <c:out value="${training.pet.name}"/>
                </td>
                <td>
                    <spring:url value="/trainers/{trainerId}" var="trainerUrl">
                        <spring:param name="trainerId" value="${training.trainer.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(trainerUrl)}"><c:out value="${training.trainer.firstName} ${training.trainer.lastName}"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>

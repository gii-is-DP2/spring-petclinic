<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trainers">
    <h2>Trainers</h2>
    
    <a class="btn btn-default" href='<spring:url value="/trainers/new" htmlEscape="true"/>'>Add Trainer</a>
    
    <form:form modelAttribute="trainer" action="/trainers/find" method="get" class="form-horizontal" id="search-trainer-form">
        <div class="form-group">
            <div class="control-group" id="lastName">
                <label class="col-sm-2 control-label">Last name </label>
                <div class="col-sm-10">
                    <form:input class="form-control" path="lastName" size="30" maxlength="80"/>
                    <span class="help-inline"><form:errors path="*"/></span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Find Trainer</button>
            </div>
        </div>
    </form:form>

    <table id="trainersTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Salary</th>
            <th>DNI</th>
            <th>Telephone</th>
            <th>Email</th>
            <th>Specialty</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        
        <c:forEach items="${trainers}" var="trainer">
            <tr>
                <td>
                    <spring:url value="/trainers/{trainerId}" var="trainerUrl">
                        <spring:param name="trainerId" value="${trainer.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(trainerUrl)}"><c:out value="${trainer.firstName} ${trainer.lastName}"/></a>
                </td>
                <td>
                    <c:out value="${trainer.salary}"/>
                </td>
                <td>
                    <c:out value="${trainer.dni}"/>
                </td>
                <td>
                    <c:out value="${trainer.telephone}"/>
                </td>
                <td>
                    <c:out value="${trainer.email}"/>
                </td>
                <td>
                    <c:out value="${trainer.specialty}"/>
                </td>
                <td>
                    <c:out value="${trainer.description}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>

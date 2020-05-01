<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="carers">
    <h2>Carers</h2>
    
    <a class="btn btn-default" href='<spring:url value="/carers/new" htmlEscape="true"/>'>Add Carer</a>

    <table id="carersTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Salary</th>
            <th>DNI</th>
            <th>Telephone</th>
            <th>Email</th>
            <th>Hairdresser</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        
        <c:forEach items="${carers}" var="carer">
            <tr>
                <td>
                    <spring:url value="/carers/{carerId}" var="carerUrl">
                        <spring:param name="carerId" value="${carer.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(carerUrl)}"><c:out value="${carer.firstName} ${carer.lastName}"/></a>
                </td>
                <td>
                    <c:out value="${carer.salary}"/>
                </td>
                <td>
                    <c:out value="${carer.dni}"/>
                </td>
                <td>
                    <c:out value="${carer.telephone}"/>
                </td>
                <td>
                    <c:out value="${carer.email}"/>
                </td>
                <td>
                    <c:out value="${carer.isHairdresser}"/>
                </td>
                <td>
                <a href="<spring:url value="/carers/${carer.id}/delete" htmlEscape="true" />">
	                		<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
	                	</a>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>

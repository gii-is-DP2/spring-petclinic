<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="carer">

    <h2>Carer Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${carer.firstName} ${carer.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Salary</th>
            <td><b><c:out value="${carer.salary}"/></b></td>
        </tr>
        <tr>
            <th>DNI</th>
            <td><b><c:out value="${carer.dni}"/></b></td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td><b><c:out value="${carer.telephone}"/></b></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><b><c:out value="${carer.email}"/></b></td>
        </tr>
        <tr>
            <th>Hairdresser</th>
            <td><b><c:out value="${carer.isHairdresser}"/></b></td>
        </tr>

    </table>
    
    <a class="btn btn-default" href='<spring:url value="/carers/${carer.id}/edit" htmlEscape="true"/>'>Edit carer</a>    
    <a class="btn btn-default" href='<spring:url value="/carers/${carer.id}/delete" htmlEscape="true"/>'>Delete</a>    

</petclinic:layout>

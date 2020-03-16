<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trainers">

    <h2>Trainer Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${trainer.firstName} ${trainer.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Salary</th>
            <td><b><c:out value="${trainer.salary} ${trainer.salary}"/></b></td>
        </tr>
        <tr>
            <th>DNI</th>
            <td><b><c:out value="${trainer.dni} ${trainer.dni}"/></b></td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td><b><c:out value="${trainer.telephone} ${trainer.telephone}"/></b></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><b><c:out value="${trainer.email} ${trainer.email}"/></b></td>
        </tr>
        <tr>
            <th>Specialty</th>
            <td><b><c:out value="${trainer.specialty} ${trainer.specialty}"/></b></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><b><c:out value="${trainer.description} ${trainer.description}"/></b></td>
        </tr>
    </table>

</petclinic:layout>

<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="headers">
    <h2>
        <c:if test="${headers['new']}">Add </c:if> Trainer
    </h2>
    <form:form modelAttribute="trainer" class="form-horizontal" id="add-trainer-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="First Name" name="firstName"/>
            <petclinic:inputField label="Last Name" name="lastName"/>
            <petclinic:inputField label="Salary" name="salary"/>
            <petclinic:inputField label="DNI" name="dni"/>
            <petclinic:inputField label="Telephone" name="telephone"/>
            <petclinic:inputField label="Email" name="email"/>
            <petclinic:inputField label="Specialty" name="specialty"/>
            <petclinic:inputField label="Description" name="description"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${trainer['new']}">
                        <button class="btn btn-default" type="submit">Add Trainer</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Trainer</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>

<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="headers">
    <h2>
        <c:if test="${carer['new']}">Add </c:if> Carer
    </h2>
    <form:form modelAttribute="carer" class="form-horizontal" id="add-carer-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="First Name" name="firstName"/>
            <petclinic:inputField label="Last Name" name="lastName"/>
            <petclinic:inputField label="Salary" name="salary"/>
            <petclinic:inputField label="DNI" name="dni"/>
            <petclinic:inputField label="Telephone" name="telephone"/>
            <petclinic:inputField label="Email" name="email"/>
            <petclinic:selectField label="Hairdresser" name="isHairdresser" names="${isHairdresser}" size="1"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${carer['new']}">
                        <button class="btn btn-default" type="submit">Add Carer</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Carer</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>

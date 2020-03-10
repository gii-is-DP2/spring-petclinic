<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout currentPage="clients">
    <div class="container">
    <h2 class="mx-3 my-5">
        New client
    </h2>
    <form:form modelAttribute="client" class="form-horizontal" id="add-owner-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Surname" name="surname"/>
            <petclinic:inputField label="Document" name="document"/>
            <petclinic:selectField label="Document Type" name="documentType" size="3" names="${documentTypes}"/>
            <petclinic:inputField label="Health Insurance" name="healthInsurance"/>
            <petclinic:inputField label="Health Card Number" name="healthCardNumber"/>
            <petclinic:inputField label="Email" name="email"/>
            <petclinic:inputField label="Password" name="password"/>
            
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
               <button class="btn btn-primary" type="submit">Sign up</button>
            </div>
        </div>
    </form:form>
    </div>
</petclinic:layout>

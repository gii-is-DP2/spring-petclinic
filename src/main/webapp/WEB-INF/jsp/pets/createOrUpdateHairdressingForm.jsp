<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2><c:if test="${hairdressing['new']}">New </c:if>Hairdressing</h2>

        <b>Pet</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Birth Date</th>
                <th>Type</th>
                <th>Owner</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${hairdressing.pet.name}"/></td>
                <td><petclinic:localDate date="${hairdressing.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${hairdressing.pet.type.name}"/></td>
                <td><c:out value="${hairdressing.pet.owner.firstName} ${hairdressing.pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="hairdressing" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Date" name="date"/>
                <petclinic:inputField label="Description" name="description"/>
                <petclinic:selectField label="Care type" name="cuidado" size="2" names="${tiposCuidados}"></petclinic:selectField>
              	<petclinic:selectField label="Time" name="time" size="5" names="${horasDisponibles}"></petclinic:selectField>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                   	
                    <button class="btn btn-default" type="submit">Add Hairdressing Appointment</button>
                </div>
            </div>
        </form:form>

        <br/>
        <b>Previous Hairdressing Appointments</b>
        <table class="table table-striped">
            <tr>
                <th>Date</th>
                <th>Description</th>
                <th>Tipo de cuidado</th>
            </tr>
            <c:forEach var="hairdressing" items="${hairdressing.pet.visits}">
                <c:if test="${!hairdressing['new']}">
                    <tr>
                        <td><petclinic:localDate date="${hairdressing.date}" pattern="yyyy/MM/dd"/></td>
                        <td><c:out value="${hairdressing.description}"/></td>
                        <td><c:out value="${hairdressing.cuidado}"/></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>

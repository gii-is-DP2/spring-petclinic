<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="daycares">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2><c:if test="${daycare['new']}">New </c:if> Daycare
		</h2>
        <form:form modelAttribute="daycare" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Date" name="date"/>
                <petclinic:inputField label="Description" name="description"/>
                <petclinic:inputField label="Capacity" name="capacity"/>
            </div>
            <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${daycare['new']}">
                        <input type="hidden" name="petId" value="${daycare.pet.id}"/>
                        <button class="btn btn-default" type="submit">Add Daycare</button>
                    </c:when>
                    <c:otherwise>
                    	<input type="hidden" name="petId" value="${daycare.pet.id}"/>
                        <button class="btn btn-default" type="submit">Update Daycare</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        </form:form>

        <br/>

    </jsp:body>

</petclinic:layout>
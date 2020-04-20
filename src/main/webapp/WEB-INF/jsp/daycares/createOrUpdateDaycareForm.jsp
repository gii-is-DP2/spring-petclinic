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
        <h2><c:if test="${nuevo}">New </c:if> Daycare
		</h2>
        <form:form modelAttribute="daycareDTO" class="form-horizontal" id="add-training-form">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Date" name="date"/>
                <petclinic:inputField label="Description" name="description"/>

                    <div class="control-group">
                    	<petclinic:selectField name="petName" label="Pet Name" names="${pets}" size="${pets.size()}"/>
                	</div>

            </div>
            <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${nuevo}">
                        <input type="hidden" name="capacity" value="10"/>
                        <button class="btn btn-default" type="submit">Add Daycare</button>
                    </c:when>
                    <c:otherwise>
                    	<input type="hidden" name="capacity" value="10"/>
                        <button class="btn btn-default" type="submit">Update Daycare</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        </form:form>

        <br/>

    </jsp:body>

</petclinic:layout>
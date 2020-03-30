<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="hairdressing">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>Hairdressing</h2>

        

        <form:form modelAttribute="hairdressing" class="form-horizontal" action="/hairdressing/save" method="post">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Date" name="date"/>
                <petclinic:inputField label="Description" name="description"/>
                <petclinic:selectField label="Tipo de cuidado" name="cuidado" size="2" names="${tiposCuidados}"></petclinic:selectField>
                <petclinic:selectField label="Mascota" name="pet" size="3" names="${mascotas}"></petclinic:selectField>
                
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">Add Hairdressing Appointment</button>
            </div>
                
                
            </div>
        </form:form>

        <br/>
        
    </jsp:body>

</petclinic:layout>

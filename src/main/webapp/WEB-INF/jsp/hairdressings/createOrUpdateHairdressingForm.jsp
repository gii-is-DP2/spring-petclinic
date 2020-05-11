<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="headers">
	<jsp:attribute name="customScript">
        <script type="text/javascript">
									$(function() {
										$("#date").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
    </jsp:attribute>
	<jsp:body>
	    <h2>
	        <c:if test="${boton}">Add </c:if> Hairdressing
	    </h2>
	    <form:form modelAttribute="hairdressingDTO" class="form-horizontal"
				id="add-hairdressing-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Date" name="date" />
	            <petclinic:inputField label="Description" name="description" />
	            
	            
				<div class="control-group">
                    <petclinic:selectField name="time" label="Time" names="${availableTimes}" size="5"/>
                    <petclinic:selectField name="cuidado" label="Care Type" names="${tiposCuidados}" size="2"/>
                    <petclinic:selectField name="petName" label="Pet Name" names="${pets}" size="${pets.size()}"/>
                </div>	        
            </div>
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-10">
	                
		                <c:choose>
		                    <c:when test="${boton}">
		                        <button class="btn btn-default" type="submit">Add Hairdressing</button>
		                    </c:when>
		                    <c:otherwise>
		                        <button class="btn btn-default" type="submit">Update Hairdressing</button>
		                    </c:otherwise>
		                </c:choose>
	                
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>

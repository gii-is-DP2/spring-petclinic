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
	        <c:if test="${training['new']}">Add </c:if> Training
	    </h2>
	    <form:form modelAttribute="training" class="form-horizontal"
				id="add-training-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Date" name="date" />
	            <petclinic:inputField label="Description" name="description" />
	            <petclinic:inputField label="Pista" name="pista" />
				<div class="control-group">
                    <petclinic:selectField name="tipoPista" label="Tipo pista" names="${tipoPistas}" size="3"/>
                </div>	        
            </div>
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-10">
	                <c:choose>
	                    <c:when test="${training['new']}">
	                        <button class="btn btn-default" type="submit">Add Training</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Update Training</button>
	                    </c:otherwise>
	                </c:choose>
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>

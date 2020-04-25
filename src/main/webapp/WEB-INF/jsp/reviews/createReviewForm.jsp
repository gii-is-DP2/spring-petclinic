<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="headers">
	<jsp:body>
	    <h2>
	        Add Review
	    </h2>
	    <form:form modelAttribute="reviewDTO" class="form-horizontal" id="add-review-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Comments" name="comments" />
	            <petclinic:inputField label="Rating" type="number" minValue="1" name="rating" />
				<div class="control-group">
                    <petclinic:selectField name="serviceType" label="Service Type" names="${serviceTypes}" size="${serviceTypes.size()}"/>
                </div>	        
            </div>
	        <div class="form-group">
		        <div class="col-sm-offset-2 col-sm-10">
		            <button class="btn btn-default" type="submit">Submit Review</button>
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>

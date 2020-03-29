<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="name" required="true" rtexprvalue="true" description="Name of corresponding property in bean object" %>
<%@ attribute name="label" required="true" rtexprvalue="true" description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="names" required="true" rtexprvalue="true" type="java.util.List" description="Names in the list" %>
<%@ attribute name="size" required="true" rtexprvalue="true" description="Size of Select" %>
<%@ attribute name="itemValue" required="false" rtexprvalue="true" description="Item value of Select" %>
<%@ attribute name="itemLabel" required="false" rtexprvalue="true" description="Item label of Select" %>

<spring:bind path="${name}">
    <c:set var="cssGroup" value="form-group ${status.error ? 'error' : '' }"/>
    <c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
    <div class="${cssGroup}">
        <label class="col-sm-2 control-label">${label}</label>

        <div class="col-sm-10">
        	<c:if test="${itemValue == null || itemLabel == null}">
        		<form:select class="form-control" path="${name}" items="${names}" size="${size}"/>
        	</c:if>
            <c:if test="${itemValue != null && itemLabel != null}">
        		<form:select class="form-control" path="${name}" size="${size}">
        			<form:options items="${names}" itemValue="${itemValue}" itemLabel="${itemLabel}" />
        		</form:select>
        	</c:if>
            
            <c:if test="${valid}">
                <span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
            </c:if>
            <c:if test="${status.error}">
                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                <span class="help-inline">${status.errorMessage}</span>
            </c:if>
        </div>
    </div>
</spring:bind>

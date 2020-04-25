<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="trainers">
    <h2>Reviews</h2>
    
	<sec:authorize access="hasAuthority('owner')">
    	<a class="btn btn-default" href='<spring:url value="reviews/new" htmlEscape="true"/>'>Add review</a>
    	<br><br>
	</sec:authorize>

    <table id="reviewsTable" class="table table-striped">
        <thead>
	        <tr>
	            <th>Owner</th>
	            <th>Date</th>
	            <th>Service type</th>
	            <th>Rating</th>
	            <th>Comments</th>
        	</tr>
        </thead>
        <tbody>
        
        <c:forEach items="${reviews}" var="review">
            <tr>
                <td>
                    <c:out value="${review.owner.firstName} ${review.owner.lastName}"/>
                </td>
                <td>
                    <c:out value="${review.date}"/>
                </td>
                <td>
                    <c:out value="${review.serviceType}"/>
                </td>
                <td>
                	<c:forEach begin="1" end="${review.rating}">
    					<span class="glyphicon glyphicon-star" aria-hidden="true"></span>
					</c:forEach>
                </td>
                <td>
                    <c:out value="${review.comments}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>

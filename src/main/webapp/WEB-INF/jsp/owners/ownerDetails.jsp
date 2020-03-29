<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Address</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>City</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>

    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Owner</a>

    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>

    <br/>
    <br/>
    <br/>

    <h2>Pets, Visits, Daycares and hairdressings</h2>
    The hairdressing appointments or daycares that are for tomorrow or today won't be deleted

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                            </td>
                            <td>
                            	<tr>
                            		<th>Hairdressing Appointment Date</th>
                            		<th>Time</th>
                            		<th>Description</th>
                            		<th>Tipo Cuidado</th>
                        		</tr>
                        	
                        		<c:forEach var="hairdressing" items="${pet.hairdressings}">
                            		<tr>
                                		<td><petclinic:localDate date="${hairdressing.date}" pattern="yyyy-MM-dd"/></td>
                                		<td><c:out value="${hairdressing.time}"/></td>
                                		<td><c:out value="${hairdressing.description}"/></td>
                                		
                                		<td><c:out value="${hairdressing.cuidado}"/></td>
                                		<td>
                                		
                                		
                                		
	                                		<spring:url value="/owners/{ownerId}/pets/{petId}/hairdressing/{hairdressingId}/delete" var="hairdressingUrl">
	                                    		<spring:param name="ownerId" value="${owner.id}"/>
	                                    		<spring:param name="petId" value="${pet.id}"/>
	                                    		<spring:param name="hairdressingId" value="${hairdressing.id}"/>
	                                		</spring:url>
	                                		<a href="${fn:escapeXml(hairdressingUrl)}">Delete Appointment</a>
                                		</td>
                            		</tr>
                        		</c:forEach>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/hairdressing/new" var="hairdressingUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(hairdressingUrl)}">Add Hairdressing Appointment</a>
                            </td>
                        </tr>
                    </table>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        
                        <tr>
                            <th>Daycare Date</th>
                            <th>Description</th>
                        </tr>
                        </thead>
                        <c:forEach var="daycare" items="${pet.daycares}">
                            <tr>
                                <td><petclinic:localDate date="${daycare.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${daycare.description}"/></td>
                                <td>
                    				<spring:url value="/owners/{ownerId}/pets/{petId}/daycares/{daycareId}" var="daycareUrl">
                                    	<spring:param name="ownerId" value="${owner.id}"/>
                                    	<spring:param name="petId" value="${pet.id}"/>
                        				<spring:param name="daycareId" value="${daycare.id}"/>
                    				</spring:url>
                    				<a href="${fn:escapeXml(daycareUrl)}"><c:out value="Show Daycare"/></a>
                				</td>
                				<td>
                            	<spring:url value="/owners/{ownerId}/pets/{petId}/daycares/{daycareId}/edit" var="editUrl">
       								<spring:param name="daycareId" value="${daycare.id}"/>
        							<spring:param name="ownerId" value="${owner.id}"/>
        							<spring:param name="petId" value="${pet.id}"/>
   							 	</spring:url>
    							<a href="${fn:escapeXml(editUrl)}"> <c:out value="Edit"/></a>
                        	 	</td>
                        	 	<td>
                            	<spring:url value="/owners/{ownerId}/pets/{petId}/deleteDaycare/{daycareId}" var="deleteUrl">
       								<spring:param name="daycareId" value="${daycare.id}"/>
        							<spring:param name="ownerId" value="${owner.id}"/>
        							<spring:param name="petId" value="${pet.id}"/>
   							 	</spring:url>
    							<a href="${fn:escapeXml(deleteUrl)}">Delete</a>
                        	 	</td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/daycares/new" var="daycareUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(daycareUrl)}">Add Daycare</a>
                            </td>
                            
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>

</petclinic:layout>

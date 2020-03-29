<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="hairdressing">
    <h2>Hairdressing</h2>

    <table id="hairdressingTable" class="table table-striped">
        <thead>
        <tr>
            
            <th>Date</th>
            <th>Care type</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${hairdressings}" var="hairdressing">
            <tr>
            	<td>
                	<petclinic:localDate date="${hairdressing.date}" pattern="yyyy-MM-dd"/>
               	</td>
                <td>
                    <c:out value="${hairdressing.cuidado}"/>
                </td>
                <td>
                    <spring:url value="/hairdressing/delete/{hairdressingId}" var="hairdressingDeleteUrl">
                        <spring:param name="hairdressingId" value="${hairdressing.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(hairdressingDeleteUrl)}">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
        
    </table>
    <td>
      <spring:url value="/hairdressing/new" var="hairdressingCreateUrl">
      </spring:url>
      <a href="${fn:escapeXml(hairdressingCreateUrl)}">New Hairdressing Appointment</a>
    </td>
</petclinic:layout>
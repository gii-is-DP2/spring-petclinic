<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="pageTitle" required="false" %>

<head>
    <!-- Required meta tags -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%-- The above 4 meta tags *must* come first in the head; any other head content must come *after* these tags --%>

    <spring:url value="/resources/images/favicon.png" var="favicon" />
    <link rel="shortcut icon" type="image/x-icon" href="${favicon}">

    <%-- CSS generated from LESS --%>
    <spring:url value="/resources/css/petclinic.css" var="petclinicCss" />
    <link href="${petclinicCss}" rel="stylesheet" />

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
        integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

    <!-- Only datepicker is used -->
    <%-- <spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.min.css" var="jQueryUiCss" />
    <link href="${jQueryUiCss}" rel="stylesheet" />
    <spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.theme.min.css" var="jQueryUiThemeCss" />
    <link href="${jQueryUiThemeCss}" rel="stylesheet" /> --%>

    <title>${not empty pageTitle ? pageTitle : "Clinicas Corona"}</title>


</head>

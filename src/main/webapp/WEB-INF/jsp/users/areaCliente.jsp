<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout currentPage="clients">
	<div class="jumbotron landing">
		<div class="container">
			<h1 class="display-4" style="font-weight: normal;">Área clientes</h1>
		</div>
	</div>
    <div class="container">
   		<div class="jumbotron">
		<div class="container">
			<div class="row" style="text-align:center;">
				<div class="col-md-6" style="border-right:1px solid #ddd;">
					<h2>
						Ya soy cliente
					</h2>
					<p>Accede con tu <code>EMAIL</code> a tu cuenta para acceder a todas <br>las ventajas de <em>Corona</em><br></p>
					
					<a href="/login" class="btn btn-primary">Acceder</a>
				</div>
				<div class="col-md-6">
					<h2>
						¿No tienes cuenta?
					</h2>
					<p>Crea una cuenta para acceder a todas <br>las ventajas de <em>Corona</em></p>
					<a href="/clients/new" class="btn btn-primary">Regístrate</a>
				</div>
			</div>
		</div>
    </div>
    </div>
</petclinic:layout>

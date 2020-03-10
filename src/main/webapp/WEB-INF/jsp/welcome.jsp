<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

<petclinic:layout currentPage="home">
    <div class="jumbotron landing">
		<div class="container">
			<h1 class="display-4" style="font-weight: normal;">Clínicas <strong><em>Corona</em></strong></h1>
			<p class="lead">This is a simple hero unit, a simple jumbotron-style component for calling extra attention to
				featured content or information.</p>
			<hr class="my-4" style="height:2px;background-color:rgba(255,255,255,0.1);">
			<p>It uses utility classes for typography and spacing to space content out within the larger container.</p>
			<a class="btn btn-primary btn-lg" href="#" role="button">> Citación online</a>
		</div>
    </div>
    <div class="container">
        <h2>More info here</h2>
        <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque purus diam, viverra a sagittis eu,
            mattis eu tortor. Morbi malesuada at ipsum pellentesque ultrices. Praesent id porttitor lacus. Vestibulum
            sit amet enim in massa commodo laoreet. Duis faucibus ante condimentum, feugiat magna sed, tincidunt urna.
            Duis varius consectetur tempor. Sed consequat interdum magna at placerat. Nam gravida augue justo, sed
            convallis orci aliquet quis. Ut ut lorem quam. Aenean nec dolor neque. Etiam ultrices imperdiet urna eget
            euismod. Fusce consequat malesuada libero aliquam scelerisque. Nunc tincidunt tellus at enim gravida
            scelerisque. Integer luctus tortor et massa aliquet porttitor. Phasellus blandit maximus orci imperdiet
            imperdiet. Nulla in enim et odio vulputate dignissim.
		</p>
        <p>
		    Integer vestibulum nisl vel lorem ultrices, posuere porttitor quam tempor. Integer suscipit rutrum velit at
            elementum. Sed vestibulum interdum elit vitae aliquet. Aenean cursus velit sed urna imperdiet scelerisque.
            Nullam sit amet ante tempor tortor faucibus hendrerit. Maecenas ultrices sit amet nulla sit amet rhoncus.
            Praesent ipsum quam, placerat id eleifend et, congue vel sem. Quisque a lacus tempor, auctor purus vehicula,
            congue magna. Phasellus imperdiet urna in est ultricies, eget congue augue vehicula. Pellentesque vel est
            sit amet sapien pellentesque dictum. Cras ultrices commodo quam quis congue. Praesent a sagittis libero.
            Donec iaculis nunc ac elit aliquet ullamcorper. Aenean pellentesque turpis sit amet quam dapibus, id
            accumsan urna venenatis.
		</p>
		<p>
        	Sed eu consectetur velit. Duis pellentesque posuere erat, et interdum ligula pellentesque non. Vestibulum eu
            ultricies erat, ac dictum libero. Vivamus pulvinar quis lacus nec sodales. Phasellus eu lectus et ipsum
            viverra vestibulum. In hac habitasse platea dictumst. Aliquam porta mattis magna, nec efficitur ante luctus
            a. Morbi sed suscipit velit, id varius quam. Morbi finibus pharetra justo, non ultricies dolor suscipit nec.
            Curabitur feugiat augue velit. Pellentesque pellentesque velit nec ligula dignissim tempor. Quisque ut
            blandit turpis, vitae scelerisque nisl. Proin posuere ex quam, eget gravida urna pretium eu. Etiam interdum
            vel quam quis faucibus.
        </p>
    </div>

</petclinic:layout>

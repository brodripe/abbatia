<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <link rel="stylesheet" href="/theme/style-global.css" type="text/css"/>
    <link rel="stylesheet" href="/theme/style-revised.css" type="text/css"/>
    <title><bean:message key="poblacion.opcion.contratar_asesino"/></title>
</head>

<body>
<div id="container">
    <div id="content">
        <display:table name="requestScope.Contents" uid="lista_sicarios"
                       pagesize="30" class="contenidoedificio alignCenter">
            <display:caption><bean:message key="sicario.titulo"/></display:caption>
            <display:column titleKey="usuario.nombre" class="textLeft">
                <html:link action="/buscar_eminencias?opcion=1" paramId="clave" paramName="lista_sicarios"
                           paramProperty="idSicario">
                    ${lista_sicarios.nombre} ${lista_sicarios.apellido}
                </html:link>
            </display:column>
            <display:column property="obispos_muertos" titleKey="sicario.ase_obispo" class="alignCenter"/>
            <display:column property="cardenales_muertos" titleKey="sicario.ase_cardenal" class="alignCenter"/>
            <display:column property="papas_muertos" titleKey="sicario.ase_papa" class="alignCenter"/>
        </display:table>
        <!--publicidad inicio-->
        <jsp:include page="/varios/publicidad.jsp"/>
        <!--publicidad fin-->
    </div>
</div>

<%--Modulo de estadisticas de google--%>
<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-302272-1']);
    _gaq.push(['_setDomainName', 'abbatia.net']);
    _gaq.push(['_trackPageview']);
    (function() {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>
<!--Fin Script para google-analytics-->

</body>
<!-- Mostrar la abadia congelada -->
<jsp:include page="/abadias/congelado.jsp"/>
</html>

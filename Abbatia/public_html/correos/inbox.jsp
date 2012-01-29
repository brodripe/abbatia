<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>

<body>

<%--<div id="content">--%>
<div id="container">
    <html:form action="/borrarMensajes">
        <display:table name="requestScope.MensajeForm.correos" uid="correo_entrada" pagesize="30" export="true"
                       requestURI="/mensajes_recibidos.do" sort="list" class="contenidoedificio alignCenter">
            <display:setProperty name="export.xsl.filename" value="correo_entrada.xsl"/>
            <display:setProperty name="export.pdf.filename" value="correo_entrada.pdf"/>
            <display:setProperty name="export.csv.filename" value="correo_entrada.csv"/>
            <display:caption><bean:message key="mensajes.inbox.recibidos"/></display:caption>
            <display:column title="<input type='checkbox' name='CheckAll' onClick='checkOrUncheck()' />">
                <html:multibox name="MensajeForm" property="seleccion">
                    ${correo_entrada.idCorreo}
                </html:multibox>
            </display:column>
            <display:column property="abadiaDestino" titleKey="mensajes.inbox.table.origen" sortable="true"
                            class="textLeft"/>
            <display:column property="fecha_abadia" titleKey="mensajes.inbox.table.fecha_abadia"
                            format="{0,date,dd-MM-yyyy}" sortable="true"/>
            <display:column property="fecha_real" titleKey="mensajes.inbox.table.fecha_real"
                            format="{0,date,dd-MM-yyyy hh:mm}" sortable="true"/>
            <display:column titleKey="mensajes.inbox.table.msg" class="textLeft" maxLength="80">
                <html:link action="/mensajes_recibidos" paramName="correo_entrada" paramId="correoid"
                           paramProperty="idCorreo">
                    ${correo_entrada.texto}
                </html:link>
            </display:column>
            <display:column titleKey="mensajes.inbox.table.leido" sortable="true">
                <c:if test="${correo_entrada.estado == 'NO'}">
                    <html:img border="0" page="/images/iconos/16/sinleer.gif"
                              altKey="tooltip.sinleer"/>
                </c:if>
                <c:if test="${correo_entrada.estado == 'SI'}">
                    <html:img border="0" page="/images/iconos/16/leido.gif"
                              altKey="tooltip.leido"/>
                </c:if>
            </display:column>
            <display:column titleKey="mensajes.inbox.table.borrar">
                <html:link action="/borrarMensajes" paramName="correo_entrada" paramId="correoid"
                           paramProperty="idCorreo">
                    <layout:img border="0" page="/images/iconos/16/cancelar.jpg"
                                altKey="tooltip.borrar"/>
                </html:link>
            </display:column>
        </display:table>
        <div class="alignCenter">
            <html:submit>
                <bean:message key="outlook.menu.eliminarseleccion"/>
            </html:submit>
        </div>
    </html:form>
    <!--Inicio Script para google-analytics-->
    <script type="text/javascript">
        function checkOrUncheck() {
            if (document.MensajeForm.CheckAll.checked)
                checkAll(document.MensajeForm.seleccion);
            else
                uncheckAll(document.MensajeForm.seleccion);
        }

        function checkAll(aCheck) {
            for (i = 0; i < aCheck.length; i++)
                aCheck[i].checked = true;
        }

        function uncheckAll(aCheck) {
            for (i = 0; i < aCheck.length; i++)
                aCheck[i].checked = false;
        }

    </script>
    <!--Fin Script para google-analytics-->

    <!-- Mostrar la abadia congelada -->
    <jsp:include page="/abadias/congelado.jsp"/>
</div>
</body>
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
</html>

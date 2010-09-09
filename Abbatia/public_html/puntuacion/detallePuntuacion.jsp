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
    <display:table name="requestScope.puntuaciones" uid="historico_puntuacion" pagesize="30"
                   export="true" requestURI="/mostrarPuntuaciones.do" sort="list"
                   class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="historico_puntuacion.xsl"/>
        <display:setProperty name="export.pdf.filename" value="historico_puntuacion.pdf"/>
        <display:setProperty name="export.csv.filename" value="historico_puntuacion.csv"/>
        <display:caption><bean:message key="puntuacion.tabla.cabecera"/></display:caption>
        <display:column property="fechaAbadia" titleKey="puntuacion.tabla.fecha" sortable="true"/>
        <display:column property="monjesNivelL" titleKey="puntuacion.tabla.jerarquia" sortable="true"
                        class="textRight" format="{0,number,#,##0}"/>
        <display:column property="monjesHabilidadL" titleKey="puntuacion.tabla.habilidades" sortable="true"
                        class="textRight" format="{0,number,#,##0}"/>
        <display:column property="monjesActividadL" titleKey="puntuacion.tabla.actividades" sortable="true"
                        class="textRight" format="{0,number,#,##0}"/>
        <display:column property="monjesMuertosL" titleKey="puntuacion.tabla.monjesmuertos" sortable="true"
                        class="textRightRed" format="{0,number,#,##0}"/>
        <display:column property="santosL" titleKey="puntuacion.tabla.santos" sortable="true"
                        class="textRight" format="{0,number,#,##0}"/>
        <display:column property="animalesL" titleKey="puntuacion.tabla.animales" sortable="true"
                        class="textRight" format="{0,number,#,##0}"/>
        <display:column property="edificiosL" titleKey="puntuacion.tabla.edificios" sortable="true"
                        class="textRight" format="{0,number,#,##0}"/>
        <display:column property="librosL" titleKey="puntuacion.tabla.libros" sortable="true"
                        class="textRight" format="{0,number,#,##0}"/>
        <display:column property="totalL" titleKey="puntuacion.tabla.total" sortable="true"
                        class="textRight" format="{0,number,#,##0}"/>
        <display:column property="clasificacion" titleKey="puntuacion.tabla.clasificacion" sortable="true"
                        class="textRight"/>
        <display:column property="clasificacionRegional" titleKey="puntuacion.tabla.clasificacionregional"
                        sortable="true" class="textRight"/>
    </display:table>

    <%--</div>--%>
    <!--Inicio Script para google-analytics-->
    <script type="text/javascript">
        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
        document.write(unescape("%3Cscript src='" + gaJsHost +
                                "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    </script>
    <script type="text/javascript">
        var pageTracker = _gat._getTracker("UA-302272-1");
        pageTracker._initData();
        pageTracker._trackPageview();
    </script>
    <!--Fin Script para google-analytics-->

    <!-- Mostrar la abadia congelada -->
    <jsp:include page="/abadias/congelado.jsp"/>
</div>
<div id="container">
    <!-- Publicidad -->
    <logic:present name="usuario" scope="session">
        <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
            <jsp:include page="/varios/publicidad.jsp"/>
        </logic:notEqual>
    </logic:present>
</div>

</body>
</html>
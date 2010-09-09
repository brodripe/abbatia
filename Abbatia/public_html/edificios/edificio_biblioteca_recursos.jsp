<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>


<head>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>

<div id="container">
    <display:table name="requestScope.Edificio.contenido" uid="edificio_biblioteca_recursos" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do"
                   sort="list" class="contenidoedificio alignCenter">
        <%--<display:caption><bean:message key="edificios.abadia.biblioteca.titulo.recursos"/></display:caption>--%>
        <display:setProperty name="export.xsl.filename" value="edificio_biblioteca_recursos.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_biblioteca_recursos.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_biblioteca_recursos.csv"/>
        <display:column property="descripcion" titleKey="edificios.abadia.almacen.descripcion" sortable="true"
                        class="textLeft"/>
        <display:column property="cantidadF" titleKey="edificios.abadia.almacen.cantidad" sortable="true"/>
        <display:column property="barra_HTML" titleKey="edificios.abadia.almacen.estado"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <a href="javascript:vender('${edificio_biblioteca_recursos.recursoID}', 'R');">
                <html:img border="0" page="/images/iconos/16/vender.jpg"
                          altKey="tooltip.vender"/>
            </a>
        </display:column>
    </display:table>
</div>
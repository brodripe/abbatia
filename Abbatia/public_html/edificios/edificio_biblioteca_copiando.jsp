<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<head>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>

<div id="container">

    <display:table name="requestScope.Edificio.contenido" uid="edificio_biblioteca_copiando" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do"
                   sort="list" class="contenidoedificio alignCenter">
        <%--<display:caption><bean:message key="edificios.abadia.biblioteca.titulocopiando"/></display:caption>--%>
        <display:setProperty name="export.xsl.filename" value="edificio_biblioteca_copiando.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_biblioteca_copiando.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_biblioteca_copiando.csv"/>
        <display:column property="nombreRegion" titleKey="edificios.abadia.biblioteca.region" sortable="true"
                        class="textLeft"/>
        <display:column property="nombreAbadia" titleKey="edificios.abadia.biblioteca.abadia" sortable="true"/>
        <display:column property="nombreLibroNivel" titleKey="edificios.abadia.biblioteca.nombre" sortable="true"/>
        <display:column property="nombreMonje" titleKey="edificios.abadia.biblioteca.monje" sortable="true"/>
        <display:column property="descEstado" titleKey="edificios.abadia.biblioteca.estado"/>
        <display:column property="progreso" titleKey="edificio.abadia.biblioteca.progreso"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <html:link action="/mostrarDetalleLibro.do" paramName="edificio_biblioteca_copiando"
                       paramProperty="idLibro_origen" paramId="clave">
                <html:img border="0" page="/images/iconos/16/ojo.gif"
                          altKey="tooltip.detallelibro"/>
            </html:link>
            <logic:equal value="1" name="edificio_biblioteca_copiando" property="estado">
                <html:link action="/eliminarLibro" paramName="edificio_biblioteca_copiando" paramProperty="idLibro"
                           paramId="clave">
                    <html:img border="0" page="/images/iconos/16/cancelar.jpg"
                              altKey="tooltip.borrarlibro"/>
                </html:link>
                <html:link action="/mostrarMonjesCopia.do" paramName="edificio_biblioteca_copiando"
                           paramProperty="idLibro_origen" paramId="clave">
                    <html:img border="0" page="/images/iconos/16/copiar.gif"
                              altKey="tooltip.retomarcopia"/>
                </html:link>
            </logic:equal>
        </display:column>
    </display:table>
</div>

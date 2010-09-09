<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<head>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>

<div id="container">

    <display:table name="requestScope.consumos" uid="edificio_comedor" pagesize="20"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_comedor.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_comedor.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_comedor.csv"/>
        <display:caption><bean:message key="edificios.comedor.dieta.detalle"/></display:caption>
        <c:if test="${edificio_comedor.diferencia < 0}">
            <c:set var="estilo" value="textRightRed"/>
        </c:if>
        <c:if test="${edificio_comedor.diferencia >= 0}">
            <c:set var="estilo" value="textRight"/>
        </c:if>
        <display:column property="familiaDesc" titleKey="edificios.comedor.dieta.familia" sortable="true" group="1"
                        class="textLeft"/>
        <display:column titleKey="edificios.comedor.dieta.alimento" sortable="true"
                        class="textLeft">
            <a href="/mercado_compra_detalle.do?action=inicio&filtro=5&mercancia=1&filtrocontents=${edificio_comedor.alimentoDesc}"
               ondblclick="return false;">
                    ${edificio_comedor.alimentoDesc}
            </a>
        </display:column>
        <display:column property="disponible" titleKey="edificios.comedor.dieta.disponible" format="{0,number,#.###}"
                        class="textRight"/>
        <display:column property="requerido" titleKey="edificios.comedor.dieta.requisitosdiarios"
                        format="{0,number,#.###}" class="textRight"/>

        <display:column property="diferencia" titleKey="edificios.comedor.dieta.deficid" format="{0,number,#.###}"
                        class="${estilo}"/>
    </display:table>
</div>

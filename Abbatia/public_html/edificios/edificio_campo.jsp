<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
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

    <div id="bookFilter" class="alignCenter">
        <ul>
            <li>
                <html:link action="/nuevo_campo" styleClass="textCenter">
                    <bean:message key="edificios.abadia.campo.nuevo"/>
                </html:link>
            </li>
        </ul>
    </div>
    <div class="break"></div>
    <display:table name="requestScope.Edificio.contenido" uid="edificio_campo" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_campo.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_campo.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_campo.csv"/>
        <display:caption><bean:message key="edificios.abadia.campo.titulo"/></display:caption>
        <display:column property="fechaCreacion" titleKey="edificios.abadia.campo.fechacreacion" sortable="true"/>
        <display:column property="descEstado" titleKey="edificios.abadia.campo.estado" sortable="true"/>
        <display:column property="descAlimento" titleKey="edificios.abadia.campo.alimento" sortable="true"/>
        <display:column property="fechaInicioCultivo" titleKey="edificios.abadia.campo.cultivo.inicio" sortable="true"/>
        <display:column property="fechaFinCultivo" titleKey="edificios.abadia.campo.cultivo.fin" sortable="true"/>
        <display:column property="fechaInicioRecogida" titleKey="edificios.abadia.campo.recogida.inicio"
                        sortable="true"/>
        <display:column property="fechaFinRecogida" titleKey="edificios.abadia.campo.recogida.fin" sortable="true"/>
        <display:column property="numMonjes" titleKey="edificios.abadia.campo.nummonjes" sortable="true"/>
        <display:column property="barras_visualiza" titleKey="libro.detalle.tabla.avance" sortable="true"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <a href="javascript:mostrarMonjesAgricultura('${edificio_campo.idCultivo}');">
                <html:img border="0" page="/images/iconos/16/monjes.gif"
                          altKey="tooltip.campo.seleccionar.monjes"/>
            </a>
            <c:if test="${edificio_campo.idEstado == 2}">
                <a href="javascript:sembrarCampo('${edificio_campo.idCultivo}');">
                    <html:img border="0" page="/images/iconos/16/sembrar.gif" altKey="tooltip.sembrar"/>
                </a>
            </c:if>
            <c:if test="${edificio_campo.idEstado != 1}">
                <html:link action="/arar_campo" paramId="clave" paramName="edificio_campo" paramProperty="idCultivo">
                    <html:img border="0" page="/images/iconos/16/ararcampo.gif" altKey="tooltip.arar"/>
                </html:link>
            </c:if>
            <c:if test="${edificio_campo.idEstado == 1}">
                <c:if test="${edificio_campo.arado_total < 24000}">
                    <c:if test="${edificio_campo.arado_total > 1000}">
                        <a href="javascript:sembrarCampoPrematura('${edificio_campo.idCultivo}');">
                            <html:img border="0" page="/images/iconos/16/sembrar.gif"
                                      altKey="tooltip.sembrar.prematuro"/>
                        </a>
                    </c:if>
                </c:if>
            </c:if>
            <c:if test="${edificio_campo.siembra_total <= 0}">
                <c:if test="${edificio_campo.idEstado > 3}">
                    <html:link action="/cancelar_campo" paramId="clave" paramName="edificio_campo"
                               paramProperty="idCultivo">
                        <html:img border="0" page="/images/iconos/16/cancelar.jpg"
                                  altKey="tooltip.cancelarcampo"/>
                    </html:link>
                </c:if>
            </c:if>
        </display:column>
    </display:table>
</div>
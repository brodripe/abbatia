<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
            <li class="textCenter">
                <a href="javascript:molerCereales();">
                    <bean:message key="edificio.abadia.molino.moler"/>
                </a>
            </li>
        </ul>
    </div>
    <div class="break"></div>
    <display:table name="requestScope.Edificio.contenido" uid="edificio_molino" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_molino.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_molino.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_molino.csv"/>
        <display:caption><bean:message key="edificio.abadia.molino.titulo"/></display:caption>
        <display:column property="productoid_desc" titleKey="edificio.abadia.molino.tabla.producto" sortable="true"/>
        <display:column property="recursoid_desc" titleKey="edificio.abadia.molino.tabla.recurso" sortable="true"/>
        <display:column property="fecha_inicio" titleKey="edificio.abadia.molino.tabla.fecha_entrada" sortable="true"/>
        <display:column property="cantidad_ini" titleKey="edificio.abadia.molino.tabla.cantidadinicial"
                        format="{0,number,#,##0}" sortable="true"/>
        <display:column property="cantidad_act" titleKey="edificio.abadia.molino.tabla.cantidadactual"
                        format="{0,number,#,##0}" sortable="true"/>
        <display:column property="barra_HTML" titleKey="edificio.abadia.molino.tabla.estado" sortable="false"/>
    </display:table>
</div>

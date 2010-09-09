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
    <display:table name="requestScope.Edificio.contenido" uid="edificio_taller_costura" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_almacen.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_almacen.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_almacen.csv"/>
        <display:column  property="descripcion" titleKey="edificios.abadia.almacen.descripcion"
                        sortable="true" class="textLeft"/>
        <display:column property="cantidad" titleKey="edificios.abadia.almacen.cantidad" sortable="true"
                        format="{0,number,#,##0}" class="textRight"/>
        <display:column property="barra_HTML" titleKey="edificios.abadia.almacen.estado" sortable="true"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <a href="javascript:vender('${edificio_taller_costura.recursoID}', 'R');">
                <html:img border="0" page="/images/iconos/16/vender.jpg" altKey="tooltip.vender"
                          titleKey="tooltip.vender"/>
            </a>
        </display:column>
    </display:table>
</div>
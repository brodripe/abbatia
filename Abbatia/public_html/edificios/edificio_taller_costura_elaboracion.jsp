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

    <div id="bookFilter">
        <html:form action="/elaborarAlimentos" styleClass="alignCenter">
            <html:hidden property="accion" name="datosElaboracion" value="elaborar"/>
            <ul class="alignCenter">
                <li><bean:message key="edificios.abadia.cocina.elaboracion.recurso.seleccion"/></li>
                <li>
                    <html:select property="idProducto" name="datosElaboracion"
                                 titleKey="edificios.abadia.cocina.elaboracion.recurso.seleccion"
                                 onchange="elaborarAlimento(forms.datosElaboracion.idProducto.value);">
                        <html:options collection="elaborables" property="id" labelProperty="description"/>
                    </html:select>
                </li>
            </ul>
        </html:form>
    </div>
    <div class="break"></div>
    <display:table name="requestScope.Edificio.contenido" uid="edificio_costura_elaboracion" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_costura_elaboracion.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_costura_elaboracion.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_costura_elaboracion.csv"/>
        <display:column property="descProducto" titleKey="edificios.abadia.cocina.elaboracion.tabla.recurso"
                        sortable="true" class="textLeft"/>
        <display:column property="cantidad" format="{0,number,#,##0}"
                        titleKey="edificios.abadia.cocina.elaboracion.tabla.cantidad" sortable="true"
                        class="textRight"/>
        <display:column property="fecha_inicio"
                        titleKey="edificios.abadia.cocina.elaboracion.tabla.fecha_inicio"
                        sortable="true"/>
        <display:column property="descEstado"
                        titleKey="edificios.abadia.cocina.elaboracion.tabla.estado"
                        sortable="true"/>
        <display:column property="elaborado" format="{0,number,#,##0}" class="textRight"
                        titleKey="edificios.abadia.cocina.elaboracion.tabla.elaborado"
                        sortable="true"/>

        <display:column titleKey="edificios.abadia.tabla.opciones">
            <c:if test="${edificio_costura_elaboracion.estado == 1}">
                <html:link action="/elaboracion_pausar" paramName="edificio_costura_elaboracion"
                           paramProperty="idElaboracion" paramId="clave">
                    <html:img border="0" page="/images/iconos/16/pausar.gif"
                              altKey="tooltip.pausar"/>
                </html:link>
            </c:if>
            <c:if test="${edificio_costura_elaboracion.estado == 7}">
                <html:link action="/elaboracion_reanudar" paramName="edificio_costura_elaboracion"
                           paramProperty="idElaboracion" paramId="clave">
                    <html:img border="0" page="/images/iconos/16/reanudar.gif"
                              altKey="tooltip.reanudar"/>
                </html:link>
            </c:if>
            <html:link action="/elaboracion_eliminar" paramName="edificio_costura_elaboracion"
                       paramProperty="idElaboracion" paramId="clave">
                <html:img border="0" page="/images/iconos/16/eliminar.gif"
                          altKey="tooltip.eliminarelaboracion"/>
            </html:link>
        </display:column>
    </display:table>
</div>
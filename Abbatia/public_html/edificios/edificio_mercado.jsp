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
    <display:table name="requestScope.Edificio.contenido" uid="edificio_mercado" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_mercado.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_mercado.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_mercado.csv"/>
        <display:caption><bean:message key="mercado.venta.productosenventa"/></display:caption>
        <display:column property="descripcion" titleKey="mercado.venta.tabla.descripcion" sortable="true"
                        class="textLeft"/>
        <display:column property="fecha_inicial" titleKey="mercado.venta.tabla.inicio" sortable="true"/>
        <display:column property="fecha_final" titleKey="mercado.venta.tabla.finalizacion" sortable="true"/>
        <display:column property="cantidad" titleKey="mercado.venta.tabla.cantidad" sortable="true"/>
        <display:column property="precio" titleKey="mercado.venta.tabla.precio" sortable="true"/>
        <display:column property="volumenD" titleKey="mercado.venta.tabla.volumen" format="{0,number,#.###}"
                        sortable="true"/>
        <display:column property="precioTotalD" titleKey="mercado.venta.tabla.total" format="{0,number,#.###}"
                        sortable="true"/>
        <display:column titleKey="mercado.venta.tabla.opcion">
            <a href="javascript:cancelarVenta('${edificio_mercado.idProducto}');">
                <html:img border="0" page="/images/iconos/16/cancelar.jpg" altKey="mercado.venta.cancelar.titulo"/>
            </a>
            <a href="javascript:modificarVenta('${edificio_mercado.idProducto}');">
                <html:img border="0" page="/images/iconos/16/modificar.gif" altKey="mercado.modificar.venta.principal"/>
            </a>
            <a href="/mostrarHistoricoProducto.do?idproducto=${edificio_mercado.idProducto}">
                <html:img border="0" page="/images/iconos/16/historico.gif" altKey="mercado.modificar.venta.historico"/>
            </a>
        </display:column>
    </display:table>
</div>


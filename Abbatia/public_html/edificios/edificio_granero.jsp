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
    <display:table name="requestScope.Edificio.contenido" uid="edificio_granero" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_granero.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_granero.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_granero.csv"/>
        <display:caption><bean:message key="edificios.abadia.granero.cereales"/></display:caption>
        <display:column titleKey="edificios.abadia.cocina.alimentos.tabla.descripcion" sortable="true" class="textLeft">
            ${edificio_granero.familiaDescripcion} (<strong>${edificio_granero.descripcion}</strong>)
        </display:column>
        <display:column property="cantidad" titleKey="edificios.abadia.cocina.alimentos.tabla.cantidad"
                        sortable="true" format="{0,number,#,##0}" class="textRight"/>
        <display:column property="fechaCaducidad_desde"
                        titleKey="edificios.abadia.cocina.alimentos.tabla.fechacaducidad_ini" sortable="true"/>
        <display:column property="fechaCaducidad_hasta"
                        titleKey="edificios.abadia.cocina.alimentos.tabla.fechacaducidad_fin" sortable="true"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <a href="javascript:venderAgrupado('${edificio_granero.idAlimento}', 'A')">
                <html:img border="0" page="/images/iconos/16/vender.jpg" altKey="tooltip.vender"/>
            </a>
        </display:column>
    </display:table>
</div>

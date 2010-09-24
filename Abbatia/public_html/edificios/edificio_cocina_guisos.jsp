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
        <ul class="floatLeft">
            <li class="three">
                <c:if test="${sessionScope.pagesize==1000}">
                    <html:link
                            action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=10&Tab=guisos">
                        <bean:message key="edificios.abadia.cocina.alimentos.filtro.paginacion"/>
                    </html:link>
                </c:if>
                <c:if test="${sessionScope.pagesize!=1000}">
                    <html:link
                            action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=1000&Tab=guisos">
                        <bean:message key="edificios.abadia.cocina.alimentos.filtro.todos"/>
                    </html:link>
                </c:if>
            </li>
        </ul>
    </div>
    <div class="break"></div>
    <display:table name="requestScope.Edificio.contenido" uid="edificio_cocina_guisos"
                   pagesize="${sessionScope.pagesize}"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_cocina_guisos.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_cocina_guisos.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_cocina_guisos.csv"/>
        <display:column titleKey="edificios.abadia.cocina.alimentos.tabla.descripcion"
                        sortable="true" class="textLeft">
            ${edificio_cocina_guisos.familiaDescripcion} (<strong>${edificio_cocina_guisos.descripcion}</strong>)
        </display:column>
        <display:column property="cantidad" format="{0,number,#,##0}"
                        titleKey="edificios.abadia.cocina.alimentos.tabla.cantidad" sortable="true" class="textRight"/>
        <display:column property="fechaCaducidad_desde"
                        titleKey="edificios.abadia.cocina.alimentos.tabla.fechacaducidad_ini"
                        sortable="true"/>
        <display:column property="fechaCaducidad_hasta"
                        titleKey="edificios.abadia.cocina.alimentos.tabla.fechacaducidad_fin"
                        sortable="true"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <a href="javascript:venderAgrupado('${edificio_cocina_guisos.idAlimento}', 'A')">
                <html:img border="0" page="/images/iconos/16/vender.jpg" altKey="tooltip.vender"
                          titleKey="tooltip.vender"/>
            </a>
            <c:if test="${edificio_cocina_guisos.idAlimentoSalado > 0}">
                <a href="/salar_alimento.do?clave=${edificio_cocina_guisos.idAlimento}">
                    <html:img border="0" page="/images/iconos/16/salar.gif" altKey="tooltip.salar"
                              titleKey="tooltip.salar"/>
                </a>
            </c:if>
        </display:column>
    </display:table>
</div>

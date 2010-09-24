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
            <li class="alafirst">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=4&Tab=alacena">
                    <span class="meat"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.carnes"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=2&Tab=alacena">
                    <span class="wheat"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.cereales"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=5&Tab=alacena">
                    <span class="grape"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.frutas"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=10&Tab=alacena">
                    <span class="eggs"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.huevos"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=8&Tab=alacena">
                    <span class="milk"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.leches"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=6&Tab=alacena">
                    <span class="legum"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.legumbres"/>
                </html:link>
            </li>
        </ul>
        <ul class="floatLeft">
            <li class="alafirst">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=1&Tab=alacena">
                    <span class="bread"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.panes"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=3&Tab=alacena">
                    <span class="fish"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.pescados"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=9&Tab=alacena">
                    <span class="cheese"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.quesos"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&familia=7&Tab=alacena">
                    <span class="vege"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.verduras"/>
                </html:link>
            </li>
            <li class="alalast">
                <html:link
                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=1000&Tab=alacena">
                    <span class="ojo"></span><bean:message key="edificios.abadia.cocina.alimentos.filtro.todos"/>
                </html:link>
            </li>
        </ul>
    </div>
    <div class="break"></div>
    <display:table name="requestScope.Edificio.contenido" uid="edificio_cocina_alacena"
                   pagesize="${sessionScope.pagesize}"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_cocina_alacena.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_cocina_alacena.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_cocina_alacena.csv"/>
        <display:column titleKey="edificios.abadia.cocina.alimentos.tabla.descripcion"
                        sortable="true" class="textLeft">
            ${edificio_cocina_alacena.familiaDescripcion} (<strong>${edificio_cocina_alacena.descripcion}</strong>)
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
            <a href="javascript:venderAgrupado('${edificio_cocina_alacena.idAlimento}', 'A')">
                <html:img border="0" page="/images/iconos/16/vender.jpg" altKey="tooltip.vender"
                          titleKey="tooltip.vender"/>
            </a>
            <c:if test="${edificio_cocina_alacena.idAlimentoSalado > 0}">
                <a href="/salar_alimento.do?clave=${edificio_cocina_alacena.idAlimento}">
                    <html:img border="0" page="/images/iconos/16/salar.gif" altKey="tooltip.salar"
                              titleKey="tooltip.salar"/>
                </a>
            </c:if>
        </display:column>
    </display:table>
</div>

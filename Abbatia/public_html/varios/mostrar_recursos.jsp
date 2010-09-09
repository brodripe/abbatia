<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<logic:notEmpty name="DatosContents">
    <h4><bean:message key="principal.recursos"/></h4>
    <dl>
        <dt><span class="coin">&nbsp;</span></dt>
        <dd><bean:write name="DatosContents" property="recursoMonedas" filter="false"/></dd>
        <dt><span class="water">&nbsp;</span></dt>
        <dd><a href="/mercado_compra_detalle.do?action=inicio&filtro=5&mercancia=4&filtrocontents=Agua"><bean:write
                name="DatosContents" property="recursoAgua" filter="false"/></a></dd>
        <dt><span class="wood">&nbsp;</span></dt>
        <dd><a href="/mercado_compra_detalle.do?action=inicio&filtro=5&mercancia=4&filtrocontents=Madera"><bean:write
                name="DatosContents" property="recursoMadera" filter="false"/></a></dd>
        <dt><span class="stone">&nbsp;</span></dt>
        <dd><a href="/mercado_compra_detalle.do?action=inicio&filtro=5&mercancia=4&filtrocontents=Piedra"><bean:write
                name="DatosContents" property="recursoPiedra" filter="false"/></a></dd>
        <dt><span class="iron">&nbsp;</span></dt>
        <dd><a href="/mercado_compra_detalle.do?action=inicio&filtro=5&mercancia=4&filtrocontents=Hierro"><bean:write
                name="DatosContents" property="recursoHierro" filter="false"/></a></dd>
    </dl>
</logic:notEmpty>

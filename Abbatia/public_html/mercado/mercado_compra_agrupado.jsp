<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page pageEncoding="ISO-8859-1" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html xhtml="http://www.w3.org/1999/xhtml">
    <head>
        <title><bean:message key="mercado.compra.titulo"/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
        <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
    </head>

    <body>
    <div id="container">
        <div id="mercadoFiltro" class="alignCenter">
            <h4><bean:message key="mercado.venta.filtro"/></h4>
            <html:form action="/mercado_compra_agrupado" onsubmit="">
                <div class="floatLeft">
                    <ul>
                        <li><bean:message key="mercado.venta.mercancias"/></li>
                        <li>
                            <html:select name="BuscarMercadoAgrupadoCompraForm" property="mercancia">
                                <html:options collection="mercancia_tipo" property="id"
                                              labelProperty="description"/>
                            </html:select>
                        </li>
                        <logic:equal value="1" property="mercancia" name="BuscarMercadoAgrupadoCompraForm">
                            <li><bean:message key="mercado.venta.familias"/></li>
                            <li>
                                <html:select name="BuscarMercadoAgrupadoCompraForm" property="familiaAlimento">
                                    <html:option value="0"><bean:message key="principal.todas"/></html:option>
                                    <html:options collection="alimento_familia" property="id"
                                                  labelProperty="description"/>
                                </html:select>
                            </li>
                        </logic:equal>
                    </ul>
                </div>
                <div class="floatRight">
                    <ul>
                        <li>
                            <html:submit onclick="this.disabled=true; this.form.submit();">
                                <bean:message key="mercado.compra.buscar"/>
                            </html:submit>
                        </li>
                        <li>
                            <html:link href="mercado_compra_detalle.do" ondblclick="return false;">
                                <bean:message key="mercado.compra.avanzada"/>
                            </html:link>
                        </li>
                    </ul>
                </div>
            </html:form>
        </div>
        <display:table name="requestScope.MercadosListas.lstCompra" uid="compra_agrupado" pagesize="30"
                       export="true" requestURI="/mercado_compra_agrupado.do"
                       sort="list" class="contenidoedificio alignCenter">
            <display:setProperty name="export.xsl.filename" value="compra_agrupado.xsl"/>
            <display:setProperty name="export.pdf.filename" value="compra_agrupado.pdf"/>
            <display:setProperty name="export.csv.filename" value="compra_agrupado.csv"/>
            <display:caption><bean:message key="mercado.compra.tabla.cabecera"/></display:caption>
            <display:column titleKey="mercado.venta.tabla.descripcion" sortable="true" class="textLeft">
                <c:if test="${not empty compra_agrupado.familia}">
                    ${compra_agrupado.familia}:
                </c:if>
                <a href="/mercado_compra_detalle.do?action=inicio&filtro=5&mercancia=${compra_agrupado.mercancia}&filtrocontents=${compra_agrupado.descripcion}"
                   ondblclick="return false;">
                        ${compra_agrupado.descripcion}
                </a>
                <c:if test="${not empty compra_agrupado.nivel}">
                    (${compra_agrupado.nivel})
                </c:if>
            </display:column>
            <display:column property="ventas" titleKey="mercado.venta.tabla.nrventas"/>
            <display:column titleKey="mercado.venta.tabla.ctotal" class="textRight">
                ${compra_agrupado.cantidad} ${compra_agrupado.unidadDescripcion}
            </display:column>
            <display:column property="precioMinimo" titleKey="mercado.venta.tabla.pminimo" class="textRight"/>
            <display:column property="precioMaximo" titleKey="mercado.venta.tabla.pmaximo" class="textRight"/>
            <display:column titleKey="edificios.abadia.tabla.opciones">
                <a href="/mercado_compra_detalle.do?action=inicio&filtro=5&mercancia=${compra_agrupado.mercancia}&filtrocontents=${compra_agrupado.descripcion}"
                   ondblclick="return false;">
                    <bean:message key="mercado.ver"/>
                </a>
            </display:column>
        </display:table>

            <%-- Publicidad --%>
        <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
            <jsp:include page="/varios/publicidad.jsp"/>
        </logic:notEqual>
            <%--Modulo de estadisticas de google--%>
        <script type="text/javascript">
            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-302272-1']);
            _gaq.push(['_setDomainName', 'abbatia.net']);
            _gaq.push(['_trackPageview']);
            (function() {
                var ga = document.createElement('script');
                ga.type = 'text/javascript';
                ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(ga, s);
            })();
        </script>
        <!--Fin Script para google-analytics-->
        <jsp:include page="/abadias/congelado.jsp"/>
    </div>
    </body>
</html:html>
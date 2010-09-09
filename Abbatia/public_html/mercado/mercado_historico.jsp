<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>

<body>

<div id="container">
    <div id="mercadoFiltro" class="alignCenter">
        <h4><bean:message key="mercado.venta.filtro"/></h4>
        <html:form action="/mercado_historico" onsubmit="">
            <div class="floatLeft">
                <ul>
                    <li><bean:message key="mercado.venta.mercancias"/></li>
                    <li>
                        <html:select name="MercadoAdminForm" property="mercancia">
                            <html:options collection="mercancia_tipo" property="id"
                                          labelProperty="description"/>
                        </html:select>
                    </li>
                </ul>
            </div>
            <div class="floatRight">
                <ul>
                    <li>
                        <html:submit onclick="this.disabled=true; this.form.submit();">
                            <bean:message key="mercado.compra.buscar"/>
                        </html:submit>
                    </li>
                </ul>
            </div>
        </html:form>
    </div>

    <div id="content">
        <display:table name="requestScope.compras" uid="historico_mercado_compras" pagesize="30"
                       export="true" requestURI="/mercado_historico.do" sort="list"
                       class="contenidoedificio alignCenter">
            <display:setProperty name="export.xsl.filename" value="historico_mercado.xsl"/>
            <display:setProperty name="export.pdf.filename" value="historico_mercado.pdf"/>
            <display:setProperty name="export.csv.filename" value="historico_mercado.csv"/>
            <display:caption><bean:message key="mercado.historico.titulo.compras"/></display:caption>
            <display:column property="abadia" titleKey="mercado.historico.abadia" sortable="true" class="textLeft"/>
            <display:column property="fecha" titleKey="mercado.historico.fecha" sortable="true"/>
            <display:column property="descripcion" titleKey="mercado.historico.descripcion" sortable="true"
                            class="textLeft"/>
            <display:column property="cantidadD" titleKey="mercado.historico.cantidad" sortable="true" class="textRight"
                            format="{0,number,#,##0}"/>
            <display:column property="precioUnidadD" titleKey="mercado.historico.punitario" sortable="true"
                            class="textRight"
                            format="{0,number,#,##0}"/>
            <display:column property="precioTotalD" titleKey="mercado.historico.ptotal" sortable="true"
                            class="textRight"
                            format="{0,number,#,##0}"/>
        </display:table>

        <display:table name="requestScope.ventas" uid="historico_mercado_ventas" pagesize="30"
                       export="true" requestURI="/mercado_historico.do" sort="list"
                       class="contenidoedificio alignCenter">
            <display:setProperty name="export.xsl.filename" value="historico_mercado.xsl"/>
            <display:setProperty name="export.pdf.filename" value="historico_mercado.pdf"/>
            <display:setProperty name="export.csv.filename" value="historico_mercado.csv"/>
            <display:caption><bean:message key="mercado.historico.titulo.ventas"/></display:caption>
            <display:column property="abadia" titleKey="mercado.historico.abadia" sortable="true" class="textLeft"/>
            <display:column property="fecha" titleKey="mercado.historico.fecha" sortable="true"/>
            <display:column property="descripcion" titleKey="mercado.historico.descripcion" sortable="true"
                            class="textLeft"/>
            <display:column property="cantidadD" titleKey="mercado.historico.cantidad" sortable="true" class="textRight"
                            format="{0,number,#,##0}"/>
            <display:column property="precioUnidadD" titleKey="mercado.historico.punitario" sortable="true"
                            class="textRight"
                            format="{0,number,#,##0}"/>
            <display:column property="precioTotalD" titleKey="mercado.historico.ptotal" sortable="true"
                            class="textRight"
                            format="{0,number,#,##0}"/>
        </display:table>
        <%-- Publicidad --%>
        <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
            <jsp:include page="/varios/publicidad.jsp"/>
        </logic:notEqual>

        <!--Inicio Script para google-analytics-->
        <script type="text/javascript">
            var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
            document.write(unescape("%3Cscript src='" + gaJsHost +
                                    "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        </script>
        <script type="text/javascript">
            var pageTracker = _gat._getTracker("UA-302272-1");
            pageTracker._initData();
            pageTracker._trackPageview();
        </script>
        <!--Fin Script para google-analytics-->
    </div>
</div>
</body>
<jsp:include page="/abadias/congelado.jsp"/>
</html>

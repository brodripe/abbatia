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
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
    <script type="text/javascript" src="/theme/validator.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/ajaxcontentmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/iframecontentmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws_draggable.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws_scroll.js"></script>
    <script type="text/javascript" src="/theme/popup.js"></script>
    <html:javascript formName="MercadoCompraForm" dynamicJavascript="true" staticJavascript="false"/>
</head>
<body>
<div id="container">
    <div id="resources" class="alignCenter column">
       <dl>
            <dt><span class="coin">&nbsp;</span></dt>
            <dd>${monedas}</dd>
        </dl>
    </div>
    
    <div id="detalleCompra">
        <html:form action="/mercado_comprar" onsubmit="return validateMercadoCompraForm(this);">
        <html:hidden property="accion" value="comprar"/>
        <div class="alignCenter">
            <h4><bean:message key="mercado.compra2.titulo"/></h4>
            <dl>
                <dt><bean:message key="mercado.compra2.producto"/></dt>
                <dd><strong>${MercadoCompraForm.descripcionFamilia}</strong> (${MercadoCompraForm.descripcionProducto})</dd>
                <dt><bean:message key="mercado.compra2.cantidad"/></dt>
                <dd>${MercadoCompraForm.cantidadDisp} ${MercadoCompraForm.descripcionUnidad}</dd>
                <dt><bean:message key="mercado.compra2.preciounidad"/></dt>
                <dd>${MercadoCompraForm.precio}</dd>
                <dt><bean:message key="mercado.compra2.preciototal"/></dt>
                <dd>${MercadoCompraForm.precioTotal}</dd>
                <dt><bean:message key="mercado.compra2.cantidad"/></dt>
                <dd><html:text property="cantidad" size="5"/></dd>
            </dl>
        </div>
        <div class="break"></div>
        <div class="alignCenter downSpace">
            <html:checkbox property="volverAlMercado">
                <bean:message key="mercado.compra2.volveralmercado"/>
            </html:checkbox>
        </div>
        <div class="break"></div>
        <div class="floatLeft downSpace">
            <logic:notEmpty name="MercadoCompraForm" property="impuestoRegion">
                <h6><bean:message key="mercado.compra.comisiones.transito"/></h6>
                <c:forEach var="impuesto" items="${MercadoCompraForm.impuestoRegion}">
                    <ul>
                        <li>${impuesto.nombreRegion} (${impuesto.valorImpuesto}%)</li>
                    </ul>
                </c:forEach>
            </logic:notEmpty>
        </div>
        <div class="break"></div>            
        <div class="alignCenter topLineBorder">
            <input type="button" onClick="window.parent.cClick();"
                   value="<bean:message key="mercado.compra2.cerrar"/>"/>&nbsp;
            <html:submit>
                <bean:message key="mercado.compra2.comprar"/>
            </html:submit>
        </div>
      </html:form>
   </div>
</div>
</body>

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

<%--</body>--%>
</html:html>

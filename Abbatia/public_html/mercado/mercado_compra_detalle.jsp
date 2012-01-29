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
        <link rel="stylesheet" href="/theme/popup.css" type="text/css">
        <script type="text/javascript" src="/theme/overlibmws/overlibmws.js"></script>
        <script type="text/javascript" src="/theme/overlibmws/ajaxcontentmws.js"></script>
        <script type="text/javascript" src="/theme/overlibmws/iframecontentmws.js"></script>
        <script type="text/javascript" src="/theme/overlibmws/overlibmws_draggable.js"></script>
        <script type="text/javascript" src="/theme/overlibmws/overlibmws_scroll.js"></script>
        <script type="text/javascript" src="/theme/popup.js"></script>

        <script language="JavaScript" type="text/javascript">
            <%-- Funciones javascript para la gestión de los filtros --%>
            <!--
            function resetear() {
            }

            function buscaPorFiltro(filtro,filtrocontents) {
              resetear();
              document.BuscarMercadoCompraForm.filtro.value=filtro;
              document.BuscarMercadoCompraForm.filtrocontents.value=filtrocontents;
              document.BuscarMercadoCompraForm.submit();
            }

            function ordenarLista(campo,sentido) {
              document.BuscarMercadoCompraForm.submit();
            }

            function saltarPagina(pagina) {
              document.BuscarMercadoCompraForm.submit();
            }
            function comprar(p_ProductoId)
            {
                openPopupIFrame('/mercado_comprar.do?pid=' + p_ProductoId,550,400,"CompraProducto");
            }

            -->
        </script>
    </head>

    <body>
    <div id="container">
            <%-- Formulario para las búsquedas --%>
        <div id="mercadoFiltro" class="alignCenter">
            <h4><bean:message key="mercado.venta.filtro"/></h4>
            <html:form action="/mercado_compra_detalle" onsubmit="">
                <div class="alignCenter">
                    <ul>
                        <li><bean:message key="mercado.compra.mercado"/></li>
                        <li>
                            <html:select property="mercado">
                                <html:options collection="mercado" property="id" labelProperty="description"/>
                            </html:select>
                        </li>
                    </ul>
                    <ul>
                        <li><bean:message key="mercado.venta.mercancias"/></li>
                        <li>
                            <html:select property="mercancia">
                                <html:options collection="mercancia_tipo" property="id"
                                              labelProperty="description"/>
                            </html:select>
                        </li>
                    </ul>
                </div>
                <%----%>
                <%--                <div class="break"></div>--%>
                <div class="floatLeft">
                    <ul>
                            <%--                        <li><bean:message key="mercado.compra.avanzada"/></li>--%>
                        <li>
                            <html:select property="filtro">
                                <html:options collection="filtro" property="id" labelProperty="description"/>
                            </html:select>
                        </li>
                        <li>
                            <html:text property="filtrocontents" size="30"
                                       onkeypress="if ((event.which ? event.which : event.keyCode) == 13) {   resetear();  }"/>
                        </li>
                    </ul>
                </div>
                <%----%>
                <div class="alignCenter">
                    <ul>
                        <li>
                            <html:submit property="mercado.compra.buscar" onclick="resetear();">
                                <bean:message key="mercado.compra.buscar"/>
                            </html:submit>
                        </li>
                        <li>
                            <html:link href="mercado_compra_agrupado.do" ondblclick="return false;">
                                <bean:message key="mercado.compra.veragrupado"/>
                            </html:link>
                        </li>
                    </ul>
                </div>
            </html:form>
        </div>
            <%--tabla de contenidos--%>
        <div class="break"></div>
        <display:table name="requestScope.MercadosListas.lstCompra" uid="compra_detalle" pagesize="30"
                       export="true" requestURI="/mercado_compra_detalle.do"
                       sort="list" class="contenidoedificio alignCenter">
            <display:setProperty name="export.xsl.filename" value="compra_detalle.xsl"/>
            <display:setProperty name="export.pdf.filename" value="compra_detalle.pdf"/>
            <display:setProperty name="export.csv.filename" value="compra_detalle.csv"/>
            <display:caption><bean:message key="mercado.compra.tabla.cabecera"/></display:caption>
            <display:column titleKey="mercado.compra.tabla.mercado" sortable="true">
                <c:choose>
                    <c:when test="${compra_detalle.region == ''}">
                        ${compra_detalle.abadia}
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:buscaPorFiltro('4','${compra_detalle.region}');">
                                ${compra_detalle.region}
                        </a>
                        <strong>Abadia ( <a href="javascript:buscaPorFiltro('3','${compra_detalle.abadia}');">
                                ${compra_detalle.abadia} </a> )
                        </strong>
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column property="fecha_inicial" titleKey="mercado.compra3.fechainicio"/>
            <display:column property="fecha_final" titleKey="mercado.compra3.fechafin"/>
            <display:column titleKey="mercado.compra3.desc" class="textLeft">
                <c:if test="${compra_detalle.familia != '' }">
                    ${compra_detalle.familia}:
                </c:if>
                <strong>
                    <a href="javascript:buscaPorFiltro('5','${compra_detalle.descripcion}');">
                            ${compra_detalle.descripcion}
                    </a>
                </strong>
                <c:if test="${compra_detalle.mercancia == 2 }">
                    ( ${compra_detalle.idAnimalNivel} )
                </c:if>
            </display:column>
            <c:if test="${BuscarMercadoCompraForm.mercancia == 2}">
                <display:column titleKey="mercado.compra3.salud">
                    ${compra_detalle.barra_HTML}
                </display:column>
            </c:if>
            <c:choose>
                <c:when test="${mercancia_tipo_f == 1}">
                    <display:column property="fecha_caduca" titleKey="mercado.compra3.caducidad"/>
                </c:when>
            </c:choose>
            <display:column titleKey="mercado.compra3.ctddispo" class="textRight" sortable="true">
                ${compra_detalle.ctd_actual} ${compra_detalle.unidad_desc}
            </display:column>
            <display:column property="precio" titleKey="mercado.compra3.punidad" class="textRight" sortable="true"/>
            <display:column titleKey="edificios.abadia.tabla.opciones">
                <c:choose>
                    <c:when test="${compra_detalle.ctd_actual > 0}">
                        <a href="javascript:comprar('${compra_detalle.idProducto}')" ondblclick="return false;">
                            <html:img border="0" page="/images/iconos/16/comprar.jpg"
                                      altKey="mercado.compra2.titulo"/>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <html:img border="0" page="/images/iconos/16/comprar.jpg"
                                  altKey="mercado.compra2.titulo"/>
                    </c:otherwise>
                </c:choose>
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>
        <bean:message key="edificios.abadia.mostrar.titulo"/>
    </title>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/layout.css" TYPE="text/css"/>
    <link rel="STYLESHEET" href="/theme/popup.css" type="text/css">
    <script type="text/javascript" src="/theme/overlibmws/overlibmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/ajaxcontentmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/iframecontentmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws_draggable.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws_scroll.js"></script>
    <script type="text/javascript" src="/theme/popup.js"></script>
    <script type="text/javascript" src="/theme/displaytagex.js"></script>
    <script type="text/javascript" src="/theme/prototype.js"></script>
    <script type="text/javascript" src="/theme/number-functions.js"></script>

    <script src="/theme/popup/prototype.js" type="text/javascript"></script>
    <script src="/theme/popup/effects.js" type="text/javascript"></script>
    <script src="/theme/popup/dragdrop.js" type="text/javascript"></script>
    <script src="/theme/popup/popup.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/theme/popup/popup.css" type="text/css"/>


    <script type="text/javascript">
        <!--
        function vender(p_ProductoId, p_MercTipo)
        {
            openPopupIFrame('/mercado_vender.do?lid=' + p_ProductoId + '&merc=' + p_MercTipo,700,390,"VentaProducto");
        }
        function venderAgrupado(p_ProductoId, p_MercTipo)
        {
            openPopupIFrame('/mercado_vender_agrupado_inicio.do?lid=' + p_ProductoId + '&merc=' + p_MercTipo,700,450,"VentaProductoAgrupado");
        }
        function sacrificarAnimal(p_Clave)
        {
            openPopupIFrame('/sacrificar_animal.do?clave=' + p_Clave,600,300,"SacrificarAnimal");
        }
        function cancelarVenta(p_ProductoId)
        {
            openPopupIFrame('/cancelar_venta_inicio.do?pid=' + p_ProductoId,600,300,"CancelarVenta");
        }
        function modificarVenta(p_ProductoId)
        {
            openPopupIFrame('/modificar_venta_inicio.do?pid=' + p_ProductoId,600,300,"ModificarVenta");
        }
        function elaborarAlimento(p_ProductoId)
        {
          if (p_ProductoId != 0)
          {
            openPopupIFrame('/mostrarRequisitosElaboracionAlimentos.do?pid=' + p_ProductoId + '&tipo=A',600,250,"ElaborarAlimentos");
          }
        }
        function elaborarRecurso(p_ProductoId)
        {
          if (p_ProductoId != 0)
          {
            openPopupIFrame('/mostrarRequisitosElaboracionAlimentos.do?pid=' + p_ProductoId + '&tipo=R',600,250,"ElaborarAlimentos");
          }
        }

        function molerCereales()
        {
            openPopupIFrame('/molerCereales_inicio.do',600,250,'MolerCereales');
        }

        function sembrarCampo(p_CampoId)
        {
            openPopupIFrame('/sembrar_campo.do?clave=' + p_CampoId,600,250,'SembrarCampo');
        }
        function sembrarCampoPrematura(p_CampoId)
        {
            openPopupIFrame('/siembraPrematura.do?clave=' + p_CampoId,600,250,'SembrarCampoPrematura');
        }

        function mostrarMonjesAgricultura(p_CampoId)
        {
            openPopupIFrame('/mostrarListaMonjesAgricultura.do?clave=' + p_CampoId,700,350,'MostrarMonjesAsignados');
        }

        function fijarPrecio(p_LibroId)
        {
            openPopupIFrame('/actualizarPrecioCopia.do?clave=' + p_LibroId,750,160,"Fijar Precio Libro");
        }

        function confirmarEliminacion()
        {
            return confirm('<bean:message key="edificios.abadia.biblioteca.confirmar.eliminacion"/>');
        }

        -->
    </script>

</head>


<body>
<div id="container">
<div id="edificioHeader">
    <h4><bean:message key="edificios.abadia.datosedificio"/></h4>

    <div class="floatLeft" style="width: 30%">
        <img alt="<bean:write name="Edificio" property="descripcion"/>"
             SRC="images/mapas/<bean:write name="Edificio" property="grafico_construccion"/>_<bean:write name="Edificio" property="nivel"/>.gif"/><br/>

        <h6><bean:write name="Edificio" property="nombre" filter="false"/></h6>
    </div>
    <div class="floatRight" style="width: 70%">
        <dl>
            <dt><bean:message key="edificios.abadia.fechaconstruccion"/></dt>
            <dd><bean:write name="Edificio" property="fechaDeConstruccion" filter="false"/></dd>
            <dt><bean:message key="edificios.abadia.mostrar.nivel"/></dt>
            <dd>
                <bean:write name="Edificio" property="nivel" filter="false"/>
                <logic:equal value="N" name="Edificio" property="enConstruccion">
                    <logic:present name="DatosNivel">
                        <html:link action="/SubirNivel" paramId="clave" paramName="Edificio"
                                   paramProperty="idDeEdificio">
                            ( <bean:message key="edificios.abadia.subirnivel"/>&nbsp;<bean:write
                                name="DatosNivel" property="nivel" filter="false"/> )
                        </html:link>
                    </logic:present>
                </logic:equal>
            </dd>
            <dt><bean:message key="edificios.abadia.mostrar.estado"/></dt>
            <dd><bean:write name="Edificio" property="barraEstado" filter="false"/></dd>
            <dt><bean:message key="edificios.abadia.mostrar.mantenimiento"/></dt>
            <dd>
                <html:link action="/mantenimientoEdificio">
                    <bean:write name="Edificio" property="mantenimientoDesc"/>
                </html:link>
            </dd>
            <dt><bean:message key="edificios.abadia.mostrar.almacenamiento"/></dt>
            <dd>
                <bean:write name="Edificio" property="almacenamientoActualStr" filter="false"/>/
                <bean:write name="Edificio" property="almacenamiento" filter="false"/>
                <strong> +<bean:write name="Edificio" property="almacenamiento_plus" filter="false"/></strong>
            </dd>
        </dl>
    </div>
    <div class="break"></div>
    <div class="alignCenter topLineBorder" style="width: 90%;margin-top: 10px;">
        <dl>
            <dt>
                <bean:message key="edificios.abadia.descripcion"/>:
            </dt>
            <dd>
                <bean:write name="Edificio" property="descripcion" filter="false"/>
            </dd>
        </dl>
    </div>

    <logic:equal value="S" name="Edificio" property="enConstruccion">
        <h5><bean:message key="edificios.abadia.enamplicacion"/></h5>

        <div class="alignCenter" style="width: 80%">
            <dl>
                <dt><bean:message key="edificios.abadia.fechaprevista"/></dt>
                <dd><bean:write name="Edificio" property="fechaFinPrevista" filter="false"/></dd>
            </dl>
        </div>
    </logic:equal>

</div>


<table>
    <TR>
        <TD colspan="4">
            <div align="center">
                <html:errors/>
            </div>
        </TD>
    </TR>
</table>
<br>

<div class="alignCenter">
<!-- Si se trata del oratorio.... -->
<logic:equal value="1" name="Edificio" property="idDeTipoDeEdificio">
    <jsp:include page="/edificios/edificio_oratorio.jsp"/>
</logic:equal>
<!-- Si se trata del dormitorio.... -->
<logic:equal value="3" name="Edificio" property="idDeTipoDeEdificio">
    <logic:equal value="init" name="Tab">
        <jsp:include page="/edificios/edificio_dormitorio.jsp"/>
    </logic:equal>
    <logic:equal value="monjes" name="Tab">
        <jsp:include page="/edificios/edificio_dormitorio_monjes.jsp"/>
    </logic:equal>
</logic:equal>
<!-- Si se trata del comedor.... -->
<logic:equal value="4" name="Edificio" property="idDeTipoDeEdificio">
    <div id="content">
        <jsp:include page="/edificios/edificio_comedor_dieta.jsp"/>
    </div>
</logic:equal>
<!-- Si se trata de la cocina.... -->
<logic:equal value="5" name="Edificio" property="idDeTipoDeEdificio">

    <div id="header">
        <ul>
            <c:choose>
                <c:when test="${Tab=='init' || Tab==null}">
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.alimentos.agrupados"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=elaboracion&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.elaboracion.titulo"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=salar&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.salar.titulo"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_cocina.jsp"/>
                </c:when>
                <c:when test="${Tab=='elaboracion'}">
                    <li><a href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.alimentos.agrupados"/></a></li>
                    <!---->
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=elaboracion&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.elaboracion.titulo"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=salar&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.salar.titulo"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_cocina_elaboracion.jsp"/>
                </c:when>
                <c:when test="${Tab=='salar'}">
                    <li><a href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.alimentos.agrupados"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=elaboracion&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.elaboracion.titulo"/></a></li>
                    <!---->
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=salar&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.salar.titulo"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_cocina_salar.jsp"/>
                </c:when>
            </c:choose>
        </ul>
    </div>
    <div id="content">
        <jsp:include page="${urlContent}"/>
    </div>
</logic:equal>
<!-- Si se trata de la biblioteca.... -->
<logic:equal value="6" name="Edificio" property="idDeTipoDeEdificio">
    <div id="header">
        <ul>
            <c:choose>
                <c:when test="${Tab=='init' || Tab==null}">
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.local"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=region&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.region"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=general&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.general"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=copias&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.copias"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=recursos&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.recursos"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_biblioteca.jsp"/>
                </c:when>
                <c:when test="${Tab=='region'}">
                    <li><a href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.local"/></a></li>
                    <!---->
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=region&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.region"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=general&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.general"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=copias&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.copias"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=recursos&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.recursos"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_biblioteca_region.jsp"/>
                </c:when>
                <c:when test="${Tab=='general'}">
                    <li><a href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.local"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=region&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.region"/></a></li>
                    <!---->
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=general&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.general"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=copias&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.copias"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=recursos&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.recursos"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_biblioteca_todos.jsp"/>
                </c:when>
                <c:when test="${Tab=='copias'}">
                    <li><a href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.local"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=region&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.region"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=general&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.general"/></a></li>
                    <!---->
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=copias&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.copias"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=recursos&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.recursos"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_biblioteca_copiando.jsp"/>
                </c:when>
                <c:when test="${Tab=='recursos'}">
                    <li><a href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.local"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=region&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.region"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=general&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.general"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=copias&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.copias"/></a></li>
                    <!---->
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=recursos&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.biblioteca.tab.recursos"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_biblioteca_recursos.jsp"/>
                </c:when>
            </c:choose>
        </ul>
    </div>
    <div id="content">
        <jsp:include page="${urlContent}"/>
    </div>
</logic:equal>


<!-- Si se trata del cementerio.... -->
<logic:equal value="26" name="Edificio" property="idDeTipoDeEdificio">
    <logic:equal value="velatorio" name="Tab">
        <jsp:include page="/edificios/edificio_cementerio_velatorio.jsp"/>
    </logic:equal>
    <logic:equal value="init" name="Tab">
        <jsp:include page="/edificios/edificio_cementerio_velatorio.jsp"/>
    </logic:equal>
    <logic:equal value="cementerio" name="Tab">
        <jsp:include page="/edificios/edificio_cementerio_enterrados.jsp"/>
    </logic:equal>
    <logic:equal value="osario" name="Tab">
        <jsp:include page="/edificios/edificio_cementerio_osario.jsp"/>
    </logic:equal>
</logic:equal>

<!-- Si se trata del taller de costura.... -->
<logic:equal value="10" name="Edificio" property="idDeTipoDeEdificio">
    <div id="header">
        <ul>
            <c:choose>
                <c:when test="${Tab=='init' || Tab==null}">
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.contenido"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=elaboracion&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.elaboracion.titulo"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_taller_costura.jsp"/>
                </c:when>
                <c:when test="${Tab=='elaboracion'}">
                    <li><a href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.contenido"/></a></li>
                    <!---->
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=elaboracion&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.elaboracion.titulo"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_taller_costura_elaboracion.jsp"/>
                </c:when>
            </c:choose>
        </ul>
    </div>
    <div id="content">
        <jsp:include page="${urlContent}"/>
    </div>

</logic:equal>

<!-- Si se trata del taller de artesania.... -->
<logic:equal value="11" name="Edificio" property="idDeTipoDeEdificio">
    <div id="header">
        <ul>
            <c:choose>
                <c:when test="${Tab=='init' || Tab==null}">
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.contenido"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=elaboracion&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.elaboracion.titulo"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_taller_artesania.jsp"/>
                </c:when>
                <c:when test="${Tab=='elaboracion'}">
                    <li><a href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.contenido"/></a></li>
                    <!---->
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=elaboracion&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.elaboracion.titulo"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_taller_artesania_elaboracion.jsp"/>
                </c:when>
            </c:choose>
        </ul>
    </div>
    <div id="content">
        <jsp:include page="${urlContent}"/>
    </div>

</logic:equal>


<!-- Si se trata de la granero.... -->
<logic:equal value="8" name="Edificio" property="idDeTipoDeEdificio">
    <div id="content">
        <jsp:include page="/edificios/edificio_granero.jsp"/>
    </div>
</logic:equal>
<!-- Si se trata de la granja.... -->
<logic:equal value="7" name="Edificio" property="idDeTipoDeEdificio">
    <div id="content">
        <jsp:include page="/edificios/edificio_granja.jsp"/>
    </div>
</logic:equal>
<!-- Si se trata del establo.... -->
<logic:equal value="22" name="Edificio" property="idDeTipoDeEdificio">
    <div id="content">
        <jsp:include page="/edificios/edificio_establo.jsp"/>
    </div>
</logic:equal>
<!-- Si se trata del almacen.... -->
<logic:equal value="12" name="Edificio" property="idDeTipoDeEdificio">
    <div id="header">
        <ul>
            <c:choose>
                <c:when test="${Tab=='init' || Tab==null}">
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.contenido"/></a></li>
                    <!---->
                    <li><a href="/mostrarEdificio.do?Tab=elaboracion&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.elaboracion.titulo"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_almacen.jsp"/>
                </c:when>
                <c:when test="${Tab=='elaboracion'}">
                    <li><a href="/mostrarEdificio.do?Tab=init&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.contenido"/></a></li>
                    <!---->
                    <li id="selected"><a
                            href="/mostrarEdificio.do?Tab=elaboracion&clave=${Edificio.idDeEdificio}"><bean:message
                            key="edificios.abadia.cocina.elaboracion.titulo"/></a></li>
                    <!---->
                    <c:set var="urlContent" value="/edificios/edificio_almacen_elaboracion.jsp"/>
                </c:when>
            </c:choose>
        </ul>
    </div>
    <div id="content">
        <jsp:include page="${urlContent}"/>
    </div>

</logic:equal>
<!-- Si se trata de un campo.... -->
<logic:equal value="23" name="Edificio" property="idDeTipoDeEdificio">
    <div id="content">
        <jsp:include page="/edificios/edificio_campo.jsp"/>
    </div>
</logic:equal>
<!-- Si se trata de una iglesia.... -->
<logic:equal value="24" name="Edificio" property="idDeTipoDeEdificio">
    <jsp:include page="/edificios/edificio_iglesia.jsp"/>
</logic:equal>
<!-- Si se trata del molino.... -->
<logic:equal value="9" name="Edificio" property="idDeTipoDeEdificio">
    <div id="content">
        <jsp:include page="/edificios/edificio_molino.jsp"/>
    </div>
</logic:equal>
<logic:equal value="25" name="Edificio" property="idDeTipoDeEdificio">
    <div id="content">
        <jsp:include page="/edificios/edificio_mercado.jsp"/>
    </div>
</logic:equal>
</div>
<div id="container">
    <!-- Publicidad -->
    <logic:present name="usuario" scope="session">
        <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
            <jsp:include page="/varios/publicidad.jsp"/>
        </logic:notEqual>
    </logic:present>
</div>

</div>
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

</body>
</html>

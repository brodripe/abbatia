<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<%--@elvariable id="Monje" type="org.abbatia.bean.Monje"--%>
<html:html>
<head>
    <title>
        <bean:message key="monjes.abadia.datos"/>
    </title>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>

<body>
<div id="container">
    <div id="monjeHeader">
        <h4>
            <c:if test="${Monje.anteriorID != 0}">
                <html:link action="/mostrarMonje" paramId="clave" paramName="Monje" paramProperty="anteriorID"
                           styleClass="floatLeft">
                    <html:img border="0" page="/images/iconos/16/corner_left.gif" altKey="general.anterior"/>
                </html:link>
            </c:if>
            <bean:message key="monjes.abadia.datos"/>
            <c:if test="${Monje.siguienteID != 0}">
                <html:link action="/mostrarMonje" paramId="clave" paramName="Monje" paramProperty="siguienteID"
                           styleClass="floatRight">
                    <html:img border="0" page="/images/iconos/16/corner_right.gif" altKey="general.siguiente"/>
                </html:link>
            </c:if>
        </h4>

        <div class="floatLeft" style="width: 40%">
            <c:if test="${Monje.edad < 18}">
                <html:img border="0" page="/images/iconos/monje_1_${sessionScope.abadia.idDeOrden}.png"
                          altKey="general.siguiente"/>
            </c:if>
            <c:if test="${Monje.edad >= 18 && Monje.edad < 40}">
                <html:img border="0" page="/images/iconos/monje_2_${sessionScope.abadia.idDeOrden}.png"
                          altKey="general.siguiente"/>
            </c:if>
            <c:if test="${Monje.edad >= 40}">
                <html:img border="0" page="/images/iconos/monje_3_${sessionScope.abadia.idDeOrden}.png"
                          altKey="general.siguiente"/>
            </c:if>
            <p class="alignCenter"> ${Monje.HTMLEnfermedad} </p>
        </div>

        <div class="floatRight" style="width: 60%">
            <dl>
                <dt><bean:message key="monjes.abadia.nombre"/></dt>
                <dd>${Monje.nombre} <bean:message key="monjes.abadia.nomciudad"/> ${Monje.primerApellido}</dd>
                <dt><bean:message key="monjes.abadia.fecha_nacimiento"/></dt>
                <dd>${Monje.fechaDeNacimiento}</dd>
                <dt><bean:message key="monjes.abadia.edad"/></dt>
                <dd>${Monje.edad} <bean:message key="monjes.abadia.anyos"/></dd>
                <dt><bean:message key="monjes.abadia.fecha_entrada"/></dt>
                <dd>${Monje.fechaDeEntradaEnAbadia}</dd>
                <c:if test="${Monje.estado == 1}">
                    <dt><bean:message key="monjes.abadia.fecha_fallecimiento"/></dt>
                    <dd>${Monje.fechaDeFallecimiento}</dd>
                </c:if>
                <dt><bean:message key="monjes.abadia.jerarquia"/></dt>
                <dd>${Monje.jerarquia}</dd>
                <dd>
                    <c:if test="${hayabad == 0 && Monje.idDeJerarquia == 1 && Monje.estado == 0}">
                        <html:link action="/nombrarAbad" paramId="clave" paramName="Monje" paramProperty="idDeMonje">
                            ( <bean:message key="monjes.abadia.abad"/> )
                        </html:link>
                    </c:if>
                </dd>
            </dl>
        </div>

        <div class="break"></div>

        <div class="alignCenter">
            <c:if test="${Monje.idDeJerarquia == 1 && Monje.estado == 0}">
                <html:link action="/expulsarMonje" paramId="clave" paramName="Monje" paramProperty="idDeMonje">
                    <h6>( <bean:message key="monjes.abadia.expulsar"/> )</h6>
                </html:link>
            </c:if>
        </div>

        <div class="break"></div>

        <c:if test="${Monje.estado != 1}">
            <div class="alignCenter topLineBorder" style="width:90%">
                <div class="floatLeft">
                    <dl>
                        <dt class="corto"><bean:message key="monjes.abadia.salud"/></dt>
                        <dd>${Monje.barra_salud}</dd>
                        <dt class="corto"><bean:message key="monjes.abadia.fe"/></dt>
                        <dd>${Monje.barra_fe}</dd>
                    </dl>
                </div>
                <div class="floatRight">
                    <div class="floatLeft">
                        <dl>
                            <dt class="corto"><bean:message key="monjes.abadia.proteinas"/></dt>
                            <dd>${Monje.barra_proteinas}</dd>
                            <dt class="corto"><bean:message key="monjes.abadia.grasas"/></dt>
                            <dd>${Monje.barra_lipidos}</dd>
                        </dl>
                    </div>
                    <div class="floatRight">
                        <dl>
                            <dt class="corto"><bean:message key="monjes.abadia.carbono"/></dt>
                            <dd>${Monje.barra_hidratosCarbono}</dd>
                            <dt class="corto"><bean:message key="monjes.abadia.vitaminas"/></dt>
                            <dd>${Monje.barra_vitaminas}</dd>
                        </dl>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="break"></div>

        <div class="alignCenter topLineBorder" style="width:90%">
            <div class="floatLeft">
                <dl>
                    <dt class="corto"><bean:message key="monjes.abadia.idioma"/></dt>
                    <dd>${Monje.barra_idioma}</dd>
                    <dt class="corto"><bean:message key="monjes.abadia.talento"/></dt>
                    <dd>${Monje.barra_talento}</dd>
                    <dt class="corto"><bean:message key="monjes.abadia.carisma"/></dt>
                    <dd>${Monje.barra_carisma}</dd>
                </dl>
            </div>
            <div class="floatRight">
                <dl>
                    <dt class="corto"><bean:message key="monjes.abadia.sabiduria"/></dt>
                    <dd>${Monje.barra_sabiduria}</dd>
                    <dt class="corto"><bean:message key="monjes.abadia.fuerza"/></dt>
                    <dd>${Monje.barra_fuerza}</dd>
                    <dt class="corto"><bean:message key="monjes.abadia.destreza"/></dt>
                    <dd>${Monje.barra_destreza}</dd>
                </dl>
            </div>
        </div>
        <div class="break"></div>
    </div>
</div>

<div id="content" class="alignCenter" style="width:80%">
<!--- TAREAS DEL MONJE -->
<c:if test="${Monje.estado != 1 && Monje.estado != 2 && Monje.estado != 3 && Monje.nivelEnfermedad < 3}">

<html:form action="/actualizarDatosMonje">
<html:hidden property="idMonje"/>
<%--<h4><bean:message key="monjes.trabajos"/></h4>--%>
<table summary="<bean:message key="monjes.trabajos"/>" class="detallemonje alignCenter">
    <thead>
    <tr>
        <th colspan="8"><bean:message key="monjes.trabajos"/></th>
    <tr>
        <td>(0h 15m)</td>
        <td>(2h 15m)</td>
        <td>(7h 00m)</td>
        <td>(10h 00m)</td>
        <td>(12h 00m)</td>
        <td>(14h 00m)</td>
        <td>(16h 15m)</td>
        <td>(18h 45m)</td>
    </tr>
    <tr>
        <td><strong><bean:message key="monjes.trabajos.maitines"/></strong></td>
        <td><strong><bean:message key="monjes.trabajos.laudes"/></strong></td>
        <td><strong><bean:message key="monjes.trabajos.prima"/></strong></td>
        <td><strong><bean:message key="monjes.trabajos.tercia"/></strong></td>
        <td><strong><bean:message key="monjes.trabajos.sexta"/></strong></td>
        <td><strong><bean:message key="monjes.trabajos.nona"/></strong></td>
        <td><strong><bean:message key="monjes.trabajos.visperas"/></strong></td>
        <td><strong><bean:message key="monjes.trabajos.angelus"/></strong></td>
    </tr>
    <tr>
        <td><bean:message key="monjes.tarea.rezar"/></td>
        <td><bean:message key="monjes.tarea.rezar"/></td>
        <td>
            <c:if test="${MonjeForm.actPrima_bloqueado == 0}">
                <html:select name="MonjeForm" property="actPrima">
                    <html:options collection="actividades" property="id" labelProperty="description"/>
                </html:select>
            </c:if>
            <c:if test="${MonjeForm.actPrima_bloqueado == 1}">
                ${MonjeForm.actPrima_link}
                <html:hidden name="MonjeForm" property="actPrima"/>
            </c:if>
            <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje" paramProperty="idDeMonje">
                <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.ver"/>
            </html:link>
        </td>
        <td>
            <c:if test="${MonjeForm.actTercia_bloqueado == 0}">
                <html:select name="MonjeForm" property="actTercia">
                    <html:options collection="actividades" property="id" labelProperty="description"/>
                </html:select>
            </c:if>
            <c:if test="${MonjeForm.actTercia_bloqueado == 1}">
                ${MonjeForm.actTercia_link}
                <html:hidden name="MonjeForm" property="actTercia"/>
            </c:if>
            <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje" paramProperty="idDeMonje">
                <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.ver"/>
            </html:link>
        </td>
        <td><bean:message key="monjes.tarea.comer"/></td>
        <td>
            <c:if test="${MonjeForm.actNona_bloqueado == 0}">
                <html:select name="MonjeForm" property="actNona">
                    <html:options collection="actividades" property="id" labelProperty="description"/>
                </html:select>
            </c:if>
            <c:if test="${MonjeForm.actNona_bloqueado == 1}">
                ${MonjeForm.actNona_link}
                <html:hidden name="MonjeForm" property="actNona"/>
            </c:if>
            <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje" paramProperty="idDeMonje">
                <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.ver"/>
            </html:link>
        </td>
        <td>
            <c:if test="${MonjeForm.actVispera_bloqueado == 0}">
                <html:select name="MonjeForm" property="actVispera">
                    <html:options collection="actividades" property="id" labelProperty="description"/>
                </html:select>
            </c:if>
            <c:if test="${MonjeForm.actVispera_bloqueado == 1}">
                ${MonjeForm.actVispera_link}
                <html:hidden name="MonjeForm" property="actVispera"/>
            </c:if>
            <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje" paramProperty="idDeMonje">
                <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.ver"/>
            </html:link>
        </td>
        <td><bean:message key="monjes.tarea.rezar"/></td>
    </tr>
    <tr>
        <td>
            <c:if test="${Monje.actMaitines_completado == 1}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.realizado"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.actLaudes_completado == 1}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.realizado"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.actPrima_completado == 1}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.realizado"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.actTercia_completado == 1}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.realizado"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.actSexta_completado == 1}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.realizado"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.actNona_completado == 1}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.realizado"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.actVispera_completado == 1}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.realizado"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.actAngelus_completado == 1}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.realizado"/>
            </c:if>
        </td>
    </tr>
    </thead>
</table>

<table summary="<bean:message key="monjes.abadia.dieta"/>" class="detallemonje alignCenter">
    <thead>
    <tr>
        <th colspan="5"><bean:message key="monjes.abadia.dieta"/></th>
    <tr>
    <tr>
        <td><bean:message key="monjes.abadia.dieta1"/></td>
        <td><bean:message key="monjes.abadia.dieta2"/></td>
        <td><bean:message key="monjes.abadia.dieta3"/></td>
        <td><bean:message key="monjes.abadia.dieta4"/></td>
        <td><bean:message key="monjes.abadia.dieta5"/></td>
    </tr>
    <tr>
        <td>
            <html:select name="MonjeForm" property="comeFamiliaID1" disabled="true">
                <html:options collection="alifam" property="id" labelProperty="description"/>
            </html:select>
        </td>
        <td>
            <html:select name="MonjeForm" property="comeFamiliaID2" disabled="true">
                <html:options collection="alifam" property="id" labelProperty="description"/>
            </html:select>
        </td>
        <td>
            <html:select name="MonjeForm" property="comeFamiliaID3" disabled="true">
                <html:options collection="alifam" property="id" labelProperty="description"/>
            </html:select>
        </td>
        <td>
            <html:select name="MonjeForm" property="comeFamiliaID4" disabled="true">
                <html:options collection="alifam" property="id" labelProperty="description"/>
            </html:select>
        </td>
        <td>
            <html:select name="MonjeForm" property="comeFamiliaID5" disabled="true">
                <html:options collection="alifam" property="id" labelProperty="description"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td>
            <c:if test="${Monje.ha_comidoFamiliaID1 == -1}">
                <html:img border="0" page="/images/iconos/16/marca_roja.gif" altKey="tooltip.nocomido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID1 == 0}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.comido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID1 > 0}">
                <html:img border="0" page="/images/iconos/16/marca_azul.gif" altKey="tooltip.nocomidolomismo"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.ha_comidoFamiliaID2 == -1}">
                <html:img border="0" page="/images/iconos/16/marca_roja.gif" altKey="tooltip.nocomido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID2 == 0}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.comido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID2 > 0}">
                <html:img border="0" page="/images/iconos/16/marca_azul.gif" altKey="tooltip.nocomidolomismo"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.ha_comidoFamiliaID3 == -1}">
                <html:img border="0" page="/images/iconos/16/marca_roja.gif" altKey="tooltip.nocomido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID3 == 0}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.comido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID3 > 0}">
                <html:img border="0" page="/images/iconos/16/marca_azul.gif" altKey="tooltip.nocomidolomismo"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.ha_comidoFamiliaID4 == -1}">
                <html:img border="0" page="/images/iconos/16/marca_roja.gif" altKey="tooltip.nocomido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID4 == 0}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.comido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID4 > 0}">
                <html:img border="0" page="/images/iconos/16/marca_azul.gif" altKey="tooltip.nocomidolomismo"/>
            </c:if>
        </td>
        <td>
            <c:if test="${Monje.ha_comidoFamiliaID5 == -1}">
                <html:img border="0" page="/images/iconos/16/marca_roja.gif" altKey="tooltip.nocomido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID5 == 0}">
                <html:img border="0" page="/images/iconos/16/marca.gif" altKey="tooltip.comido"/>
            </c:if>
            <c:if test="${Monje.ha_comidoFamiliaID5 > 0}">
                <html:img border="0" page="/images/iconos/16/marca_azul.gif" altKey="tooltip.nocomidolomismo"/>
            </c:if>
        </td>
    </tr>
    <tr>
        <td>
            <ul>
                <c:forEach items="${Alimentos1}" var="alimento">
                    <li style="display:list-item;">
                            ${alimento.description}
                    </li>
                </c:forEach>
            </ul>
        </td>
        <td>
            <ul>
                <c:forEach items="${Alimentos2}" var="alimento">
                    <li style="display:list-item;">
                            ${alimento.description}
                    </li>
                </c:forEach>
            </ul>
        </td>
        <td>
            <ul>
                <c:forEach items="${Alimentos3}" var="alimento">
                    <li style="display:list-item;">
                            ${alimento.description}
                    </li>
                </c:forEach>
            </ul>
        </td>
        <td>
            <ul>
                <c:forEach items="${Alimentos4}" var="alimento">
                    <li style="display:list-item;">
                            ${alimento.description}
                    </li>
                </c:forEach>
            </ul>
        </td>
        <td>
            <ul>
                <c:forEach items="${Alimentos5}" var="alimento">
                    <li style="display:list-item;">
                            ${alimento.description}
                    </li>
                </c:forEach>
            </ul>
        </td>

    </tr>
    </thead>
    <tfoot>
    <tr>
        <th colspan="5">
            <!-- Sólo se puede modificar si está en tú abadia, si esta de visita en mi abadia y es un monje, o
si es un obispo, cardenal o papa y esta de visita en otra abadia ( la abadia destino no puede modificar ) -->
            <!-- ABADIA ORIGEN -->
            <c:if test="${Visita_miabadia == 0}">
                <c:if test="${Monje.estado == 0}">
                    <html:submit value="Modificar" property="accion"/>
                    <html:reset value="Restablecer"/>
                </c:if>
                <c:if test="${Monje.estado == 4}">
                    <c:if test="Monje.idDeJerarquia < 3">
                        <bean:message key="monjes.abadia.dieta.devisita"/>
                    </c:if>
                    <c:if test="Monje.idDeJerarquia > 2">
                        <html:submit value="Modificar" property="accion"/>
                        <html:reset value="Restablecer"/>
                    </c:if>
                </c:if>
            </c:if>
            <!-- ABADIA DESTINO, MONJE DE VISITA -->
            <c:if test="${Visita_miabadia == 1}">
                <c:if test="${Monje.idDeJerarquia < 3}">
                    <html:submit value="Modificar" property="accion"/>
                    <html:reset value="Restablecer"/>
                </c:if>
                <c:if test="${Monje.idDeJerarquia > 2}">
                    <bean:message key="monjes.abadia.dieta.visita"/>
                </c:if>
            </c:if>
        </th>
    </tr>
    </tfoot>
</table>
</html:form>

</c:if>

<div id="eventsMonje">
    <h4><bean:message key="monjes.abadia.mensajes"/></h4>
    <ul>
        <c:forEach items="${Monje.mensajesMonje}" var="ultamens">
            <li>${ultamens.description}</li>
        </c:forEach>
    </ul>
</div>
</div>
<div id="container">
    <!-- Publicidad -->
    <logic:present name="usuario" scope="session">
        <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
            <jsp:include page="/varios/publicidad.jsp"/>
        </logic:notEqual>
    </logic:present>
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
</html:html>

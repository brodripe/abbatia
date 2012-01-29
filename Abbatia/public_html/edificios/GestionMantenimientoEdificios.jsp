<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title><bean:message key="edificios.abadia.mantenimiento.titulo"/></title>
    <script src="/theme/prototype-1.6.0.2.js" type="text/javascript"></script>
    <script src="/theme/number-functions.js" type="text/javascript"></script>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>
<body>
<div id="container">
    <h3><bean:message key="edificios.abadia.mantenimiento.titulo"/></h3>
    <html:form action="/mantenimientoEdificio">
        <table summary="Datos de mantenimiento" class="maintenance">
            <thead>
            <tr>
                <th colspan="7"><bean:message key="edificios.abadia.mantenimiento.table.header"/></th>
            </tr>
            <tr>
                <th><bean:message key="edificios.abadia.mantenimiento.table.edificionivel"/></th>
                <th><bean:message key="edificios.abadia.mantenimiento.table.estado"/></th>
                <th width="10%"><bean:message key="edificios.abadia.mantenimiento.table.nulo"/></th>
                <th width="10%"><bean:message key="edificios.abadia.mantenimiento.table.basico"/></th>
                <th width="10%"><bean:message key="edificios.abadia.mantenimiento.table.moderado"/></th>
                <th width="10%"><bean:message key="edificios.abadia.mantenimiento.table.intensivo"/></th>
                <th width="10%"><bean:message key="edificios.abadia.mantenimiento.table.coste"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var='lista' items='${edificios}'>
                <%!String cssClass = null;%>
                <%
                    if (cssClass == null) {
                        cssClass = "one";
                    } else if (cssClass.equals("one")) {
                        cssClass = "two";
                    } else if (cssClass.equals("two")) {
                        cssClass = "one";
                    }

                %>
                <tr class="<%=cssClass%>">
                    <td>
                        <c:url value="/mostrarEdificio.do" var="url">
                            <c:param name="clave" value="${lista.idDeEdificio}"/>
                        </c:url>
                        <a href="${url}">
                                ${lista.nombre}(${lista.nivel})
                        </a>
                    </td>
                    <td>
                            ${lista.barraEstado}
                    </td>
                    <td>
                        <input type="radio" name="${lista.idDeTipoDeEdificio}"
                               id="${lista.idDeTipoDeEdificio}" value="0"
                                <c:if test="${lista.mantenimiento==0}">
                                    checked="checked"
                                </c:if>
                               onclick="actualizarValores(${lista.idDeTipoDeEdificio}, ${lista.nivel}, 0)"/>
                    </td>
                    <td>
                        <input type="radio" name="${lista.idDeTipoDeEdificio}"
                               id="${lista.idDeTipoDeEdificio}" value="1"
                                <c:if test="${lista.mantenimiento==1}">
                                    checked="checked"
                                </c:if>
                               onclick="actualizarValores(${lista.idDeTipoDeEdificio}, ${lista.nivel}, 1)"/>
                    </td>
                    <td>
                        <input type="radio" name="${lista.idDeTipoDeEdificio}"
                               id="${lista.idDeTipoDeEdificio}" value="2"
                                <c:if test="${lista.mantenimiento==2}">
                                    checked="checked"
                                </c:if>
                               onclick="actualizarValores(${lista.idDeTipoDeEdificio}, ${lista.nivel}, 2)"/>
                    </td>
                    <td>
                        <input type="radio" name="${lista.idDeTipoDeEdificio}"
                               id="${lista.idDeTipoDeEdificio}" value="3"
                                <c:if test="${lista.mantenimiento==3}">
                                    checked="checked"
                                </c:if>
                               onclick="actualizarValores(${lista.idDeTipoDeEdificio}, ${lista.nivel}, 3)"/>
                    </td>
                    <td id="totalLinea_${lista.idDeTipoDeEdificio}">
                        <fmt:formatNumber value="${lista.costeMantenimientoD}" type="number"/>
                    </td>
                    <td style="visibility:hidden;display:none" id="totalLineaN_${lista.idDeTipoDeEdificio}">
                            ${lista.costeMantenimientoD}
                    </td>

                </tr>
            </c:forEach>
            </tbody>
            <tfoot>
            <tr>
                <th colspan="7">
                    <html:submit property="aceptar">
                        <bean:message key="buscar.abadia.boton.aceptar"/>
                    </html:submit>
                </th>
            </tr>
            <logic:messagesPresent message="true">
                <tr>
                    <th colspan="7">
                        <html:messages id="msg" message="true">
                            <bean:write name="msg"/><br/>
                        </html:messages>
                    </th>
                </tr>
            </logic:messagesPresent>
            </tfoot>
            <tfoot>
            <tr>
                <th colspan="6" class="textRight"><bean:message key="edificios.abadia.mantenimiento.table.total"/></th>
                <th id="costeTotal"><bean:write name="costeMantenimiento" scope="request"/></th>
            </tr>
            </tfoot>
        </table>
    </html:form>

    <!--Inicio Script para google-analytics-->
    <script type="text/javascript">
        var counter = 0
        function actualizarValores(p_TipoEdificioId, p_Nivel, p_Mantenimiento) {
            var sumaTotal = 0;
            var sumaParcial = 0;
            console.log("actualizarValores(" + p_TipoEdificioId + ", " + p_Nivel + ", " + p_Mantenimiento + ")");
            document.body.addClassName('waiting');

            new Ajax.Request('<html:rewrite page="/json/actualizarValoresMantenimiento.do"/>', {
                parameters: {tipoEdificioId: p_TipoEdificioId, nivelId: p_Nivel, contador: ++counter},
                onComplete: function() {
                    document.body.removeClassName('waiting');
                },
                onSuccess: function(transport, json) {
                    if (json.executeError) {
                        //console.log(json.executeError);
                        alert(json.executeError);
                    }
                    else {
                        sumaParcial = json.coste * p_Mantenimiento;
                        $('totalLineaN_' + p_TipoEdificioId).innerHTML = sumaParcial;
                        $('totalLinea_' + p_TipoEdificioId).innerHTML = new String(sumaParcial.numberFormat('#,###')).replace(',', '.');
                        for (counter = 0; counter < 30; counter++) {
                            if ($('totalLinea_' + counter) != null) {
                                sumaParcial = $('totalLineaN_' + counter).innerHTML;
                                sumaTotal += parseFloat(sumaParcial);
                            }
                        }
                        $('costeTotal').innerHTML = new String(sumaTotal.numberFormat('#,###')).replace(',', '.');
                    }

                }
            });
        }
    </script>
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

</div>
<div id="container">
    <!-- Publicidad -->
    <logic:present name="usuario" scope="session">
        <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
            <jsp:include page="/varios/publicidad.jsp"/>
        </logic:notEqual>
    </logic:present>
</div>

</body>
</html>

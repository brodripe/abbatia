<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title></title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<p>&nbsp;</p>
<center>
<br/>
<table border="1" cellspacing="0" bordercolor="#000000" width="80%" bordercolorlight="#000000"
       bordercolordark="#000000">
<tr height="20">
    <td align="center" bgcolor="#996633">
        <b><font color="#FFFFFF" size="2">
            <bean:message key="solicitud.in.table.titulo"/>
        </font>
        </b>
    </td>
</tr>
<!-- Solicitudes de viaje IN -->
<tr>
    <td align="center" bgcolor="#E1C08B">
        <b><font color="#FFFFFF" size="2">
            <bean:message key="solicitud.in.table.viajes"/>
        </font>
        </b>
    </td>
</tr>
<tr>
    <td bgcolor="#E4CFA2">
        <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
               bordercolordark="#808080">
            <tr>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.creacion"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.abadia"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.mensaje"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.estado"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <font color="#FFFFFF">
                        <b>
                            <bean:message key="solicitud.in.table.fechacaduca"/>
                        </b>
                    </font>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.opciones"/>
                    </font>
                    </b>
                </td>
            </tr>
            <logic:iterate id="lista" name="solicitudes_terceros">
                <logic:equal name="lista" property="idTipoSolicitud" value="1">
                    <tr>
                        <td align="center">
                            <bean:write property="fechaCreacion" name="lista"/>
                        </td>
                        <td align="center">
                            <bean:write property="nombreAbadia" name="lista"/>
                        </td>
                        <td align="left">
                            <!--html:link action="solicitudDetalle.do" paramName="lista" paramId="solicitud" paramProperty="idSolicitud" -->
                            <bean:write property="texto" name="lista"/>
                            <!--/html:link-->
                        </td>
                        <td align="center">
                            <logic:equal name="lista" property="estado" value="0">
                                <bean:message key="solicitud.in.table.estado.pendiente"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="1">
                                <bean:message key="solicitud.in.table.estado.aceptada"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="2">
                                <bean:message key="solicitud.in.table.estado.denegada"/>
                            </logic:equal>
                        </td>
                        <td align="center">
                            <bean:write property="fechaCaduca" name="lista"/>
                        </td>
                        <td align="center">
                            <logic:equal name="lista" property="voto" value="0">
                                <logic:equal name="lista" property="estado" value="0">
                                    <html:link action="/solicitudSi" paramName="lista" paramId="solicitud"
                                               paramProperty="idSolicitud">
                                        <html:img border="0" page="/images/iconos/16/autorizar.gif"
                                                  altKey="tooltip.autorizar"/>
                                    </html:link>
                                    <html:link action="/solicitudNo" paramName="lista" paramId="solicitud"
                                               paramProperty="idSolicitud">
                                        <html:img border="0" page="/images/iconos/16/denegar.gif"
                                                  altKey="tooltip.denegar"/>
                                    </html:link>
                                </logic:equal>
                            </logic:equal>
                            <logic:equal name="lista" property="voto" value="1">
                                <html:img border="0" page="/images/iconos/16/autorizar.gif"
                                          altKey="tooltip.autorizada"/>
                            </logic:equal>
                            <logic:equal name="lista" property="voto" value="2">
                                <html:img border="0" page="/images/iconos/16/denegar.gif" altKey="tooltip.denegada"/>
                            </logic:equal>
                        </td>
                    </tr>
                </logic:equal>
            </logic:iterate>
        </table>
    </td>
</tr>
<!-- Solicitudes de autorizacion de viajes para copiar libros
<!-- Solicitudes de viaje IN -->
<tr>
    <td align="center" bgcolor="#E1C08B">
        <b><font color="#FFFFFF" size="2">
            <bean:message key="solicitud.in.table.viajes.copia"/>
        </font>
        </b>
    </td>
</tr>
<tr>
    <td bgcolor="#E4CFA2">
        <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
               bordercolordark="#808080">
            <tr>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.creacion"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.abadia"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.mensaje"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.estado"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <font color="#FFFFFF">
                        <b>
                            <bean:message key="solicitud.in.table.fechacaduca"/>
                        </b>
                    </font>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.opciones"/>
                    </font>
                    </b>
                </td>
            </tr>
            <logic:iterate id="lista" name="solicitudes_terceros_copia">
                <logic:equal name="lista" property="idTipoSolicitud" value="2">
                    <tr>
                        <td align="center">
                            <bean:write property="fechaCreacion" name="lista"/>
                        </td>
                        <td align="center">
                            <bean:write property="nombreAbadia" name="lista"/>
                        </td>
                        <td align="left">
                            <!--html:link action="solicitudDetalle.do" paramName="lista" paramId="solicitud" paramProperty="idSolicitud" -->
                            <bean:write property="texto" name="lista"/>
                            <!--/html:link-->
                        </td>
                        <td align="center">
                            <logic:equal name="lista" property="estado" value="0">
                                <bean:message key="solicitud.in.table.estado.pendiente"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="1">
                                <bean:message key="solicitud.in.table.estado.aceptada"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="2">
                                <bean:message key="solicitud.in.table.estado.denegada"/>
                            </logic:equal>
                        </td>
                        <td align="center">
                            <bean:write property="fechaCaduca" name="lista"/>
                        </td>
                        <td align="center">
                            <logic:equal name="lista" property="voto" value="0">
                                <logic:equal name="lista" property="estado" value="0">
                                    <html:link action="/solicitudSi" paramName="lista" paramId="solicitud"
                                               paramProperty="idSolicitud">
                                        <html:img border="0" page="/images/iconos/16/autorizar.gif"
                                                  altKey="tooltip.autorizar"/>
                                    </html:link>
                                    <html:link action="/solicitudNo" paramName="lista" paramId="solicitud"
                                               paramProperty="idSolicitud">
                                        <html:img border="0" page="/images/iconos/16/denegar.gif"
                                                  altKey="tooltip.denegar"/>
                                    </html:link>
                                </logic:equal>
                            </logic:equal>
                            <logic:equal name="lista" property="voto" value="1">
                                <html:img border="0" page="/images/iconos/16/autorizar.gif"
                                          altKey="tooltip.autorizada"/>
                            </logic:equal>
                            <logic:equal name="lista" property="voto" value="2">
                                <html:img border="0" page="/images/iconos/16/denegar.gif" altKey="tooltip.denegada"/>
                            </logic:equal>
                        </td>
                    </tr>
                </logic:equal>
            </logic:iterate>
        </table>
    </td>
</tr>

<!-- Solicitudes de voto para autorizaciones IN -->
<tr>
    <td align="center" bgcolor="#E1C08B">
        <b><font color="#FFFFFF" size="2">
            <bean:message key="solicitud.in.table.votaciones"/>
        </font>
        </b>
    </td>
</tr>
<tr>
    <td bgcolor="#E4CFA2">
        <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
               bordercolordark="#808080">
            <tr>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.creacion"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.abadia"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.mensaje"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.estado"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <font color="#FFFFFF">
                        <b>
                            <bean:message key="solicitud.in.table.fechacaduca"/>
                        </b>
                    </font>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.opciones"/>
                    </font>
                    </b>
                </td>
            </tr>
            <logic:iterate id="lista" name="solicitudes_terceros">
                <logic:equal name="lista" property="idTipoSolicitud" value="2">
                    <tr>
                        <td align="center">
                            <bean:write property="fechaCreacion" name="lista"/>
                        </td>
                        <td align="center">
                            <bean:write property="nombreAbadia" name="lista"/>
                        </td>
                        <td align="left">
                            <!--html:link action="solicitudDetalle.do" paramName="lista" paramId="solicitud" paramProperty="idSolicitud" -->
                            <bean:write property="texto" name="lista"/>
                            <!--/html:link-->
                        </td>
                        <td align="center">
                            <logic:equal name="lista" property="estado" value="0">
                                <bean:message key="solicitud.in.table.estado.pendiente"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="1">
                                <bean:message key="solicitud.in.table.estado.aceptada"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="2">
                                <bean:message key="solicitud.in.table.estado.denegada"/>
                            </logic:equal>
                        </td>
                        <td align="center">
                            <bean:write property="fechaCaduca" name="lista"/>
                        </td>
                        <td align="center">
                            <logic:equal name="lista" property="voto" value="0">
                                <logic:equal name="lista" property="estado" value="0">
                                    <html:link action="/solicitudSi" paramName="lista" paramId="solicitud"
                                               paramProperty="idSolicitud">
                                        <html:img border="0" page="/images/iconos/16/autorizar.gif"
                                                  altKey="tooltip.autorizar"/>
                                    </html:link>
                                    <html:link action="/solicitudNo" paramName="lista" paramId="solicitud"
                                               paramProperty="idSolicitud">
                                        <html:img border="0" page="/images/iconos/16/denegar.gif"
                                                  altKey="tooltip.denegar"/>
                                    </html:link>
                                </logic:equal>
                            </logic:equal>
                            <logic:equal name="lista" property="voto" value="1">
                                <html:img border="0" page="/images/iconos/16/autorizar.gif"
                                          altKey="tooltip.autorizada"/>
                            </logic:equal>
                            <logic:equal name="lista" property="voto" value="2">
                                <html:img border="0" page="/images/iconos/16/denegar.gif" altKey="tooltip.denegada"/>
                            </logic:equal>
                        </td>
                    </tr>
                </logic:equal>
            </logic:iterate>
        </table>
    </td>
</tr>
</table>
<!-- *************************************-->
<!-- Mostrar las solicitudes emitidas OUT -->
<!-- *************************************-->
<table border="1" cellspacing="0" bordercolor="#000000" width="80%" bordercolorlight="#000000"
       bordercolordark="#000000">
<tr height="20">
    <td align="center" bgcolor="#996633">
        <b><font color="#FFFFFF" size="2">
            <bean:message key="solicitud.out.table.titulo"/>
        </font>
        </b>
    </td>
</tr>
<!-- Solicitudes de viaje OUT -->
<tr>
    <td align="center" bgcolor="#E1C08B">
        <b><font color="#FFFFFF" size="2">
            <bean:message key="solicitud.out.table.viajes"/>
        </font>
        </b>
    </td>
</tr>
<tr>
    <td bgcolor="#E4CFA2">
        <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
               bordercolordark="#808080">
            <tr>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.creacion"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.mensaje"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.estado"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <font color="#FFFFFF">
                        <b>
                            <bean:message key="solicitud.in.table.fechacaduca"/>
                        </b>
                    </font>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.opciones"/>
                    </font>
                    </b>
                </td>
            </tr>
            <logic:iterate id="lista" name="solicitudes_propias">
                <logic:equal name="lista" property="idTipoSolicitud" value="1">
                    <tr>
                        <td align="center">
                            <bean:write property="fechaCreacion" name="lista"/>
                        </td>
                        <td align="left">
                            <!--html:link action="solicitudDetalle.do" paramName="lista" paramId="solicitud" paramProperty="idSolicitud" -->
                            <bean:write property="texto" name="lista"/>
                            <!--/html:link-->
                        </td>
                        <td align="center">
                            <logic:equal name="lista" property="estado" value="0">
                                <bean:message key="solicitud.in.table.estado.pendiente"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="1">
                                <bean:message key="solicitud.in.table.estado.aceptada"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="2">
                                <bean:message key="solicitud.in.table.estado.denegada"/>
                            </logic:equal>
                        </td>
                        <td align="center">
                            <bean:write property="fechaCaduca" name="lista"/>
                        </td>
                        <td align="center">
                            <html:link action="/solicitudEliminar" paramName="lista" paramId="solicitud"
                                       paramProperty="idSolicitud">
                                <html:img border="0" page="/images/iconos/16/cancelar.jpg" altKey="tooltip.eliminar"/>
                            </html:link>
                        </td>
                    </tr>
                </logic:equal>
            </logic:iterate>
        </table>
    </td>
</tr>


<!-- Solicitudes de viaje OUT para copiar libros-->
<tr>
    <td align="center" bgcolor="#E1C08B">
        <b><font color="#FFFFFF" size="2">
            <bean:message key="solicitud.out.table.viajes.copia"/>
        </font>
        </b>
    </td>
</tr>
<tr>
    <td bgcolor="#E4CFA2">
        <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
               bordercolordark="#808080">
            <tr>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.creacion"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.mensaje"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.estado"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <font color="#FFFFFF">
                        <b>
                            <bean:message key="solicitud.in.table.fechacaduca"/>
                        </b>
                    </font>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.opciones"/>
                    </font>
                    </b>
                </td>
            </tr>
            <logic:iterate id="lista" name="solicitudes_propias_copia">
                <logic:equal name="lista" property="idTipoSolicitud" value="2">
                    <tr>
                        <td align="center">
                            <bean:write property="fechaCreacion" name="lista"/>
                        </td>
                        <td align="left">
                            <!--html:link action="solicitudDetalle.do" paramName="lista" paramId="solicitud" paramProperty="idSolicitud" -->
                            <bean:write property="texto" name="lista"/>
                            <!--/html:link-->
                        </td>
                        <td align="center">
                            <logic:equal name="lista" property="estado" value="0">
                                <bean:message key="solicitud.in.table.estado.pendiente"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="1">
                                <bean:message key="solicitud.in.table.estado.aceptada"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="2">
                                <bean:message key="solicitud.in.table.estado.denegada"/>
                            </logic:equal>
                        </td>
                        <td align="center">
                            <bean:write property="fechaCaduca" name="lista"/>
                        </td>
                        <td align="center">
                            <html:link action="/solicitudEliminar" paramName="lista" paramId="solicitud"
                                       paramProperty="idSolicitud">
                                <html:img border="0" page="/images/iconos/16/cancelar.jpg" altKey="tooltip.eliminar"/>
                            </html:link>
                        </td>
                    </tr>
                </logic:equal>
            </logic:iterate>
        </table>
    </td>
</tr>


<!-- Solicitudes de voto para autorizaciones OUT -->
<tr>
    <td align="center" bgcolor="#E1C08B">
        <b><font color="#FFFFFF" size="2">
            <bean:message key="solicitud.out.table.votaciones"/>
        </font>
        </b>
    </td>
</tr>
<tr>
    <td bgcolor="#E4CFA2">
        <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
               bordercolordark="#808080">
            <tr>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.creacion"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.mensaje"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.estado"/>
                    </font>
                    </b>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <font color="#FFFFFF">
                        <b>
                            <bean:message key="solicitud.in.table.fechacaduca"/>
                        </b>
                    </font>
                </td>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="solicitud.in.table.opciones"/>
                    </font>
                    </b>
                </td>
            </tr>
            <logic:iterate id="lista" name="solicitudes_propias">
                <logic:equal name="lista" property="idTipoSolicitud" value="2">
                    <tr>
                        <td align="center">
                            <bean:write property="fechaCreacion" name="lista"/>
                        </td>
                        <td align="left">
                            <!--html:link action="solicitudDetalle.do" paramName="lista" paramId="solicitud" paramProperty="idSolicitud" -->
                            <bean:write property="texto" name="lista"/>
                            <!--/html:link-->
                        </td>
                        <td align="center">
                            <logic:equal name="lista" property="estado" value="0">
                                <bean:message key="solicitud.in.table.estado.pendiente"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="1">
                                <bean:message key="solicitud.in.table.estado.aceptada"/>
                            </logic:equal>
                            <logic:equal name="lista" property="estado" value="2">
                                <bean:message key="solicitud.in.table.estado.denegada"/>
                            </logic:equal>
                        </td>
                        <td align="center">
                            <bean:write property="fechaCaduca" name="lista"/>
                        </td>
                        <td align="center">
                            <html:link action="/solicitudEliminar" paramName="lista" paramId="solicitud"
                                       paramProperty="idSolicitud">
                                <html:img border="0" page="/images/iconos/16/cancelar.jpg" altKey="tooltip.eliminar"/>
                            </html:link>
                        </td>
                    </tr>
                </logic:equal>
            </logic:iterate>
        </table>
    </td>
</tr>
</table>

</center>
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
<!-- Mostrar la abadia congelada -->
<jsp:include page="/abadias/congelado.jsp"/>
</html>

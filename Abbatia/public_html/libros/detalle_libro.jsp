<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>
        <bean:message key="edificios.abadia.mostrar.titulo"/>
    </title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
<table border="1" width="50%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td align="center" bgcolor="#996633" colspan="2">
            <b><font color="#FFFFFF" size="2">
                <bean:message key="libro.detalle.titulo"/>
            </font>
            </b>
        </td>
    </tr>
    <tr>
        <td align="center" bgcolor="#E4CFA2">
            <img border="0"
                 SRC="images/libros/<bean:write name="libroDetalle" property="grafico"/>_<bean:write name="libroDetalle" property="nivel"/>.gif"/><br/><br>
            <b><bean:write name="libroDetalle" property="nombreLibro"/></b>
        </td>
        <td bgcolor="#E4CFA2">
            <b>
                <bean:write name="libroDetalle" property="descLibro"/>
            </b>
        </td>
    </tr>
</table>
<table border="1" width="50%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td align="center" bgcolor="#996633">
            <b><font color="#FFFFFF" size="2">
                <bean:message key="libro.detalle.datos"/>
            </font>
            </b>
        </td>
    </tr>
    <tr>
        <td bgcolor="#E4CFA2">
            <table border="0" width="100%">
                <tr align="center">
                    <td align="right">
                        <b>
                            <bean:message key="libro.detalle.numeropaginas"/>
                        </b>
                    </td>
                    <td align="left">
                        <bean:write name="libroDetalle" property="numPaginas"/>
                    </td>
                    <td align="right">
                        <b>
                            <bean:message key="libro.detalle.deterioro"/>
                        </b>
                    </td>
                    <td align="left">
                        <bean:write name="libroDetalle" property="deterioro" filter="false"/>
                    </td>
                </tr>
                <tr align="center">
                    <td align="right">
                        <b>
                            <bean:message key="libro.detalle.idioma"/>
                        </b>
                    </td>
                    <td align="left">
                        <bean:write name="libroDetalle" property="idioma_desc"/>
                    </td>
                    <td align="right">
                        <b>
                            <bean:message key="libro.detalle.origen"/>
                        </b>
                    </td>
                    <td align="left">
                        <bean:write name="libroDetalle" property="abadiaDesc_copia"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
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
<!--copias en curso-->
<table border="1" width="80%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td align="center" bgcolor="#996633">
            <b><font color="#FFFFFF" size="2">
                <bean:message key="libro.detalle.detallecopias"/>
            </font>
            </b>
        </td>
    </tr>
    <tr>

        <td bgcolor="#E4CFA2">
            <logic:notEmpty name="libroDetalle" property="detalles">
                <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                       bordercolordark="#808080">
                    <tr>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="libro.detalle.tabla.periodo"/>
                            </font>
                            </b>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <font color="#FFFFFF"><b>
                                <bean:message key="libro.detalle.tabla.monje"/>
                            </b>
                            </font>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="libro.detalle.tabla.abadia"/>
                            </font>
                            </b>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="libro.detalle.tabla.avance"/>
                            </font>
                            </b>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="libro.detalle.tabla.estado"/>
                            </font>
                            </b>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="libro.detalle.tabla.idioma"/>
                            </font>
                            </b>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="libro.detalle.tabla.opciones"/>
                            </font>
                            </b>
                        </td>
                    </tr>
                    <logic:iterate id="lista" name="libroDetalle" property="detalles">
                        <tr>
                            <td align="center">
                                <bean:write property="periodo_Desc" name="lista"/>
                                <br/>
                            </td>
                            <td align="center">
                                <logic:notEmpty name="lista" property="nombreMonje">
                                    <bean:write property="nombreMonje" name="lista"/> <bean:message
                                        key="monjes.abadia.nomciudad"/> <bean:write property="apellidoMonje"
                                                                                    name="lista"/>
                                </logic:notEmpty>
                                <logic:empty name="lista" property="nombreMonje">
                                    <bean:message key="libro.detalle.tabla.libre"/>
                                </logic:empty>
                                <br/>
                            </td>
                            <td align="center">
                                <bean:write property="abadia_Desc" name="lista"/>
                                <br/>
                            </td>
                            <td align="center">
                                <bean:write property="progreso" name="lista" filter="false"/>
                                <br/>
                            </td>
                            <td align="center">
                                <bean:write property="descEstado" name="lista"/>
                                <br/>
                            </td>
                            <td align="center">
                                <bean:write property="idioma_desc" name="lista"/>
                                <br/>
                            </td>
                            <td align="center">
                                <!--solo si el monje está en su abadia y existe monje copiando...-->
                                <logic:equal name="lista" property="eliminable" value="1">
                                    <html:link action="/eliminarCopiaPeriodo" paramName="lista"
                                               paramProperty="claveLibroPeriodo" paramId="clave">
                                        <html:img border="0" page="/images/iconos/16/cancelar.jpg"
                                                  altKey="tooltip.cancelarcopiaperiodo"/>
                                    </html:link>
                                </logic:equal>
                                <br/>
                            </td>
                        </tr>
                    </logic:iterate>
                </table>
            </logic:notEmpty>
        </td>

    </tr>
</table>

</center>
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

</body>
</html>

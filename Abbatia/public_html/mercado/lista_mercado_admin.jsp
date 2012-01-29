<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html>
<head>
    <title>
        <bean:message key="mercado.admin.titulo"/>
    </title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>

        <%-- solo mostramos el filtro de mercancías para los administradores --%>
        <%--logic:equal value="1" name="usuario" scope="session" property="administrador" --%>

        <%-- Formulario para las búsquedas --%>
    <html:form action="/lista_mercado_admin">
        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000" cellpadding="2">
            <tr height="20">
                <td align="center" bgcolor="#996633" colspan="4">
                    <b><font color="#FFFFFF" size="2">
                        <bean:message key="mercado.venta.filtro"/>
                    </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="0" width="100%">
                        <tr>
                            <td width="25%">
                                <p align="right">
                                    <b><bean:message key="mercado.venta.mercancias"/></b>
                                </p>
                            </td>
                            <td width="25%" align="left">
                                <p align="left">
                                    <html:select name="MercadoAdminForm" property="mercancia">
                                        <html:options collection="mercancia_tipo" property="id"
                                                      labelProperty="description"/>
                                    </html:select>
                                </p>
                            </td>
                            <td>
                                <html:submit>
                                    <bean:message key="mercado.compra.buscar"/>
                                </html:submit>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </html:form>
    <br/>
        <%--/logic:equal--%>
        <%--link a la pantalla de alta de productos, solo para administradores --%>
    <logic:equal value="1" name="usuario" scope="session" property="administrador">
        <div align="center">
            <html:link action="/altaMercadoAdmin">
                <bean:message key="mercado.admin.link.alta"/>
            </html:link>
        </div>
    </logic:equal>

    <div align="center">
        <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
               bordercolordark="#000000">
                <%-- Título --%>
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b>
                        <font color="#FFFFFF" size="2">
                            <bean:message key="mercado.admin.tabla.cabecera"/>
                        </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                           bordercolordark="#808080">
                            <%-- Cabecera --%>
                        <tr>
                                <%-- solo mostramos el id de mercado para los administradores --%>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="mercado.admin.tabla.descripcion"/>
                                </font></b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="mercado.admin.tabla.precio"/>
                                </font></b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="mercado.admin.tabla.preciomin"/>
                                </font></b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="mercado.admin.tabla.preciominc"/>
                                </font></b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="mercado.admin.tabla.preciomax"/>
                                </font></b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="mercado.admin.tabla.preciomaxc"/>
                                </font></b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="formulario.mercado.admin.ventashoy"/>
                                </font></b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="formulario.mercado.admin.ventasayer"/>
                                </font></b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="formulario.mercado.admin.numeroventas"/>
                                </font></b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="mercado.venta.tabla.opcion"/>
                                </font></b>
                            </td>
                        </tr>
                            <%-- Cuerpo Tabla --%>
                        <tr>
                            <td bgcolor="#E4CFA2">
                                    <%-- Si la lista está vacía mostrar mensaje amigable --%>
                                <logic:empty name="lista_mercancias">
                        <tr>
                            <td colspan=6>
                                <center>No hay elementos disponibles</center>
                            <td>
                        </tr>
                        </logic:empty>
                            <%-- Mostrar la lista si tiene contenido --%>
                        <logic:iterate id="lista" name="lista_mercancias">
                            <tr>
                                    <%-- solo mostramos el id de mercado para los administradores --%>
                                <td>
                                    <bean:write property="descripcion" name="lista" filter="false"/>
                                    <logic:notEmpty property="familia" name="lista">
                                        (<b><bean:write property="familia" name="lista" filter="false"/></b>)
                                    </logic:notEmpty>
                                    <logic:notEqual value="0" property="nivel" name="lista">
                                        (<b><bean:write property="nivel" name="lista" filter="false"/></b>)
                                    </logic:notEqual>
                                </td>
                                <td align="right">
                                    <bean:write property="precio" name="lista" filter="false"/>
                                </td>
                                <td align="right">
                                    <bean:write property="precioMinimo" name="lista" filter="false"/>
                                </td>
                                <td align="right">
                                    <bean:write property="precioMinimoC" name="lista" filter="false"/>
                                </td>
                                <td align="right">
                                    <bean:write property="precioMaximo" name="lista" filter="false"/>
                                </td>
                                <td align="right">
                                    <bean:write property="precioMaximoC" name="lista" filter="false"/>
                                </td>
                                <td align="right">
                                    <bean:write property="ventasHoy" name="lista" filter="false"/>
                                </td>
                                <td align="right">
                                    <bean:write property="ventasAyer" name="lista" filter="false"/>
                                </td>
                                <td align="right">
                                    <bean:write property="numeroVentas" name="lista" filter="false"/>
                                </td>

                                <td align="center">
                                    <b>
                                        <html:link action="/modificarMercadoAdmin" paramName="lista"
                                                   paramProperty="productoId" paramId="idProducto">
                                            <bean:message key="mercado.admin.tabla.modificar"/>
                                        </html:link>
                                    </b>
                                </td>
                            </tr>
                        </logic:iterate>
                        </td>
                        </tr>

                    </table>
                </td>
            </tr>
        </table>
    </div>

</center>

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

</body>
<jsp:include page="/abadias/congelado.jsp"/>
</html:html>
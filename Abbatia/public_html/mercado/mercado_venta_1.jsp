<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>Pantalla mercado venta</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/default.css" TYPE="text/css"/>
    <link rel="stylesheet" href="/theme/popup.css" type="text/css">
    <script type="text/javascript" src="/theme/overlibmws/overlibmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/ajaxcontentmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/iframecontentmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws_draggable.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws_scroll.js"></script>
    <script type="text/javascript" src="/theme/popup.js"></script>
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
            openPopupIFrame('/mostrarRequisitosElaboracionAlimentos.do?pid=' + p_ProductoId,600,250,"ElaborarAlimentos");
          }
        }
        function molerCereales()
        {
            openPopupIFrame('/molerCereales_inicio.do',600,250,'MolerCereales');
        }

        -->
    </script>


</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <html:form action="/mercado_venta_buscar">
        <%--<html:hidden value="venta" property="accion"/>--%>
        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b>
                        <font color="#FFFFFF" size="2">
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
                                    <b>
                                        <bean:message key="mercado.venta.mercancias"/>
                                    </b>
                                </p>
                            </td>
                            <td width="25%" align="left">
                                <p align="left">
                                    <html:select name="VentaForm" property="mercancia">
                                        <html:options collection="MercanciaTipo" property="id"
                                                      labelProperty="description"/>
                                    </html:select>
                                </p>
                            </td>
                            <td width="25%">
                                <p align="right">
                                    <b>
                                        <bean:message key="mercado.venta.familias"/>
                                    </b>
                                </p>
                            </td>
                            <td width="25%">
                                <p align="left">
                                    <select size="1" name="D1">
                                        <option>Todos</option>
                                        &nbsp;
                                    </select>
                                </p>
                            </td>
                        </tr>
                    </table>
                    <center>
                        <hr size="1" width="80%"/>
                        <br/>
                        <input type="submit" value="Buscar" name="B1"/>
                    </center>
                </td>
            </tr>
        </table>
    </html:form>
    <div align="center">
        <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <FONT size="1"><b><font color="#FFFFFF">
                        <bean:message key="mercado.venta.listamercancias"/>
                    </font>
                    </b>
                    </FONT>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                           bordercolordark="#808080">
                        <tr>
                            <td bgcolor="#E1C08B" align="center">
                                <FONT size="1"><b><font color="#FFFFFF">
                                    <bean:message key="mercado.venta.tabla.edificio"/>
                                </font>
                                </b>
                                </FONT>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <font color="#FFFFFF" size="1">
                                    <b>
                                        <bean:message key="mercado.venta.tabla.descripcion"/>
                                    </b>
                                </font>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <FONT size="1"><b><font color="#FFFFFF">
                                    <logic:equal value="2" name="VentaForm" property="mercancia">
                                        <bean:message key="mercado.venta.tabla.nacimiento"/>
                                    </logic:equal>
                                    <logic:equal value="1" name="VentaForm" property="mercancia">
                                        <bean:message key="mercado.venta.tabla.fechaentrada"/>
                                    </logic:equal>
                                    <logic:equal value="4" name="VentaForm" property="mercancia">
                                        <bean:message key="mercado.venta.tabla.fechaentrada"/>
                                    </logic:equal>
                                </font>
                                </b>
                                </FONT>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <FONT size="1"><b><font color="#FFFFFF">
                                    <logic:equal value="2" name="VentaForm" property="mercancia">
                                        <bean:message key="mercado.venta.tabla.diasvida"/>
                                    </logic:equal>
                                    <logic:equal value="1" name="VentaForm" property="mercancia">
                                        <bean:message key="mercado.venta.tabla.caducidad"/>
                                    </logic:equal>
                                    <logic:equal value="4" name="VentaForm" property="mercancia">
                                        <bean:message key="mercado.venta.tabla.caducidad"/>
                                    </logic:equal>
                                </font>
                                </b>
                                </FONT>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <FONT size="1"><b><font color="#FFFFFF">
                                    <bean:message key="mercado.venta.tabla.cantidad"/>
                                </font>
                                </b>
                                </FONT>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <FONT size="1"><b><font color="#FFFFFF">
                                    <bean:message key="mercado.venta.tabla.estado"/>
                                </font>
                                </b>
                                </FONT>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <FONT size="1"><b><font color="#FFFFFF">
                                    <bean:message key="mercado.venta.tabla.opcion"/>
                                </font>
                                </b>
                                </FONT>
                            </td>
                        </tr>
                        <font size="1">
                            <logic:iterate id="lista" name="MercadosListas" property="lstVenta">
                                <bean:write property="description" name="lista" filter="false"/>
                            </logic:iterate>
                        </font>
                    </table>
                </td>
            </tr>
        </table>
    </div>
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

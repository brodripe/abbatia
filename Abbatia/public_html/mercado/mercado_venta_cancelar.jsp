<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>Pantalla mercado venta</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<p>&nbsp;</p>
<html:form action="/cancelar_venta_fin">
    <DIV align="center">
        <table border="1" cellspacing="0" bordercolor="#000000" width="80%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <DIV align="center">
                        <b>
                            <font color="#FFFFFF" size="2">
                                <bean:message key="mercado.venta.cancelar.titulo"/>
                            </font>
                        </b>
                    </DIV>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <DIV align="center">
                        <table border="0" width="100%">
                            <tr>
                                <td width="25%">
                                    <p align="right">
                                        <b>
                                            <font size="3">
                                                <bean:message key="mercado.compra2.producto"/>
                                            </font>
                                        </b>
                                    </p>
                                </td>
                                <td width="25%" align="left">
                                    <font size="3">
                                        <bean:write name="DatosVentaForm" property="descripcionFamilia"/>
                                        (
                                        <bean:write name="DatosVentaForm" property="descripcionProducto"/>
                                        )
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td width="25%">
                                    <p align="right">
                                        <b>
                                            <font size="3">
                                                <bean:message key="mercado.compra2.cantidadventa"/>
                                            </font>
                                        </b>
                                    </p>
                                </td>
                                <td width="25%" align="left">
                                    <font size="3">
                                        <html:text name="DatosVentaForm" property="cantidad" size="5"/>
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2">
                                    <bean:message key="mercado.venta.cancelar.comision"/>
                                    <bean:write name="DatosVentaForm" property="comision"/>
                                    %
                                    <bean:message key="mercado.venta.cancelar.comision2"/>
                                </td>
                            </tr>
                        </table>
                    </DIV>
                </td>
            </tr>
            <tr>
                <td>
                    <p align="center">
                        <input type="button" onClick="window.parent.cClick();" value="Atrás"/>&nbsp;
                        <html:submit>
                            <bean:message key="mercado.venta.aceptar.boton"/>
                        </html:submit>
                    </p>
                </td>
            </tr>
        </table>
    </DIV>
</html:form>
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

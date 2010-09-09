<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>Pantalla mercado venta</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
    <script type="text/javascript" src="/theme/validator.js"></script>
    <html:javascript formName="VentaForm" dynamicJavascript="true" staticJavascript="false"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <p>&nbsp;</p>
    <html:form action="/mercado_vender_fin" onsubmit="return validateVentaForm(this);">
        <table border="1" cellspacing="0" bordercolor="#000000" width="80%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b>
                        <font color="#FFFFFF" size="2">
                            <bean:message key="mercado.venta2.titulo"/>
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
                                        <font size="3">
                                            <bean:message key="mercado.compra2.producto"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="25%" align="left">
                                <font size="3">
                                    <b><bean:write name="VentaForm" property="descripcionFamilia"/></b>(<bean:write
                                        name="VentaForm" property="descripcionProducto"/>)
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%">
                                <p align="right">
                                    <b>
                                        <font size="3">
                                            <bean:message key="mercado.venta2.cantidaddisponible"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="25%" align="left">
                                <font size="3">
                                    <bean:write name="VentaForm" property="cantidadDisponible"/>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%">
                                <p align="right">
                                    <b>
                                        <font size="3">
                                            <bean:message key="mercado.venta2.tipoventa"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="25%" align="left">
                                <html:select property="tipoVenta">
                                    <html:option value="0">Venta</html:option>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%">
                                <p align="right">
                                    <b>
                                        <font size="3">
                                            <bean:message key="mercado.venta2.duracion"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="25%" align="left">
                                <html:select property="dias">
                                    <html:option value="5">5 D&iacute;as</html:option>
                                    <html:option value="10">10 D&iacute;as</html:option>
                                    <html:option value="15">15 D&iacute;as</html:option>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%">
                                <p align="right">
                                    <b>
                                        <font size="3">
                                            <bean:message key="mercado.venta2.preciounidad"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="25%" align="left">
                                <font size="3">
                                    <html:text property="precio" size="10"/>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%">
                                <p align="right">
                                    <b>
                                        <font size="3">
                                            <bean:message key="mercado.venta2.cantidad"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="25%" align="left">
                                <font size="3">
                                    <html:text property="cantidad" size="10"/>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2">
                                <logic:equal value="true" name="VentaForm" property="usuarioNuevo">
                                    <html:checkbox property="venderACiudad" name="VentaForm" disabled="true">
                                        <bean:message key="mensaje.vender.venderaciudad"/> <bean:write
                                            property="precioVentaCiudad" name="VentaForm"/>
                                    </html:checkbox>
                                </logic:equal>
                                <logic:equal value="false" name="VentaForm" property="existeMercado">
                                    <html:checkbox property="venderACiudad" name="VentaForm" disabled="true">
                                        <bean:message key="mensaje.vender.venderaciudad"/> <bean:write
                                            property="precioVentaCiudad" name="VentaForm"/>
                                    </html:checkbox>
                                </logic:equal>

                                <logic:equal value="false" name="VentaForm" property="usuarioNuevo">
                                    <logic:equal value="true" name="VentaForm" property="existeMercado">
                                        <html:checkbox property="venderACiudad" name="VentaForm" disabled="false">
                                            <bean:message key="mensaje.vender.venderaciudad"/> <bean:write
                                                property="precioVentaCiudad" name="VentaForm"/>
                                        </html:checkbox>
                                    </logic:equal>
                                </logic:equal>

                                <logic:equal value="true" name="VentaForm" property="usuarioNuevo">
                                    <br/><bean:message key="mensaje.vender.avisousuarionuevo"/>
                                </logic:equal>
                                <logic:equal value="false" name="VentaForm" property="existeMercado">
                                    <br/><bean:message key="mensaje.vender.avisosinmercado"/>
                                </logic:equal>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2">
                                <hr size="1" width="80%"/>
                                <input type="button" onClick="window.parent.cClick();" value="Atrás"/>&nbsp;
                                <html:submit>
                                    <bean:message key="mercado.venta2.vender"/>
                                </html:submit>
                            </td>
                        </tr>
                    </table>
        </table>
    </html:form>
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
</html>

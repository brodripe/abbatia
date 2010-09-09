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
    <script type="text/javascript" src="/theme/validator.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/ajaxcontentmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/iframecontentmws.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws_draggable.js"></script>
    <script type="text/javascript" src="/theme/overlibmws/overlibmws_scroll.js"></script>
    <script type="text/javascript" src="/theme/popup.js"></script>
    <html:javascript formName="VentaAgrupadaForm" dynamicJavascript="true" staticJavascript="false"/>
</head>

<SCRIPT LANGUAGE="JavaScript" type="text/javascript">

    function checkOrUncheck()
    {
        if (document.VentaAgrupadaForm.CheckAll.checked)
            checkAll(document.VentaAgrupadaForm.seleccion);
        else
            uncheckAll(document.VentaAgrupadaForm.seleccion);
    }

    function checkAll(aCheck)
    {
        for (i = 0; i < aCheck.length; i++)
            aCheck[i].checked = true;
    }

    function uncheckAll(aCheck) {
        for (i = 0; i < aCheck.length; i++)
            aCheck[i].checked = false;
    }

</script>

<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <div align="center" style="vertical-align:middle;">
        <html:form action="/mercado_vender_agrupado_fin" onsubmit="return validateVentaAgrupadaForm(this);">
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
                                <td width="25%" align="center" colspan="2">
                                    <font size="3">
                                        <b><bean:write name="VentaAgrupadaForm"
                                                       property="descripcionFamilia"/></b>(<bean:write
                                            name="VentaAgrupadaForm" property="descripcionProducto"/>)
                                    </font>
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
                                        <html:text property="precio" size="7"/> (<bean:write name="VentaAgrupadaForm"
                                                                                             property="precioMercado"/>)
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td width="25%" colspan="2">
                                    <p align="center">
                                        <b>
                                            <font size="2">
                                                <bean:message key="mercado.venta2.detalle"/>
                                            </font>
                                        </b>
                                    </p>
                                </td>
                            </tr>
                        </table>
                        <table width="90%" align="center" border="1">
                            <tr height="20">
                                <td align="center" bgcolor="#E1C08B">
                                    <input type="checkbox" name="CheckAll" value="Check All" onClick="checkOrUncheck()">
                                </td>
                                <td align="center" bgcolor="#E1C08B">
                                    <b>
                                        <font color="#FFFFFF" size="2">
                                            <bean:message key="mercado.venta.tabla.caducidad"/>
                                        </font>
                                    </b>
                                </td>
                                <td align="center" bgcolor="#E1C08B">
                                    <b>
                                        <font color="#FFFFFF" size="2">
                                            <bean:message key="mercado.venta.tabla.cantidad"/>
                                        </font>
                                    </b>
                                </td>
                            </tr>

                            <logic:iterate id="lista" name="VentaAgrupadaForm" property="listaProductos">
                                <tr>
                                    <td align="center">
                                        <html:multibox name="VentaAgrupadaForm" property="seleccion">
                                            <bean:write name="lista" property="idLote"/>
                                        </html:multibox>
                                        <br/>
                                    </td>
                                    <td align="center">
                                        <bean:write property="fechaCaducidad" name="lista"/>
                                        <br/>
                                    </td>
                                    <td align="right">
                                        <bean:write property="cantidadS" name="lista"/>
                                        <br/>
                                    </td>
                                </tr>
                            </logic:iterate>
                        </table>
                <tr>
                    <td align="center">
                        <logic:equal value="true" name="VentaAgrupadaForm" property="usuarioNuevo">
                            <html:checkbox property="venderACiudad" name="VentaAgrupadaForm" disabled="true">
                                <bean:message key="mensaje.vender.venderaciudad"/> <bean:write
                                    property="precioVentaCiudad" name="VentaAgrupadaForm"/>
                            </html:checkbox>
                        </logic:equal>
                        <logic:equal value="false" name="VentaAgrupadaForm" property="existeMercado">
                            <html:checkbox property="venderACiudad" name="VentaAgrupadaForm" disabled="true">
                                <bean:message key="mensaje.vender.venderaciudad"/> <bean:write
                                    property="precioVentaCiudad" name="VentaAgrupadaForm"/>
                            </html:checkbox>
                        </logic:equal>

                        <logic:equal value="false" name="VentaAgrupadaForm" property="usuarioNuevo">
                            <logic:equal value="true" name="VentaAgrupadaForm" property="existeMercado">
                                <html:checkbox property="venderACiudad">
                                    <bean:message key="mensaje.vender.venderaciudad"/> <bean:write
                                        property="precioVentaCiudad" name="VentaAgrupadaForm"/>
                                </html:checkbox>
                            </logic:equal>
                        </logic:equal>

                        <logic:equal value="true" name="VentaAgrupadaForm" property="usuarioNuevo">
                            <br/><bean:message key="mensaje.vender.avisousuarionuevo"/>
                        </logic:equal>
                        <logic:equal value="false" name="VentaAgrupadaForm" property="existeMercado">
                            <br/><bean:message key="mensaje.vender.avisosinmercado"/>
                        </logic:equal>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <input type="button" onClick="window.parent.cClick();" value="Atrás"/>&nbsp;
                        <html:submit>
                            <bean:message key="mercado.venta2.vender"/>
                        </html:submit>
                    </td>
                </tr>
            </table>
        </html:form>
    </div>
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

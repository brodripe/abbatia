<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html>
<head>
    <title>Pantalla muestra Impuestos</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css">
</head>

<body topmargin="0" leftmargin="0" bgcolor="#E1C08B">

&nbsp;

<!-- RECAUDACION -->
<p align="center">
<table width="250">
    <tr>
        <td vAlign="top" align="middle" width="100%">
            <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="100%"
                   borderColorLight="#000000" border="1">
                <tr>
                    <td align="middle" bgColor="#996633">
                        <p align="center"><b><font color="#ffffff"><bean:message
                                key="obispado.impuestos.total.titulo"/></font></b></p>
                    </td>
                </tr>
                <tr>
                    <td bgColor="#e4cfa2">
                        <p align="center">
                            <bean:write name="ObispadoImpuestos" property="total"/>&nbsp;
                            <bean:message key="obispado.impuestos.total.monedas"/></p>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</p>
&nbsp;
<center>
    <!--- IMPUESTOS -->
    <table width="90%">
        <tr>
            <td vAlign="top" align="middle" width="100%">
                <html:form action="/obispadoImpuestos">
                    <html:hidden property="modificar" name="ObispadoImpuestos" value="1"/>
                    <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="100%"
                           borderColorLight="#000000" border="1">
                        <tr>
                            <td align="middle" bgColor="#996633">
                                <p align="center"><b><font color="#ffffff"><bean:message
                                        key="diplomacia.jerarquia.comision.titulo"/></font></b></p>
                            </td>
                        </tr>
                        <tr>
                            <td bgColor="#e4cfa2">
                                <table width="100%" border="0">
                                    <tr>
                                        <td align="center">
                                            <b><bean:message key="diplomacia.jerarquia.comision.obispado"/></b>
                                        </td>
                                        <td align="center">
                                            <b><bean:message key="diplomacia.jerarquia.comision.cancelacion"/></b>
                                        </td>
                                        <td align="center">
                                            <b><bean:message key="diplomacia.jerarquia.comision.venta5"/></b>
                                        </td>
                                        <td align="center">
                                            <b><bean:message key="diplomacia.jerarquia.comision.venta10"/></b>
                                        </td>
                                        <td align="center">
                                            <b><bean:message key="diplomacia.jerarquia.comision.venta15"/></b>
                                        </td>
                                        <td align="center">
                                            <b><bean:message key="diplomacia.jerarquia.comision.transito"/></b>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center">
                                            <html:text property="obispado" name="ObispadoImpuestos"
                                                       size="4"></html:text>&nbsp;%
                                            <html:img src="images/iconos/16/ayuda.gif"
                                                      altKey="diplomacia.jerarquia.comision.obispado.help"/>
                                        </td>
                                        <td align="center">
                                            <html:text property="cancelacion" name="ObispadoImpuestos"
                                                       size="4"></html:text>&nbsp;%
                                            <html:img src="images/iconos/16/ayuda.gif"
                                                      altKey="diplomacia.jerarquia.comision.cancelacion.help"/>
                                        </td>
                                        <td align="center">
                                            <html:text property="venta5" name="ObispadoImpuestos" size="4"></html:text>&nbsp;%
                                            <html:img src="images/iconos/16/ayuda.gif"
                                                      altKey="diplomacia.jerarquia.comision.venta5.help"/>
                                        </td>
                                        <td align="center">
                                            <html:text property="venta10" name="ObispadoImpuestos" size="4"></html:text>&nbsp;%
                                            <html:img src="images/iconos/16/ayuda.gif"
                                                      altKey="diplomacia.jerarquia.comision.venta10.help"/>
                                        </td>
                                        <td align="center">
                                            <html:text property="venta15" name="ObispadoImpuestos" size="4"></html:text>&nbsp;%
                                            <html:img src="images/iconos/16/ayuda.gif"
                                                      altKey="diplomacia.jerarquia.comision.venta15.help"/>
                                        </td>
                                        <td align="center">
                                            <html:text property="transito" name="ObispadoImpuestos"
                                                       size="4"></html:text>&nbsp;%
                                            <html:img src="images/iconos/16/ayuda.gif"
                                                      altKey="diplomacia.jerarquia.comision.transito.help"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td height=40>
                                <p align="center">
                                    <html:submit value="Modificar"/>
                                    <html:reset value="Restablecer"/>
                                </p>
                            </td>
                    </table>
                </html:form>
            </td>
        </tr>
    </table>
    &nbsp;

    <!--- HISTORIAL -->
        <bean:write name="Navega" filter="false"/>
    <table width="90%">
        <tr>
            <td vAlign="top" align="middle" width="100%">
                <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="100%"
                       borderColorLight="#000000" border="1">
                    <tr>
                        <!-- Ficha 0 -->
                        <logic:equal name="Tab" value="0">
                            <td align="center" style="border-bottom: none thin white;" bgcolor="#E4CFA2">
                                <b><font size="2">
                                    <bean:message key="obispado.impuestos.historia.titulo"/>
                                </font>
                                </b>
                            </td>
                            <td align="center" bgcolor="#780A00">
                                <html:link action="/obispadoImpuestos?tab=1">
                                    <font color="#FFFFFF"><b><bean:message
                                            key="obispado.impuestos.historia.titulo_abadia"/></b></font>
                                </html:link>
                            </td>
                            <td align="center" bgcolor="#780A00">
                                <html:link action="/obispadoImpuestos?tab=2">
                                    <font color="#FFFFFF"><b><bean:message
                                            key="obispado.impuestos.historia.titulo_pago"/></b></font>
                                </html:link>
                            </td>
                        </logic:equal>
                        <!-- Ficha 1 -->
                        <logic:equal name="Tab" value="1">
                            <td align="center" bgcolor="#780A00">
                                <html:link action="/obispadoImpuestos?tab=0">
                                    <font color="#FFFFFF"><b><bean:message
                                            key="obispado.impuestos.historia.titulo"/></b></font>
                                </html:link>
                            </td>
                            <td align="center" bgcolor="#E4CFA2" style="border-bottom: none thin white;">
                                <b><font size="2">
                                    <bean:message key="obispado.impuestos.historia.titulo_abadia"/>
                                </font>
                                </b>
                            </td>
                            <td align="center" bgcolor="#780A00">
                                <html:link action="/obispadoImpuestos?tab=2">
                                    <font color="#FFFFFF"><b><bean:message
                                            key="obispado.impuestos.historia.titulo_pago"/></b></font>
                                </html:link>
                            </td>
                        </logic:equal>
                        <!-- Ficha 2-->
                        <logic:equal name="Tab" value="2">
                            <td align="center" bgcolor="#780A00">
                                <html:link action="/obispadoImpuestos?tab=0">
                                    <font color="#FFFFFF"><b><bean:message
                                            key="obispado.impuestos.historia.titulo"/></b></font>
                                </html:link>
                            </td>
                            <td align="center" bgcolor="#780A00">
                                <html:link action="/obispadoImpuestos?tab=1">
                                    <font color="#FFFFFF"><b><bean:message
                                            key="obispado.impuestos.historia.titulo_abadia"/></b></font>
                                </html:link>
                            </td>
                            <td align="center" bgcolor="#E4CFA2" style="border-bottom: none thin white;">
                                <b><font size="2">
                                    <bean:message key="obispado.impuestos.historia.titulo_pago"/>
                                </font>
                                </b>
                            </td>
                        </logic:equal>
                    </tr>
                    <tr>
                        <td bgcolor="#E4CFA2" colspan="3" style="border-top: none thin white;">
                            <br/>
                            <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="100%"
                                   borderColorLight="#000000" border="1">
                                <logic:iterate id="lista" name="ObispadoImpuestos" property="contribuciones">
                                    <bean:write property="description" name="lista" filter="false"/>
                                </logic:iterate>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>


    <!-- Publicidad -->
    <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
        <jsp:include page="/varios/publicidad.jsp"/>
    </logic:notEqual>

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

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>
        <bean:message key="index.header"/>
    </title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body bgcolor="#E1C08B">
<html:form action="/modificarMercadoAdmin">
    <html:hidden property="accion" value="actualizar"/>
    <html:hidden property="productoId"/>
    <center>
        <table border="1" width="50%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr>
                <td colspan="2" align="center" bgcolor="#996633">
                    <b>
                        <font color="#FFFFFF" size="2">
                            <bean:message key="formulario.mercado.admin.titulo"/>
                        </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="0" align="center">
                        <tr>
                            <td colspan="2" align="center">
                                <bean:message key="formulario.mercado.admin.producto"/>:
                                <bean:write name="datosMercadoAdmin" property="descripcion"/>
                                <logic:notEmpty property="familia" name="datosMercadoAdmin">
                                    (<b><bean:write property="familia" name="datosMercadoAdmin" filter="false"/></b>)
                                </logic:notEmpty>
                                <logic:notEqual value="0" property="nivel" name="datosMercadoAdmin">
                                    (<b><bean:write property="nivel" name="datosMercadoAdmin" filter="false"/></b>)
                                </logic:notEqual>
                            </td>
                        </tr>
                        <br>

                            <%-- solo mostramos el precio máximo y mínimo real para los administradores --%>
                        <logic:equal value="1" name="usuario" scope="session" property="administrador">
                            <tr>
                                <th>
                                    <div align="right">
                                        <bean:message key="formulario.mercado.admin.precio"/>
                                    </div>
                                </th>
                                <td>
                                    <html:text property="precio" maxlength="10" name="datosMercadoAdmin" size="5"/>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    <div align="right">
                                        <bean:message key="formulario.mercado.admin.preciominimo"/>
                                    </div>
                                </th>
                                <td>
                                    <html:text maxlength="10" property="precioMinimo" name="datosMercadoAdmin"
                                               size="5"/>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    <div align="right">
                                        <bean:message key="formulario.mercado.admin.preciomaximo"/>
                                    </div>
                                </th>
                                <td>
                                    <html:text maxlength="10" property="precioMaximo" name="datosMercadoAdmin"
                                               size="5"/>
                                </td>
                            </tr>
                        </logic:equal>
                            <%-- en el caso de no ser administrador, se asume que se trata de un cardenal y mostramos máximos y míminos para cardenal --%>
                        <logic:notEqual value="1" name="usuario" scope="session" property="administrador">
                            <tr>
                                <th>
                                    <div align="right">
                                        <bean:message key="formulario.mercado.admin.preciominimo"/>
                                    </div>
                                </th>
                                <td>
                                    <html:text maxlength="10" property="precioMinimoC" name="datosMercadoAdmin"
                                               size="5"/> >(<bean:write name="datosMercadoAdmin"
                                                                        property="precioMinimo"/>)
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    <div align="right">
                                        <bean:message key="formulario.mercado.admin.preciomaximo"/>
                                    </div>
                                </th>
                                <td>
                                    <html:text maxlength="10" property="precioMaximoC" name="datosMercadoAdmin"
                                               size="5"/> <(<bean:write name="datosMercadoAdmin"
                                                                        property="precioMaximo"/>)
                                </td>
                            </tr>
                        </logic:notEqual>
                        <tr>
                            <th>
                                <div align="right">
                                    <bean:message key="formulario.mercado.admin.numeroventas"/>
                                </div>
                            </th>
                            <td>
                                <html:text maxlength="10" property="numeroVentas" name="datosMercadoAdmin" size="5"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <div align="right">
                                    <bean:message key="formulario.mercado.admin.ventashoy"/>
                                </div>
                            </th>
                            <td>
                                <bean:write name="datosMercadoAdmin" property="ventasHoy"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <div align="right">
                                    <bean:message key="formulario.mercado.admin.ventasayer"/>
                                </div>
                            </th>
                            <td>
                                <bean:write name="datosMercadoAdmin" property="ventasAyer"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2">
                                <hr width="100%" align="center" size="1"/>
                                <BR/>
                                <input type="button" onClick="javascript:history.back();" value="Atrás"/>
                                <html:submit>
                                    <bean:message key="formulario.mercado.admin.actualizar"/>
                                </html:submit>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2">
                                <logic:messagesPresent message="true">
                                    <html:messages id="msg" message="true">
                                        <bean:write name="msg"/><br/>
                                    </html:messages>
                                </logic:messagesPresent>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </center>
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

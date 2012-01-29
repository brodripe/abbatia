<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html>
    <head>
        <title>
            <bean:message key="mercado.compra.titulo"/>
        </title>
        <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
    </head>
    <body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
    <center>
        <html:form action="/mercado_compra">
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
                                        <b>
                                            <bean:message key="mercado.compra.mercado"/>
                                        </b>
                                    </p>
                                </td>
                                <td width="25%" align="left">
                                    <p align="left">
                                        <html:select name="CompraForm" property="mercado">
                                            <html:options collection="mercado" property="id"
                                                          labelProperty="description"/>
                                        </html:select>
                                    </p>
                                </td>
                                <td width="25%" align="left">
                                    <p align="right">&nbsp;
                                        <b>
                                            <bean:message key="mercado.compra.tipo"/>
                                        </b>
                                    </p>
                                </td>
                                <td width="25%" align="left">
                                    <html:select name="CompraForm" property="tipo">
                                        <html:options collection="compra_tipo" property="id"
                                                      labelProperty="description"/>
                                    </html:select>
                                </td>
                            </tr>
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
                                        <html:select name="CompraForm" property="mercancia">
                                            <html:options collection="mercancia_tipo" property="id"
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
                                        <select size="1" name="familia">
                                            <option>Todos</option>
                                            &nbsp;
                                        </select>
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td width="25%">
                                    <p align="right">
                                        <b>
                                            <bean:message key="mercado.compra.avanzada"/>
                                        </b>
                                    </p>
                                </td>
                                <td width="25%" align="left">
                                    <html:select name="CompraForm" property="filtro">
                                        <html:options collection="filtro" property="id" labelProperty="description"/>
                                    </html:select>
                                </td>
                                <td width="50%" colspan="2">
                                    <html:text property="filtrocontents" name="CompraForm" size="30"/>
                                </td>
                            </tr>
                        </table>
                        <hr size="1" width="80%"/>
                        <center>
                            <html:submit>
                                <bean:message key="mercado.compra.buscar"/>
                            </html:submit>
                        </center>
                    </td>
                </tr>
            </table>
        </html:form>
        <br/>
        <logic:greaterThan name="MercadosListas" property="pages" value="0">
            <bean:write name="MercadosListas" property="navigate" filter="false"/>
            <br/>
        </logic:greaterThan>
        <div align="center">
            <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
                   bordercolordark="#000000">
                <tr height="20">
                    <td align="center" bgcolor="#996633">
                        <b>
                            <font color="#FFFFFF" size="2">
                                <bean:message key="mercado.compra.tabla.cabecera"/>
                            </font>
                        </b>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#E4CFA2">
                        <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                               bordercolordark="#808080">
                            <logic:iterate id="lista" name="MercadosListas" property="lstCompra">
                                <bean:write property="description" name="lista" filter="false"/>
                            </logic:iterate>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
    </center>
    <!-- Publicidad -->
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

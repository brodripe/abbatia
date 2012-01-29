<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>untitled</title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
</head>
<script type="text/javascript">
    function seleccionar(p_Cantidad) {
        forms.datosMolino.cantidad.value = pCantidad;
    }
</script>


<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <table border="1" cellspacing="0" bordercolor="#000000" width="80%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <html:form action="/molerCereales_fin">
        <div align="center">
            <tr>
                <td bgcolor="#E4CFA2">
            <tr height="20">
                <td align="center" bgcolor="#996633" colspan="2">
                    <b>
                        <font color="#FFFFFF">
                            <bean:message key="edificio.abadia.molino.seleccioncereal"/>
                        </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2" colspan="2">
                    <table border="1" width="100%">
                        <logic:notEmpty name="datosMolino" property="productos">
                        <logic:iterate id="lista" name="datosMolino" property="productos">
                            <tr>
                                <td align="center">
                                    <input type="radio" name="seleccion"
                                           value="<bean:write name="lista" property="idLote" />"
                                           onclick="form.cantidad.value=<bean:write name="lista" property="cantidad" />">
                                        <%--<html:radio name="datosMolino" property="seleccion" idName="lista" value="idLote" onclick="form.cantidad.value=<bean:write name='lista' property="cantidad" />"/>--%>
                                </td>
                                <td>
                                    <bean:write name="lista" property="descripcion"/>
                                </td>
                                <td>
                                    <bean:write name="lista" property="cantidad"/>
                                </td>
                            </tr>
                        </logic:iterate>
                    </table>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E1C08B" align="center">
                    <b><font color="#FFFFFF">
                        <bean:message key="edificios.abadia.almacen.cantidad"/>:
                    </font>
                    </b>
                </td>
                <td>
                    <html:text property="cantidad" name="datosMolino" maxlength="6" size="6"/>
                </td>
            </tr>
            </logic:notEmpty>
            <logic:empty name="datosMolino" property="productos">
                <tr>
                    <td align="center" colspan="2">
                        <bean:message key="mensajes.aviso.notienescereales"/>
                    </td>
                </tr>
            </logic:empty>
            <tr>
                <td align="center" colspan="2" bgcolor="#E1C08B">
                    <input type="button" onclick="window.parent.cClick();" value="Atrás"/>
                    <logic:notEmpty name="datosMolino" property="productos">
                        <html:submit>
                            <bean:message key="edificio.abadia.molino.moler"/>
                        </html:submit>
                    </logic:notEmpty>
                </td>
            </tr>
            <logic:messagesPresent message="true">
                <tr>
                    <td colspan="2" align="center">
                        <html:messages id="msg" message="true">
                            <div class="success">
                                <bean:write name="msg"/><br/>
                            </div>
                        </html:messages>
                    </td>
                </tr>
            </logic:messagesPresent>
            </html:form>
            </td>
            </tr>
        </div>
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
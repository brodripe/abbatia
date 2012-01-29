<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>


<html>
<head>
    <title>Pantalla mercado venta</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css">
</head>

<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">

<p>&nbsp;</p>
<html:form action="/enviar_password">

    <DIV align="center">

        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000">

            <tr height="20">

                <td align="center" bgcolor="#996633">

                    <DIV align="center">

                        <b>
                            <font color="#FFFFFF" size="2">
                                <bean:message key="usuario.solicitarpassword.tabla"/>
                            </font>
                        </b>

                    </DIV>

                </td>

            </tr>

            <tr>

                <td bgcolor="#E4CFA2">

                    <table border="0" width="100%">
                        <tr>
                            <td colspan="2" align="center">
                                <bean:message key="usuario.solicitarpassword.msg"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="50%">
                                <b><bean:message key="usuario.nick"/></b>
                            </td>
                            <td align="left" width="50%">
                                <html:text property="nick"/>
                            </td>
                        </tr>
                    </table>


                </td>

            </tr>

            <tr>

                <td>

                    <p align="center">

                        <input type="button" onClick="javascript:history.back();" value="Atrás"/>&nbsp;

                        <html:submit value="enviar">
                            <bean:message key="mensajes.confirmacion.boton.aceptar"/>
                        </html:submit>

                    </p>

                </td>

            </tr>

        </table>

    </DIV>
</html:form>
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

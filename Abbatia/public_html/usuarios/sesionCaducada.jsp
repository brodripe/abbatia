<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<html>
<head>
    <title></title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<P>&nbsp;</P>

<P>&nbsp;</P>

<P>&nbsp;</P>

<P>&nbsp;</P>
<center>
    <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
           bordercolordark="#000000">

        <tr height="20">

            <td align="center" bgcolor="#996633">

                <b>
                    <font color="#FFFFFF" size="2">
                        <bean:message key="mensajes.abadia.avisos"/>
                    </font>
                </b>

            </td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2">
                <table border="0" width="100%">
                    <tr>
                        <td align="center">
                            <logic:messagesPresent message="true">
                                <html:messages id="msg" message="true">
                                    <div class="success">
                                        <STRONG>
                                            <FONT size="3">
                                                <bean:write name="msg"/><br/>
                                            </FONT>
                                        </STRONG>
                                    </div>
                                </html:messages>
                            </logic:messagesPresent>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                            <html:link action="/inicio" target="_top">
                                <bean:message key="principal.reconectar"/>
                            </html:link>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</center>
<!-- Publicidad -->
<logic:present name="usuario">
    <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
        <jsp:include page="/varios/publicidad.jsp"/>
    </logic:notEqual>
</logic:present>
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


<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>

<html>
<head>
    <title>listar_TopUsuarios</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css">
</head>
<body topmargin="2" leftmargin="4" bgcolor="#E1C08B" text="#000000">

<center>

    <bean:write name="Navega" filter="false"/>
    <br/>
    <!-- Mejores en Abbatia -->
    <table border="1" width="500" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr>
            <td align="center" bgcolor="#000080"><font color="#FFFFFF" size="1"><bean:message
                    key="principal.portal.top.mejores"/></font></td>
        </tr>
        <tr>
            <td bgcolor="#FFFFFF">
                <font size="2">
                    <table border=0 width="100%">
                        <logic:iterate id="ultmejores" name="DatosInicio" property="ultimosMejoresAbadias">
                            <bean:write property="description" name="ultmejores" filter="false"/>
                        </logic:iterate>
                    </table>
                </font>
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

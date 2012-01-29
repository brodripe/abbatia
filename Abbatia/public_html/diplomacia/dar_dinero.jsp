<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<p align="right">
    <logic:notEmpty name="DatosContents">
<table border="1" width="130" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td align="center" bgcolor="#F0AF1C">
            <font color="#000000" size="1"><bean:message key="principal.recursos"/></font>
        </td>
    </tr>
    <tr>
        <td bgcolor="#F7FEEA" align="center">
            <table border="0" width="100%">
                <tr>
                    <td width="50%" align="center">
                        <img border="0" src="images/iconos/16/monedas.jpg"
                             alt="<bean:message key="principal.hint.moneda"/>"/>
                    </td>
                    <td width="50%" align="right">
                        <bean:write name="DatosContents" property="recursoMonedas" filter="false"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</logic:notEmpty>
</p>
<center>
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

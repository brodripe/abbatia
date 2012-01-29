<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<html>
<head>
    <link REL="STYLESHEET" HREF="../theme/styles.css" TYPE="text/css">
    <title>Pagina nueva 2</title>
    <base target="_self"/>
</head>
<body topmargin="2" leftmargin="4" bgcolor="#E1C08B" text="#000000">

&nbsp;
<div align="center">
    <center>
        <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="90%" borderColorLight="#000000"
               border="1">
            <tr>
                <td align="middle" bgColor="#996633">
                    <p align="center"><b><font color="#ffffff" size="2"><bean:message
                            key="publicidad.titulo"/></font></b></p>
                </td>
            </tr>
            <tr>
                <td bgColor="#e4cfa2">
                    <p align="center">&nbsp;</p>

                    <p align="center"><bean:message key="publicidad.redirect"/></p>

                    <p align="center">&nbsp;</td>
            </tr>
        </table>
    </center>
</div>
&nbsp;
<script>
    parent.location = '<bean:write name="url" filter="false" />';
</script>
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

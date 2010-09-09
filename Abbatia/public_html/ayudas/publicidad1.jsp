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

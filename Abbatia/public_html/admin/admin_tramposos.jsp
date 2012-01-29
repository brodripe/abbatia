<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
    <title>Literales</title>
</head>
<body bgcolor="#E1C08B">

<center>
    &nbsp;
    <bean:write name="Navega" filter="false"/>
    <br/>
    <table cellSpacing="0" width="90%" bgColor="#996633" border="0">
        <tbody>
        <tr>
            <td width="20"></td>
            <td>
                <p align="center"><b><font color="#ffffff" size="2">Posibles tramposos</font></b></p>
            </td>
        </tr>
        </tbody>
    </table>
    <table style="BORDER-RIGHT: #000000 1px solid; BORDER-TOP: #000000 1px solid; BORDER-LEFT: #000000 1px solid; BORDER-BOTTOM: #000000 1px solid"
           borderColor="#000000" cellSpacing="0" width="90%" border="1">
        <tbody>
        <tr>
            <td bgColor="#e4cfa2">
                <table width="100%">
                    <logic:iterate id="lista" name="tramposos">
                        <font size="2">
                            <bean:write property="description" name="lista" filter="false"/>
                        </font>
                    </logic:iterate>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
    &nbsp;
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

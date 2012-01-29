<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>Pantalla mercado venta</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css">
</head>

<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>

    <p>&nbsp;</p>

    <form action="monjes_acciones.do">
        <input type="hidden" name="action" value="reclutar_novicio">

        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height=20>
                <td align="center" bgcolor="#996633"><b><font color="#FFFFFF" size="2"><bean:message
                        key="monje.reclutar.titulo"/></font></b></td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <p align="center"><br>
                        <b><bean:message key="monje.reclutar1"/></b></p>

                    <p align="center">
                        <bean:message key="monje.reclutar2"/>
                    </p>

                    <p align="left">
                        <font SIZE="2">
                            <b><bean:message key="monje.reclutar3"/></b>
                            <font color="#808080">
                                <bean:message key="monje.reclutar4"/><br>
                            </font>
                        </font>
                    </p>
                </td>
            </tr>
            <tr>
                <td>
                    <p align="center">
                        <input type="button" onClick="javascript:history.back();"
                               value="<bean:message key="general.atras"/>">&nbsp;
                        <input type="submit" value="<bean:message key="monje.reclutar.submit"/>">
                    </p>
                </td>
            </tr>
        </table>
    </form>
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
<jsp:include page="/abadias/congelado.jsp"/>
</html>

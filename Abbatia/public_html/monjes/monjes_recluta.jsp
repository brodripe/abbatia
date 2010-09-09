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
<jsp:include page="/abadias/congelado.jsp"/>
</html>

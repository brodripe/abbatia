<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title></title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css">
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<br/>

<div align="center">
    <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr height="20">
            <td align="center" style="border-bottom: none thin white;border-right: none thin white;" bgcolor="#E4CFA2">
                <b><font size="2">
                    <bean:message key="outlook.menu.monjes.trabajos"/>
                </font>
                </b>
            </td>
            <td align="center" bgcolor="#780A00">
                <html:link action="/listarTrabajos?ahora=1">
                    <font color="#FFFFFF"><b><bean:message key="monjes.trabajosahora"/></b></font>
                </html:link>
            </td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2" colspan=2 style="border-top: none thin white;">
                <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                       bordercolordark="#808080">
                    <logic:iterate id="ultamens" name="actividades">
                        <font size="2">
                            <bean:write property="description" name="ultamens" filter="false"/>
                        </font>
                    </logic:iterate>
                </table>
            </td>
        </tr>
    </table>
</div>
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

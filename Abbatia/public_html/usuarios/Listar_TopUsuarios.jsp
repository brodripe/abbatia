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

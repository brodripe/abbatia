<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title>Confirma eliminacion</title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<p>&nbsp;</p>
<html:form action="/eliminar_abadia">
    <html:hidden property="accion" value="confirma"/>
    <DIV align="center">
        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <DIV align="center">
                        <b>
                            <font color="#FFFFFF" size="2">
                                <bean:message key="varios.eliminar.abadia.tabla"/>
                            </font>
                        </b>
                    </DIV>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <DIV align="center">
                        <table border="0" width="100%">
                            <tr>
                                <td width="100%">
                                    <p align="center">
                                        <b>
                                            <font size="3">
                                                <bean:message key="varios.eliminar.abadia.texto1"/>
                                                <bean:message key="varios.eliminar.abadia.texto2"/>
                                                <bean:message key="varios.eliminar.abadia.texto3"/>
                                                <bean:message key="varios.eliminar.abadia.confirmacion"/>
                                            </font>
                                        </b>
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </DIV>
                </td>
            </tr>
            <tr>
                <td>
                    <p align="center">
                        <input type="button" onClick="javascript:history.back();" value="Atrás"/>&nbsp;
                        <html:submit value="Eliminar"/>
                    </p>
                </td>
            </tr>
        </table>
    </DIV>
</html:form>
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

</body>
</html>

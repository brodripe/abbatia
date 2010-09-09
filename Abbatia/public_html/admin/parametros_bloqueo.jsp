<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>


<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<layout:html>
    <html:form action="/CrearPeticionBloqueo">
        <table border="1" cellspacing="0" bordercolor="#000000" ordercolorlight="#000000" bordercolordark="#000000"
               cellpadding="2">
            <tr height="20">
                <td align="center" bgcolor="#996633" colspan="4">
                    <b><font color="#FFFFFF" size="2">
                        <bean:message key="peticion.bloqueo.parametros.titulo"/> <bean:write name="DatosPeticionBloqueo"
                                                                                             property="nombreAbadia"/>
                    </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="0" width="100%">
                        <tr>
                            <td align="left">
                                <bean:message key="peticion.bloqueo.parametros.numdias"/><br/>
                                <html:text property="numDias" size="3" maxlength="3"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="left">
                                <bean:message key="peticion.bloqueo.parametros.motivo"/><br/>
                                <html:textarea property="motivo" cols="35"/>
                            </td>
                        </tr>
                    </table>
                    <hr size="1" width="80%"/>
                    <center>
                        <html:submit>
                            <bean:message key="peticion.bloqueo.parametros.submit"/>
                        </html:submit>
                    </center>
                </td>
            </tr>
        </table>
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


</layout:html>
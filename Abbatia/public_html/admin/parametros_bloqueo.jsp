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

</layout:html>
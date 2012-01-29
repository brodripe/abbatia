<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html>
    <head>
        <title></title>
        <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
    </head>
    <body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
    <center>

        <table border="1" width="80%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr>
                <td align="center" bgcolor="#996633">
                    <b><font color="#FFFFFF" size="2">
                        <bean:message key="principal.elecciones.papa"/>
                    </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td>
                    <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                           bordercolordark="#808080">
                        <tr>
                            <td colspan="4" bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="elecciones.candidatos.lista"/>
                                </font>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="elecciones.candidatos.tabla.candidato"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="elecciones.candidatos.tabla.abadia"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <font color="#FFFFFF"><b>
                                    <bean:message key="elecciones.candidatos.tabla.orden"/>
                                </b>
                                </font>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="principal.elecciones.votar"/>
                                </font>
                                </b>
                            </td>
                        </tr>
                        <logic:iterate id="lista" name="DatosVotacionPapa" property="candidatos">
                            <tr>
                                <td>
                                    <b><bean:write property="nombreMonje" name="lista"/></b>
                                </td>
                                <td align="center">
                                    <bean:write property="nombreAbadia" name="lista"/>
                                    <br/>
                                </td>
                                <td align="center">
                                    <bean:write property="nombreOrden" name="lista"/>
                                    <br/>
                                </td>
                                <td align="center">
                                    <logic:equal name="DatosVotacionPapa" property="puedeVotar" value="1">
                                        <html:link action="/votar_papa" paramId="seleccion" paramName="lista"
                                                   paramProperty="idMonje">
                                            <html:img border="0" page="/images/iconos/16/votar.gif"
                                                      altKey="tooltip.votar"/>
                                        </html:link>
                                    </logic:equal>
                                </td>
                            </tr>
                        </logic:iterate>
                        </td>
                        </tr>
                    </table>
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
</html:html>

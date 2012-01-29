<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>
        <bean:message key="administracion.titulo"/>
    </title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <p>&nbsp;</p>
    <table>
        <tr>
            <html:form action="/Administracion" focus="nombreAbadia">
                <html:hidden name="AdminForm" property="accion" value="restaurar"/>
                <td>
                    <bean:message key="administracion.restaurarabadia"/>
                </td>
                <td>
                    <html:text property="nombreAbadia" name="AdminForm" size="10"/>
                </td>
                <td>
                    <html:submit>
                        <bean:message key="administracion.restaurarlink"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
        <tr>
            <html:form action="/Administracion" focus="nombreAbadia">
                <html:hidden name="AdminForm" property="accion" value="suplantar"/>
                <td>
                    <bean:message key="administracion.suplantarabadia"/>
                </td>
                <td>
                    <html:text property="nombreAbadia" name="AdminForm" size="10"/>
                </td>
                <td>
                    <html:submit>
                        <bean:message key="administracion.suplantarlink"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
        <tr>
            <html:form action="/Administracion" focus="nombreAbadia">
                <html:hidden name="AdminForm" property="accion" value="eliminar"/>
                <td>
                    <bean:message key="administracion.eliminarabadia"/>
                </td>
                <td>
                    <html:text property="nombreAbadia" name="AdminForm" size="10"/>
                </td>
                <td>
                    <html:submit>
                        <bean:message key="administracion.eliminarlink"/>
                    </html:submit>
                    <html:checkbox name="AdminForm" property="confirmacion"/>Confirmaci&oacute;n
                </td>
            </html:form>
        </tr>
        <tr>
            <html:form action="/Administracion" focus="nombreAbadia">
                <html:hidden name="AdminForm" property="accion" value="habilidadesAbadia"/>
                <td>
                    <bean:message key="administracion.habilidadesabadia"/>
                </td>
                <td>
                    <html:text property="nombreAbadia" name="AdminForm" size="10"/>
                </td>
                <td>
                    <html:submit>
                        <bean:message key="administracion.habilidadeslink"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
        <tr>
            <html:form action="/Administracion" focus="nombreAbadia">
                <html:hidden name="AdminForm" property="accion" value="habilidadesTodas"/>
                <td>
                    <bean:message key="administracion.habilidadestodas"/>
                </td>
                <td/>
                <td>
                    <html:submit>
                        <bean:message key="administracion.habilidadeslink"/>
                    </html:submit>
                    <html:checkbox name="AdminForm" property="confirmacion"/>Confirmaci&oacute;n
                </td>
            </html:form>
        </tr>
        <tr>
            <html:form action="/Administracion" focus="nombreAbadia">
                <html:hidden name="AdminForm" property="accion" value="catastrofe"/>
                <td>
                    <bean:message key="administracion.lanzarcatastrofe"/>
                </td>
                <td>
                    <html:text property="nombreAbadia" name="AdminForm" size="10"/>
                </td>
                <td>
                    <html:submit>
                        <bean:message key="administracion.catastrofelink"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
        <tr>
            <html:form action="/Administracion" focus="idMonje">
                <html:hidden name="AdminForm" property="accion" value="arreglarMonje"/>
                <td>
                    <bean:message key="administracion.arreglarmonje"/>
                </td>
                <td>
                    <html:text property="idMonje" name="AdminForm" size="10"/>
                </td>
                <td>
                    <html:submit>
                        <bean:message key="administracion.reiniciarlink"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
        <tr>
            <html:form action="/Administracion" focus="idLibro">
                <html:hidden name="AdminForm" property="accion" value="generar_libro"/>
                <td>
                    <bean:message key="administracion.generar.libro"/>
                </td>
                <td>
                    <html:text property="idLibro" name="AdminForm" size="10"/>
                </td>
                <td>
                    <html:submit>
                        <bean:message key="administracion.propagar.libro"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
        <tr>
            <td>
                Recarga Singletons
            </td>
            <td>
                <a href="/Administracion.do?accion=recarga_singletons"> Recargar </a>
            </td>
        </tr>
        <tr>
            <td>
                Planificador ${planificador}
            </td>
            <td>
                <logic:equal value="false" name="planificador" scope="request">
                    <a href="/planificador.do?accion=start"> Arrancar </a>
                </logic:equal>
                <logic:equal value="true" name="planificador" scope="request">
                    <a href="/planificador.do?accion=stop"> Parar </a>
                </logic:equal>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <div align="center">
                    <html:errors/>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <div align="center">
                    <logic:messagesPresent message="true">
                        <html:messages id="msg" message="true">
                            <div class="success">
                                <STRONG><FONT size="3">
                                    <bean:write name="msg"/>
                                    <br/>
                                </FONT>
                                </STRONG>
                            </div>
                        </html:messages>
                    </logic:messagesPresent>
                </div>
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
</html>

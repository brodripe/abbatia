<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>


<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<layout:html>
    <html:form action="/BuscarAbadiaBloqueo">
        <table border="1" cellspacing="0" bordercolor="#000000" ordercolorlight="#000000" bordercolordark="#000000"
               cellpadding="2">
            <tr height="20">
                <td align="center" bgcolor="#996633" colspan="4">
                    <b><font color="#FFFFFF" size="2">
                        <bean:message key="peticion.bloqueo.busqueda.abadia.titulo"/>
                    </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="0" width="100%">
                        <tr>
                            <td align="center">
                                <html:text property="nombreAbadia"/>
                            </td>
                        </tr>
                    </table>
                    <hr size="1" width="80%"/>
                    <center>
                        <html:submit>
                            <bean:message key="peticion.bloqueo.busqueda.abadia.accion.buscar"/>
                        </html:submit>
                    </center>
                </td>
            </tr>
        </table>
    </html:form>

    <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr height="20">
            <td align="center" bgcolor="#996633">
                <b><font color="#FFFFFF" size="2">
                    <bean:message key="peticion.bloqueo.busqueda.abadia.lista.titulo"/>
                </font>
                </b>
            </td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2">
                <layout:pager maxPageItems="8">
                    <layout:collection name="lista_abadias" id="lista">
                        <layout:collectionItem title="peticion.bloqueo.busqueda.abadia.lista.nombre"
                                               property="nombre_abadia" sortable="true"/>
                        <layout:collectionItem title="peticion.bloqueo.busqueda.abadia.lista.region"
                                               property="nombre_region" sortable="true"/>
                        <layout:collectionItem>
                            <layout:link action="/ParametrosBloqueo" paramName="lista" paramId="idAbadia"
                                         paramProperty="idAbadia">
                                <layout:img border="0" page="/images/iconos/16/autorizar.jpg"
                                            altKey="tooltip.peticion.bloqueo.seleccionar"/>
                            </layout:link>
                        </layout:collectionItem>
                    </layout:collection>
                </layout:pager>
            </td>
        </tr>
    </table>
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
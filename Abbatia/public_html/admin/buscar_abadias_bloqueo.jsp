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
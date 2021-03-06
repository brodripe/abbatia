<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>


<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<layout:html>
    <table border="1" cellspacing="0" bordercolor="#000000" width="50%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr height="20">
            <td align="center" bgcolor="#996633">
                <h2>
                    <bean:message key="administracion.propagar.libro.titulo"/>
                </h2>
            </td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2">
                <layout:pager maxPageItems="20">
                    <layout:collection name="LibrosTipo" id="lista" styleClass="COL">
                        <layout:collectionItem title="administracion.propagar.libro.nombre" property="nombre"
                                               sortable="true"/>
                        <layout:collectionItem title="administracion.propagar.libro.descripcion" property="descripcion"
                                               sortable="true"/>
                        <layout:collectionItem title="mensajes.inbox.table.seleccion">
                            <layout:link action="/propagarLibros" paramName="lista" paramId="clave"
                                         paramProperty="idLibroTipo">
                                <layout:img border="0" page="/images/iconos/16/autorizar.jpg"
                                            altKey="tooltip.administracion.propagar.libro"/>
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
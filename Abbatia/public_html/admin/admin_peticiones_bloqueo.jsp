<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>


<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<link rel="stylesheet" href="/theme/popup.css" type="text/css">
<script type="text/javascript" src="/theme/overlibmws/overlibmws.js"></script>
<script type="text/javascript" src="/theme/overlibmws/ajaxcontentmws.js"></script>
<script type="text/javascript" src="/theme/overlibmws/iframecontentmws.js"></script>
<script type="text/javascript" src="/theme/overlibmws/overlibmws_draggable.js"></script>
<script type="text/javascript" src="/theme/overlibmws/overlibmws_scroll.js"></script>
<script type="text/javascript" src="/theme/popup.js"></script>

<script language="javascript" type="text/javascript">
    function crearPeticion()
    {
        openPopupIFrame('/BuscarAbadiaBloqueo.do', 520, 390, "altaPeticion");
    }

</script>
<layout:html>
    <table border="1" cellspacing="0" bordercolor="#000000" width="70%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr>
            <td align="center">
                <html:link href="javascript:crearPeticion();">
                    <b><bean:message key="peticion.bloqueo.accion.crear"/></b>
                </html:link>
            </td>
        </tr>

        <tr height="20">
            <td align="center" bgcolor="#996633">
                <b><font color="#FFFFFF" size="2">
                    <bean:message key="peticion.bloqueo.abiertas.titulo"/>
                </font>
                </b>
            </td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2">
                <layout:pager maxPageItems="20">
                    <layout:collection name="BloqueosAbiertos" id="lista">
                        <layout:collectionItem title="peticion.bloqueo.lista.usuario" property="nombreUsuario"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.fecha.creacion" property="fechaCreacion"
                                               sortable="true"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.abadia" property="nombreAbadia"
                                               sortable="true"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.region" property="nombreRegion"
                                               sortable="true"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.dias" property="diasBloqueo"
                                               sortable="true"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.motivo" property="motivo" width="10"/>
                        <layout:collectionItem title="mensajes.inbox.table.seleccion">
                            <layout:link action="/AutorizarBloqueo" paramName="lista" paramId="idPeticion"
                                         paramProperty="peticionId">
                                <layout:img border="0" page="/images/iconos/16/autorizar.jpg"
                                            altKey="tooltip.administracion.bloqueo.ok"/>
                            </layout:link>
                        </layout:collectionItem>
                        <layout:collectionItem title="mensajes.inbox.table.seleccion">
                            <layout:link action="/DenegarBloqueo" paramName="lista" paramId="idPeticion"
                                         paramProperty="peticionId">
                                <layout:img border="0" page="/images/iconos/16/cancelar.jpg"
                                            altKey="tooltip.administracion.bloqueo.ko"/>
                            </layout:link>
                        </layout:collectionItem>
                    </layout:collection>
                </layout:pager>
            </td>
        </tr>
    </table>
    <table border="1" cellspacing="0" bordercolor="#000000" width="70%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr height="20">
            <td align="center" bgcolor="#996633">
                <b><font color="#FFFFFF" size="2">
                    <bean:message key="peticion.bloqueo.cerradas.titulo"/>
                </font>
                </b>
            </td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2">
                <layout:pager maxPageItems="20">
                    <layout:collection name="BloqueosCerrados" id="lista">
                        <layout:collectionItem title="peticion.bloqueo.lista.usuario" property="nombreUsuario"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.fecha.creacion" property="fechaCreacion"
                                               sortable="true"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.abadia" property="nombreAbadia"
                                               sortable="true"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.region" property="nombreRegion"
                                               sortable="true"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.dias" property="diasBloqueo"
                                               sortable="true"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.motivo" property="motivo"/>
                        <layout:collectionItem title="peticion.bloqueo.lista.resolucion" property="estado"/>
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
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title><html:messages id="edificios.abadia.listado.title"/></title>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body>
<div id="container">
    <div class="alignCenter">
        <logic:equal value="1" name="Links">
            <h3><bean:message key="edificios.abadia.titulo"/></h3>
        </logic:equal>
        <logic:equal value="0" name="Links">
            <h3><bean:message key="mercado.historico.abadia"/>: ${Nombre}</h3><br/>
        </logic:equal>
    </div>

    <div class="alignCenter">
        <%--<p>--%>
        <logic:equal value="1" name="Nieve">
            <img alt="" STYLE="position:absolute;top:40; left:50%; margin-left:-300;"
                 src="/images/mapas/nieve/fondo.gif"/>
        </logic:equal>
        <logic:equal value="0" name="Nieve">
            <logic:equal value="0" name="Noche">
                <img alt="" STYLE="position:absolute;top:40; left:50%; margin-left:-300;"
                     src="/images/mapas/fondo_vacio.jpg"/>
                <!-- añadir objetos animados -->
                <img border="0" alt="Puente" STYLE="position:absolute;top:277; left:50%; margin-left:166;"
                     SRC="/images/mapas/rio_animado.gif"/>
                <!-- esquina izq. -->
                <img border="0" alt="Arbol2" STYLE="position:absolute;top:46; left:50%; margin-left:-250;"
                     SRC="/images/mapas/arbol2.gif"/>
                <img border="0" alt="Arbol2" STYLE="position:absolute;top:80; left:50%; margin-left:-280;"
                     SRC="/images/mapas/arbol2.gif"/>
                <img border="0" alt="Arbol" STYLE="position:absolute;top:68; left:50%; margin-left:-230;"
                     SRC="/images/mapas/arbolanimado1.gif"/>
                <!-- esquina dcha. -->
                <img border="0" alt="Arbol2" STYLE="position:absolute;top:46; left:50%; margin-left:130;"
                     SRC="/images/mapas/arbol2.gif"/>
                <img border="0" alt="Arbol2" STYLE="position:absolute;top:58; left:50%; margin-left:152;"
                     SRC="/images/mapas/arbol2.gif"/>
                <img border="0" alt="Arbol2" STYLE="position:absolute;top:48; left:50%; margin-left:163;"
                     SRC="/images/mapas/arbol2.gif"/>
                <img border="0" alt="Arbol2" STYLE="position:absolute;top:46; left:50%; margin-left:178;"
                     SRC="/images/mapas/arbol2.gif"/>
                <img border="0" alt="Arbol" STYLE="position:absolute;top:76; left:50%; margin-left:180;"
                     SRC="/images/mapas/arbolanimado1.gif"/>
                <img border="0" alt="Arbol" STYLE="position:absolute;top:86; left:50%; margin-left:240;"
                     SRC="/images/mapas/arbolanimado1.gif"/>
            </logic:equal>
            <logic:equal value="1" name="Noche">
                <img alt="" STYLE="position:absolute;top:40; left:50%; margin-left:-300;"
                     src="/images/mapas/noche/fondo.jpg"/>
            </logic:equal>
        </logic:equal>
        <!--img alt=&quot;&quot; STYLE=&quot;position:absolute;top:40; left:50%; margin-left:-300;&quot; SRC=&quot;images/mapas/murallasmadera.gif&quot;-->
        <logic:iterate id="Edificio" name="Edificios">
            <logic:equal value="1" name="Links">
                <html:link action="/mostrarEdificio" paramId="clave" paramName="Edificio" paramProperty="idDeEdificio">
                    <logic:equal value="1" name="Nieve">
                        <img border="0" alt="<bean:write name="Edificio" property="nombre"/>"
                             STYLE="position:absolute;top:<bean:write name="Edificio" property="map_y"/>; left:50%; margin-left:<bean:write name="Edificio" property="map_x"/>;"
                             SRC="images/mapas/nieve/<bean:write name="Edificio" property="grafico_visualizacion"/>_<bean:write name="Edificio" property="nivel"/>.gif"/>
                    </logic:equal>
                    <logic:equal value="0" name="Nieve">
                        <logic:equal value="0" name="Noche">
                            <img border="0" alt="<bean:write name="Edificio" property="nombre"/>"
                                 STYLE="position:absolute;top:<bean:write name="Edificio" property="map_y"/>; left:50%; margin-left:<bean:write name="Edificio" property="map_x"/>;"
                                 SRC="images/mapas/<bean:write name="Edificio" property="grafico_visualizacion"/>_<bean:write name="Edificio" property="nivel"/>.gif"/>
                        </logic:equal>
                        <logic:equal value="1" name="Noche">
                            <img border="0" alt="<bean:write name="Edificio" property="nombre"/>"
                                 STYLE="position:absolute;top:<bean:write name="Edificio" property="map_y"/>; left:50%; margin-left:<bean:write name="Edificio" property="map_x"/>;"
                                 SRC="images/mapas/noche/<bean:write name="Edificio" property="grafico_visualizacion"/>_<bean:write name="Edificio" property="nivel"/>.gif"/>
                        </logic:equal>
                    </logic:equal>
                </html:link>
            </logic:equal>
            <logic:equal value="0" name="Links">
                <a>
                    <img border="0" alt="<bean:write name="Edificio" property="nombre"/>"
                         STYLE="position:absolute;top:<bean:write name="Edificio" property="map_y"/>; left:50%; margin-left:<bean:write name="Edificio" property="map_x"/>;"
                         SRC="images/mapas/<bean:write name="Edificio" property="grafico_visualizacion"/>_<bean:write name="Edificio" property="nivel"/>.gif"/>
                </a>
            </logic:equal>
        </logic:iterate>
        <%--</p>--%>
    </div>
    <div class="break"></div>
    <div class="listBuilding">
        <logic:iterate id="Edificio" name="Edificios">
            <div class="viewBuilding">
                <table>
                    <thead>
                    <tr>
                        <th><bean:write name="Edificio" property="nombre"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="content">
                        <td>
                            <dl>
                                <dt><bean:message key="edificios.abadia.fechaconstruccion"/></dt>
                                <dd><bean:write name="Edificio" property="fechaDeConstruccion"/></dd>
                                <dt><bean:message key="edificios.abadia.nivel"/></dt>
                                <dd><bean:write name="Edificio" property="nivel"/></dd>
                                <dt><bean:message key="edificios.abadia.estado"/></dt>
                                <dd><bean:write name="Edificio" property="barraEstado" filter="false"/></dd>
                                <dt><bean:message key="edificios.abadia.mostrar.mantenimiento"/></dt>
                                <dd><bean:write name="Edificio" property="mantenimientoDesc"/></dd>
                                <dt><bean:message key="edificios.abadia.capacidad"/></dt>
                                <dd><bean:write name="Edificio" property="capacidadVital"/></dd>
                                <dt><bean:message key="edificios.abadia.almacen"/></dt>
                                <dd><bean:write name="Edificio" property="almacenamiento"/> <strong> +<bean:write
                                        name="Edificio" property="almacenamiento_plus" filter="false"/></strong></dd>
                            </dl>
                        </td>
                    </tr>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td>
                            <logic:equal value="1" name="Links">
                                <html:link action="/mostrarEdificio" paramId="clave" paramName="Edificio"
                                           paramProperty="idDeEdificio">
                                    <bean:message key="edificios.abadia.masinfo"/>
                                </html:link>
                            </logic:equal>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </logic:iterate>
    </div>
    <!-- Publicidad -->
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
    <jsp:include page="/abadias/congelado.jsp"/>
</div>
</body>

</html>

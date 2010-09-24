<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<head>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>

<div id="container">

    <div id="bookFilter">
        <html:form action="/mostrarEdificio" styleClass="alignCenter">
            <input type="hidden" id="Tab" name="Tab" value="general"/>
            <input type="hidden" id="clave" name="clave" value="${Edificio.idDeEdificio}"/>
            <ul class="alignCenter">
                <li class="formone"><bean:message key="edificios.abadia.biblioteca.region"/></li>
                <li>
                    <html:select name="FiltroLibros" property="region">
                        <html:options collection="tabla_regiones" property="id" labelProperty="description"/>
                    </html:select>
                </li>
                <li class="formder"><bean:message key="edificios.abadia.biblioteca.abadia"/></li>
                <li>
                    <html:select property="abadia" name="FiltroLibros"
                                 titleKey="edificios.abadia.biblioteca.abadia">
                        <html:options collection="tabla_abadias" property="id" labelProperty="description"/>
                    </html:select>
                </li>
                <li class="formder"><bean:message key="edificios.abadia.biblioteca.idioma"/></li>
                <li>
                    <html:select name="FiltroLibros" property="idioma"
                                 titleKey="edificios.abadia.biblioteca.idioma">
                        <html:options collection="tabla_idiomas" property="id" labelProperty="description"/>
                    </html:select>
                </li>
                <!--categoría-->
                <li class="formder"><bean:message key="edificios.abadia.biblioteca.categoria"/></li>
                <li>
                    <html:select name="FiltroLibros" property="categoria"
                                 titleKey="edificios.abadia.biblioteca.categoria">
                        <html:options collection="tabla_categorias" property="id" labelProperty="description"/>
                    </html:select>
                </li>
                <li class="formder"><bean:message key="edificios.abadia.biblioteca.libro"/></li>
                <li>
                    <html:select name="FiltroLibros" property="libro"
                                 titleKey="edificios.abadia.biblioteca.nombre">
                        <html:options collection="tabla_libros" property="id" labelProperty="description"/>
                    </html:select>
                </li>
                <li class="formder">
                    <html:checkbox name="FiltroLibros" property="disponible">
                        <strong><bean:message key="libro.detalle.disponible"/></strong>
                    </html:checkbox>
                </li>
                <li class="formder">
                    (<bean:message key="libro.detalle.max.registros" arg0="200"/>)
                </li>
            </ul>
            <ul class="floatRight">
                <li>
                    <html:submit>
                        <bean:message key="buscar.abadia.boton.buscar"/>
                    </html:submit>
                </li>
            </ul>
        </html:form>
    </div>
    <div class="break"></div>
    <display:table name="requestScope.Edificio.contenido" uid="edificio_biblioteca_general" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_biblioteca_general.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_biblioteca_general.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_biblioteca_general.csv"/>
        <display:column property="nombreRegion" titleKey="edificios.abadia.biblioteca.region" sortable="true"
                        class="textLeft"/>
        <display:column property="nombreAbadia" titleKey="edificios.abadia.biblioteca.abadia" sortable="true"
                        class="textLeft"/>
        <display:column property="nombreLibroNivel" titleKey="edificios.abadia.biblioteca.nombre" sortable="true"
                        class="textLeft"/>
        <display:column property="nombreCategoria" titleKey="edificios.abadia.biblioteca.categoria" sortable="true"
                        class="textLeft"/>
        <display:column property="precioCopia" titleKey="edificios.abadia.biblioteca.preciocopia"
                        format="{0,number,#,##0}" sortable="true"/>
        <display:column property="idioma_desc" titleKey="edificios.abadia.biblioteca.idioma"/>
        <display:column property="descEstado" titleKey="edificios.abadia.biblioteca.estado"/>
        <display:column property="deterioro" titleKey="edificios.abadia.biblioteca.deterioro"/>
        <display:column property="ocupacion" titleKey="edificios.abadia.biblioteca.ocupacion"/>
        <display:column property="fecha_creacion" titleKey="edificios.abadia.biblioteca.fecha_creacion"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <c:if test="${edificio_biblioteca_general.estado == 2}">
                <layout:link action="/mostrarMonjesCopia.do" paramName="edificio_biblioteca_general"
                             paramProperty="idLibro" paramId="clave">
                    <layout:img border="0" page="/images/iconos/16/copiar.gif"
                                altKey="tooltip.copiarlibro"/>
                </layout:link>
            </c:if>
            <layout:link action="/mostrarDetalleLibro.do" paramName="edificio_biblioteca_general"
                         paramProperty="idLibro" paramId="clave">
                <layout:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.detallelibro"/>
            </layout:link>
        </display:column>
    </display:table>
</div>


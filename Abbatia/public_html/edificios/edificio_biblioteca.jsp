<%@ page import="org.abbatia.bean.Edificio" %>
<%@ page import="org.abbatia.bean.Libro" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-layout.tld" prefix="layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>


<%--
<head>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
    <script src="/theme/popup/prototype.js" type="text/javascript"></script>
    <script src="/theme/popup/effects.js" type="text/javascript"></script>
    <script src="/theme/popup/dragdrop.js" type="text/javascript"></script>
    <script src="/theme/popup/popup.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/theme/popup/popup.css" type="text/css" />
</head>
--%>

<%--<div id="popup_4" class="popup"><jsp:include page="/mostrarDetalleLibro.do?clave=19417"/></div>--%>
<%--
<script type="text/javascript">
  //<![CDATA[
  new Popup('popup_4','popup_link_4',{modal:true,duration:1})
  //]]>
</script>
--%>


<div id="container">

    <%
        List alLibrosTodos = ((Edificio) request.getAttribute("Edificio")).getContenido();
        ArrayList alLibrosEnProceso = new ArrayList();
        ArrayList alLibrosCompletos = new ArrayList();
        Iterator itLibrosTodos;
        int iPageSize;

        Libro oLibro;
        itLibrosTodos = alLibrosTodos.iterator();
        while (itLibrosTodos.hasNext()) {
            oLibro = (Libro) itLibrosTodos.next();
            if (oLibro.getEstado() != 2 && oLibro.getEstado() != 5 && oLibro.getEstado() != 7 && oLibro.getEstado() != 8) {
                alLibrosEnProceso.add(oLibro);
            } else {
                alLibrosCompletos.add(oLibro);
            }
        }
        pageContext.setAttribute("librosEnProceso", alLibrosEnProceso);
        pageContext.setAttribute("librosCompletos", alLibrosCompletos);

        if (session.getAttribute("pagesize") == null) {
            iPageSize = 10;
            //session.setAttribute("pagesize", 10);
        } else {
            iPageSize = (Integer) session.getAttribute("pagesize");
        }
    %>

    <div class="break"></div>
    <div id="bookFilter">
        <ul class="floatLeft">
            <li class="three">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=10">
                    <bean:message key="edificios.abadia.biblioteca.mostrar.10"/>
                </html:link>
            </li>
            <li class="three">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=20">
                    <bean:message key="edificios.abadia.biblioteca.mostrar.20"/>
                </html:link>
            </li>
            <li class="three">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=30">
                    <bean:message key="edificios.abadia.biblioteca.mostrar.30"/>
                </html:link>
            </li>
            <li class="three">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=1000">
                    <bean:message key="edificios.abadia.biblioteca.mostrar.todos"/>
                </html:link>
            </li>
        </ul>
        <ul class="floatRight">
            <li class="one">
                <html:link action="/restaurarLibro">
                    <bean:message key="edificios.abadia.biblioteca.restaurar.todos"/>
                </html:link>
            </li>
            <li class="two">
                <html:link action="/cancelarRestauracionLibro">
                    <bean:message key="edificios.abadia.biblioteca.no.restaurar.todos"/>
                </html:link>
            </li>
        </ul>
    </div>
    <div class="break"></div>
    <display:table name="pageScope.librosCompletos" uid="edificio_biblioteca_local" pagesize="<%=iPageSize%>"
                   export="true" requestURI="/mostrarEdificio.do"
                   sort="list" class="contenidoedificio alignCenter">
        <%--<display:caption><bean:message key="edificios.abadia.biblioteca.titulo"/></display:caption>--%>
        <display:setProperty name="export.xsl.filename" value="edificio_biblioteca_local.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_biblioteca_local.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_biblioteca_local.csv"/>
        <display:column property="idLibro" class="ocultar" headerClass="ocultar"/>
        <display:column titleKey="edificios.abadia.biblioteca.nombre" sortable="true" class="textLeft">
            ${edificio_biblioteca_local.nombreLibro} (${edificio_biblioteca_local.nivel})
        </display:column>
        <display:column property="precioCopia" format="{0,number,#,##0}"
                        titleKey="edificios.abadia.biblioteca.preciocopia" sortable="true"/>
        <%--href="/actualizarPrecioCopia.do" paramId="clave" paramProperty="idLibro"/>--%>
        <display:column property="idioma_desc" titleKey="edificios.abadia.biblioteca.idioma" sortable="true"/>
        <display:column property="descEstado" titleKey="edificios.abadia.biblioteca.estado" sortable="true"/>
        <display:column property="deterioro" titleKey="edificios.abadia.biblioteca.deterioro"/>
        <display:column property="ocupacion" titleKey="edificios.abadia.biblioteca.ocupacion"/>
        <display:column property="fecha_creacion" titleKey="edificios.abadia.biblioteca.fecha_creacion"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <c:choose>
                <c:when test="${edificio_biblioteca_local.estado == 2}">
                    <%--                    <a id="popup_link_4" href="#">t</a>--%>
                    <html:link action="/mostrarMonjesCopia" paramName="edificio_biblioteca_local"
                               paramProperty="idLibro" paramId="clave">
                        <html:img border="0" page="/images/iconos/16/copiar.gif" altKey="tooltip.copiarlibro"/>
                    </html:link>
                    <html:link action="/mostrarDetalleLibro" paramName="edificio_biblioteca_local"
                               paramProperty="idLibro" paramId="clave">
                        <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.detallelibro"/>
                    </html:link>
                    <html:link action="/mostrarMonjesSubirNivelCopia" paramName="edificio_biblioteca_local"
                               paramProperty="idLibro"
                               paramId="clave">
                        <html:img border="0" page="/images/iconos/16/subirnivel.gif" altKey="tooltip.libro.subirnivel"/>
                    </html:link>
                    <c:if test="${edificio_biblioteca_local.desgaste > 0}">
                        <%--
                                                <a href="javascript:restaurar(${edificio_biblioteca_local.idLibro},'restaurar_${edificio_biblioteca_local_rowNum}')">
                                                    <img src="/images/iconos/16/restaurarlibro.gif" border="0" id="restaurar_${edificio_biblioteca_local_rowNum}" alt="<bean:message key='tooltip.libro.restaurar'/>">
                                                </a>
                        --%>
                        <html:link action="/restaurarLibro" paramName="edificio_biblioteca_local"
                                   paramProperty="idLibro" paramId="claveLibro">
                            <html:img border="0" page="/images/iconos/16/restaurarlibro.gif"
                                      altKey="tooltip.libro.restaurar"/>
                        </html:link>
                    </c:if>
                </c:when>
                <c:when test="${edificio_biblioteca_local.estado == 7}">
                    <html:link action="/mostrarDetalleLibro" paramName="edificio_biblioteca_local"
                               paramProperty="idLibro" paramId="clave">
                        <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.detallelibro"/>
                    </html:link>
                    <html:link action="/restaurarLibro" paramName="edificio_biblioteca_local" paramProperty="idLibro"
                               paramId="claveLibro">
                        <html:img border="0" page="/images/iconos/16/restaurarlibro.gif"
                                  altKey="tooltip.libro.restaurar"/>
                    </html:link>
                </c:when>
                <c:when test="${edificio_biblioteca_local.estado == 8}">
                    <html:link action="/mostrarDetalleLibro" paramName="edificio_biblioteca_local"
                               paramProperty="idLibro" paramId="clave">
                        <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.detallelibro"/>
                    </html:link>
                    <html:link action="/cancelarRestauracionLibro" paramName="edificio_biblioteca_local"
                               paramProperty="idLibro"
                               paramId="claveLibro">
                        <html:img border="0" page="/images/iconos/16/anular_restaurarlibro.gif"
                                  altKey="tooltip.libro.abandonarrestauracion"/>
                    </html:link>
                </c:when>
                <c:when test="${edificio_biblioteca_local.estado == 5}">
                    <html:link action="/mostrarDetalleLibro" paramName="edificio_biblioteca_local"
                               paramProperty="idLibro" paramId="clave">
                        <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.detallelibro"/>
                    </html:link>
                    <html:link action="/encuadernarLibro" paramName="edificio_biblioteca_local" paramProperty="idLibro"
                               paramId="claveLibro">
                        <html:img border="0" page="/images/iconos/16/encuadernar.gif"
                                  altKey="tooltip.encuadernarlibro"/>
                    </html:link>
                </c:when>
            </c:choose>
        </display:column>
    </display:table>
    <div class="break"></div>
    <display:table name="pageScope.librosEnProceso" uid="edificio_biblioteca_local_copias" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:caption><bean:message key="edificios.abadia.biblioteca.titulocopiando"/></display:caption>
        <display:setProperty name="export.xsl.filename" value="edificio_biblioteca_local.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_biblioteca_local.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_biblioteca_local.csv"/>
        <display:column titleKey="edificios.abadia.biblioteca.nombre" sortable="true" class="textLeft">
            ${edificio_biblioteca_local_copias.nombreLibro} (${edificio_biblioteca_local_copias.nivel})
        </display:column>
        <display:column titleKey="edificios.abadia.biblioteca.monje" sortable="true">
            ${edificio_biblioteca_local_copias.nombreMonje}
            <strong>(${edificio_biblioteca_local_copias.nombreAbadia})</strong>
        </display:column>
        <display:column property="idioma_desc" titleKey="edificios.abadia.biblioteca.idioma" sortable="true"/>
        <display:column property="descEstado" titleKey="edificios.abadia.biblioteca.estado" sortable="true"/>
        <display:column property="progreso" titleKey="edificio.abadia.biblioteca.progreso"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <html:link action="/mostrarDetalleLibro" paramName="edificio_biblioteca_local_copias"
                       paramProperty="idLibro_origen"
                       paramId="clave">
                <html:img border="0" page="/images/iconos/16/ojo.gif"
                          altKey="tooltip.detallelibro"/>
            </html:link>
            <c:if test="${edificio_biblioteca_local_copias.esCopiaLocal == 1}">
                <html:link action="/eliminarLibro" paramName="edificio_biblioteca_local_copias" paramProperty="idLibro"
                           paramId="clave" onclick="return confirmarEliminacion()">
                    <html:img border="0" page="/images/iconos/16/cancelar.jpg"
                              altKey="tooltip.borrarlibro"/>
                </html:link>
            </c:if>

        </display:column>
    </display:table>
</div>

<script type="text/javascript">
    /*    new Popup('popup_4','popup_link_4',{modal:true,duration:1});*/
    //    addColumnHandlersJS('edificio_biblioteca_local', 'fijarPrecio', 'p_LibroId', 2, 0);
    addColumnsHandlersJS('edificio_biblioteca_local', 'fijarPrecio', 0, 'rowMouseOverPointer', 9);
    function cargadinamica(p_libroid)
    {
        document.body.addClassName('waiting');
        new Ajax.Request('<html:rewrite page="/mostrarDetalleLibro.do"/>', {
            parameters: {clave: p_libroid},
            onComplete: function() {
                document.body.removeClassName('waiting');
            },
            onSuccess: function(transport, json) {
                if (json.executeError)
                {
                    alert(json.executeError);
                }
                else
                {
                    document.getElementById('popup_4').innerHTML = json.toHTML();
                }

            }
        });
    }
    function restaurar(p_libroid, p_id)
    {
        document.body.addClassName('waiting');
        new Ajax.Request('<html:rewrite page="/json/restaurarLibro.do"/>', {
            parameters: {claveLibro: p_libroid},
            onComplete: function() {
                document.body.removeClassName('waiting');
            },
            onSuccess: function(transport, json) {
                if (json.executeError)
                {
                    alert(json.executeError);
                }
                else
                {
                    if (json.retorno == 'OK')
                    {
                        document.getElementById('p_id').src = '/images/iconos/16/anular_restaurarlibro.gif';
                        document.refresh();
                    } else
                    {
                        alert(json.retorno);
                    }
                }

            }
        });
    }
</script>
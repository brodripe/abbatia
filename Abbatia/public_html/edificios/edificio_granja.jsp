<%@ page import="org.abbatia.bean.Animal" %>
<%@ page import="org.abbatia.bean.Edificio" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<head>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/granja.css" TYPE="text/css"/>
</head>

<SCRIPT LANGUAGE="JavaScript" type="text/javascript">

    function checkOrUncheck() {
        if (document.SacrificioForm.CheckAll.checked)
            checkAll(document.SacrificioForm.seleccion);
        else
            uncheckAll(document.SacrificioForm.seleccion);
    }

    function checkAll(aCheck) {
        for (i = 0; i < aCheck.length; i++)
            aCheck[i].checked = true;
    }

    function uncheckAll(aCheck) {
        for (i = 0; i < aCheck.length; i++)
            aCheck[i].checked = false;
    }

</script>


<div id="container">

<%
    ArrayList alAnimalesTodos = ((Edificio) request.getAttribute("Edificio")).getContenido();
    ArrayList alAnimalesNormal = new ArrayList();
    ArrayList alAnimalesAislados = new ArrayList();
    ArrayList alAnimalesEmbarazo = new ArrayList();
    Iterator itAnimalesTodos;
    String sTipoAnimal = request.getParameter("tipoAnimal");
    int iTipoHembra;
    int iTipoMacho;
    int iTipoOtro = 0;

    StringTokenizer stTipoAnimal;
    Animal oAnimal;
    itAnimalesTodos = alAnimalesTodos.iterator();
    while (itAnimalesTodos.hasNext()) {
        oAnimal = (Animal) itAnimalesTodos.next();
        if (oAnimal.getFecha_embarazo().equals("00-00-0000")) {
            if (oAnimal.getAislado() == 0) {
                if ((sTipoAnimal != null) && (!sTipoAnimal.equals(""))) {
                    stTipoAnimal = new StringTokenizer(sTipoAnimal, "|");
                    iTipoHembra = Integer.valueOf(stTipoAnimal.nextToken());
                    iTipoMacho = Integer.valueOf(stTipoAnimal.nextToken());
                    if (stTipoAnimal.hasMoreTokens())
                        iTipoOtro = Integer.valueOf(stTipoAnimal.nextToken());
                    if (oAnimal.getTipoAnimalid() == iTipoMacho || oAnimal.getTipoAnimalid() == iTipoHembra || oAnimal.getTipoAnimalid() == iTipoOtro) {
                        alAnimalesNormal.add(oAnimal);
                    }
                } else {
                    alAnimalesNormal.add(oAnimal);
                }
            } else {
                alAnimalesAislados.add(oAnimal);
            }
        } else {
            alAnimalesEmbarazo.add(oAnimal);
        }
    }
    pageContext.setAttribute("AnimalesNormal", alAnimalesNormal);
    pageContext.setAttribute("AnimalesAislados", alAnimalesAislados);
    pageContext.setAttribute("AnimalesEmbarazo", alAnimalesEmbarazo);
%>
<html:form action="/sacrificar_animal_grupo">
    <div id="bookFilter">
            <%--
                    <ul class="floatLeft">
                        <li class="three">
                            <c:if test="${sessionScope.pagesize==1000}">
                                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=10">
                                    <bean:message key="edificios.abadia.cocina.alimentos.filtro.paginacion"/>
                                </html:link>
                            </c:if>
                            <c:if test="${sessionScope.pagesize!=1000}">
                                <html:link
                                        action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=1000">
                                    <bean:message key="edificios.abadia.cocina.alimentos.filtro.todos"/>
                                </html:link>
                            </c:if>
                        </li>
                    </ul>
            --%>
        <ul class="floatRight">
            <li class="alamiddle">
                <c:if test="${sessionScope.pagesize==1000}">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=10">
                        <span class="ojo2"><bean:message key="edificios.abadia.cocina.alimentos.filtro.paginacion"/>
                    </html:link>
                </c:if>
                <c:if test="${sessionScope.pagesize!=1000}">
                    <html:link
                            action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&pagesize=1000">
                        <span class="ojo2"><bean:message key="edificios.abadia.cocina.alimentos.filtro.todos"/>
                    </html:link>
                </c:if>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=2|11|1">
                    <span class="vaca"></span><bean:message key="animal.vaca"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=14|15">
                    <span class="oveja"></span><bean:message key="animal.oveja"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=13|12">
                    <span class="cabra"></span><bean:message key="animal.cabra"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=8|9">
                    <span class="cerdo"></span><bean:message key="animal.cerdo"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=5|10">
                    <span class="conejo"></span><bean:message key="animal.conejo"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=4|6">
                    <span class="gallina"></span><bean:message key="animal.gallina"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=18|19">
                    <span class="ganso"></span><bean:message key="animal.ganso"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=20|21">
                    <span class="faisan"></span><bean:message key="animal.faisan"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=24|25">
                    <span class="codorniz"></span><bean:message key="animal.codorniz"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=22|23">
                    <span class="pato"></span><bean:message key="animal.pato"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=26|27">
                    <span class="perdiz"></span><bean:message key="animal.perdiz"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <html:link action="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}&tipoAnimal=">
                    <span class="todos"></span><bean:message key="animal.todos"/>
                </html:link>
            </li>
            <li class="alamiddle">
                <a href="#" onclick="document.SacrificioForm.submit();">
                    <span class="sacrificar"></span><bean:message
                        key="edificios.abadia.granja.animales.tabla.sacrificar.grupo"/>
                </a>
            </li>
        </ul>
    </div>
    <div class="break"></div>
    <!-- Animales Normal -->
    <display:table name="pageScope.AnimalesNormal" uid="edificio_granja" pagesize="${sessionScope.pagesize}"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_granja.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_granja.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_granja.csv"/>
        <display:caption><bean:message key="edificios.abadia.granja.animales"/></display:caption>
        <display:column title="<input type='checkbox' name='CheckAll' onClick='checkOrUncheck()' />">
            <html:multibox name="SacrificioForm" property="seleccion">
                ${edificio_granja.clave}
            </html:multibox>
        </display:column>
        <display:column property="cantidad" titleKey="edificios.abadia.granja.animales.tabla.cantidad"
                        sortable="true"/>
        <display:column property="nivel" titleKey="edificios.abadia.granja.animales.tabla.nivel" sortable="true"/>
        <display:column titleKey="edificios.abadia.granja.animales.tabla.descripcion" sortable="true"
                        class="textLeft">
            ${edificio_granja.descTipo} (<strong>${edificio_granja.nombre}</strong>)
        </display:column>
        <display:column property="barra_HTML" titleKey="edificios.abadia.granja.animales.tabla.salud"
                        sortable="true"/>
        <display:column property="fecha_nacimiento"
                        titleKey="edificios.abadia.granja.animales.tabla.fechanacimiento"
                        sortable="true"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <a href="javascript:sacrificarAnimal('${edificio_granja.clave}');">
                <html:img border="0" page="/images/iconos/16/sacrificar.jpg" altKey="tooltip.sacrificar"/>
            </a>
            <a href="javascript:vender('${edificio_granja.clave}', 'N');">
                <html:img border="0" page="/images/iconos/16/vender.jpg" altKey="tooltip.vender"/>
            </a>
            <a href="/aislarAnimal.do?claveAnimal=${edificio_granja.animalid}">
                <html:img border="0" page="/images/iconos/16/separar.gif" altKey="tooltip.aislar"/>
            </a>
            <c:if test="${edificio_granja.tipoAnimalid == 11}">
                <a href="/caparToro.do?claveAnimal=${edificio_granja.animalid}">
                    <html:img border="0" page="/images/iconos/16/capar.gif" altKey="tooltip.capar"/>
                </a>
            </c:if>
            <c:if test="${edificio_granja.puedeTrabajar == 1}">
                <html:link action="/asignarAnimalAgricultura" paramId="claveAnimal" paramName="edificio_granja"
                           paramProperty="animalid">
                    <c:if test="${edificio_granja.trabaja == 1}">
                        <html:img border="0" page="/images/iconos/16/trabaja_no.gif"
                                  altKey="tooltip.trabaja.agricultura.no"/>
                    </c:if>
                    <c:if test="${edificio_granja.trabaja == 0}">
                        <html:img border="0" page="/images/iconos/16/trabaja_si.gif"
                                  altKey="tooltip.trabaja.agricultura.si"/>
                    </c:if>
                </html:link>
            </c:if>
        </display:column>
    </display:table>
</html:form>

<!-- Animales Aislados -->
<display:table name="pageScope.AnimalesAislados" uid="edificio_granja_aislados" pagesize="${sessionScope.pagesize}"
               export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
               sort="list" class="contenidoedificio alignCenter">
    <display:setProperty name="export.xsl.filename" value="edificio_granja_aislados.xsl"/>
    <display:setProperty name="export.pdf.filename" value="edificio_granja_aislados.pdf"/>
    <display:setProperty name="export.csv.filename" value="edificio_granja_aislados.csv"/>
    <display:caption><bean:message key="edificios.abadia.granja.aislados"/></display:caption>
    <display:column property="cantidad" titleKey="edificios.abadia.granja.animales.tabla.cantidad"
                    sortable="true"/>
    <display:column property="nivel" titleKey="edificios.abadia.granja.animales.tabla.nivel" sortable="true"/>
    <display:column titleKey="edificios.abadia.granja.animales.tabla.descripcion" sortable="true" class="textLeft">
        ${edificio_granja_aislados.descTipo} (<strong>${edificio_granja_aislados.nombre}</strong>)
    </display:column>
    <display:column property="barra_HTML" titleKey="edificios.abadia.granja.animales.tabla.salud" sortable="true"/>
    <display:column property="fecha_nacimiento" titleKey="edificios.abadia.granja.animales.tabla.fechanacimiento"
                    sortable="true"/>
    <display:column titleKey="edificios.abadia.tabla.opciones">
        <a href="javascript:sacrificarAnimal('${edificio_granja_aislados.clave}');">
            <html:img border="0" page="/images/iconos/16/sacrificar.jpg" altKey="tooltip.sacrificar"/>
        </a>
        <a href="javascript:vender('${edificio_granja_aislados.clave}', 'N');">
            <html:img border="0" page="/images/iconos/16/vender.jpg" altKey="tooltip.vender"/>
        </a>
        <a href="/devolverAnimal.do?claveAnimal=${edificio_granja_aislados.animalid}">
            <html:img border="0" page="/images/iconos/16/separar.gif" altKey="tooltip.aislar"/>
        </a>
        <c:if test="${edificio_granja_aislados.tipoAnimalid == 11}">
            <a href="/caparToro.do?claveAnimal=${edificio_granja_aislados.animalid}">
                <html:img border="0" page="/images/iconos/16/capar.gif" altKey="tooltip.capar"/>
            </a>
        </c:if>
        <c:if test="${edificio_granja_aislados.puedeTrabajar == 1}">
            <html:link action="/asignarAnimalAgricultura" paramId="claveAnimal" paramName="edificio_granja_aislados"
                       paramProperty="animalid">
                <c:if test="${edificio_granja_aislados.trabaja == 1}">
                    <html:img border="0" page="/images/iconos/16/trabaja_no.gif"
                              altKey="tooltip.trabaja.agricultura.no"/>
                </c:if>
                <c:if test="${edificio_granja_aislados.trabaja == 0}">
                    <html:img border="0" page="/images/iconos/16/trabaja_si.gif"
                              altKey="tooltip.trabaja.agricultura.si"/>
                </c:if>
            </html:link>
        </c:if>
    </display:column>
</display:table>

<!-- Animales Embarazados -->
<display:table name="pageScope.AnimalesEmbarazo" uid="edificio_granja_embarazo" pagesize="${sessionScope.pagesize}"
               export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
               sort="list" class="contenidoedificio alignCenter">
    <display:setProperty name="export.xsl.filename" value="edificio_granja_embarazo.xsl"/>
    <display:setProperty name="export.pdf.filename" value="edificio_granja_embarazo.pdf"/>
    <display:setProperty name="export.csv.filename" value="edificio_granja_embarazo.csv"/>
    <display:caption><bean:message key="edificios.abadia.granja.animales.embarazadas"/></display:caption>
    <display:column property="cantidad" titleKey="edificios.abadia.granja.animales.tabla.cantidad"
                    sortable="true"/>
    <display:column property="nivel" titleKey="edificios.abadia.granja.animales.tabla.nivel" sortable="true"/>
    <display:column titleKey="edificios.abadia.granja.animales.tabla.descripcion" sortable="true" class="textLeft">
        ${edificio_granja_embarazo.descTipo} (<strong>${edificio_granja_embarazo.nombre}</strong>)
    </display:column>
    <display:column property="barra_HTML" titleKey="edificios.abadia.granja.animales.tabla.salud" sortable="true"/>
    <display:column property="fecha_nacimiento" titleKey="edificios.abadia.granja.animales.tabla.fechanacimiento"
                    sortable="true"/>
    <display:column property="fecha_embarazo" titleKey="edificios.abadia.granja.animales.tabla.fechaparto"
                    sortable="true"/>
</display:table>

</div>

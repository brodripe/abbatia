<%@ page import="org.abbatia.bean.Animal" %>
<%@ page import="org.abbatia.bean.Edificio" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
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
</head>

<div id="container">

    <%
        ArrayList alAnimalesTodos = ((Edificio) request.getAttribute("Edificio")).getContenido();
        ArrayList alAnimalesNormal = new ArrayList();
        ArrayList alAnimalesAislados = new ArrayList();
        ArrayList alAnimalesEmbarazo = new ArrayList();
        Iterator itAnimalesTodos;

        Animal oAnimal;
        itAnimalesTodos = alAnimalesTodos.iterator();
        while (itAnimalesTodos.hasNext()) {
            oAnimal = (Animal) itAnimalesTodos.next();
            if (oAnimal.getFecha_embarazo().equals("00-00-0000")) {
                if (oAnimal.getAislado() == 0) {
                    alAnimalesNormal.add(oAnimal);
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
    <!-- Animales Normal -->
    <display:table name="pageScope.AnimalesNormal" uid="edificio_establo" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_establo.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_establo.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_establo.csv"/>
        <display:caption><bean:message key="edificios.abadia.granja.animales"/></display:caption>
        <display:column property="cantidad" titleKey="edificios.abadia.granja.animales.tabla.cantidad"
                        sortable="true"/>
        <display:column property="nivel" titleKey="edificios.abadia.granja.animales.tabla.nivel" sortable="true"/>
        <display:column titleKey="edificios.abadia.granja.animales.tabla.descripcion" sortable="true" class="textLeft">
            ${edificio_establo.descTipo} (<strong>${edificio_establo.nombre}</strong>)
        </display:column>
        <display:column property="barra_HTML" titleKey="edificios.abadia.granja.animales.tabla.salud" sortable="true"/>
        <display:column property="fecha_nacimiento" titleKey="edificios.abadia.granja.animales.tabla.fechanacimiento"
                        sortable="true"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <a href="javascript:vender('${edificio_establo.clave}', 'N');">
                <html:img border="0" page="/images/iconos/16/vender.jpg" altKey="tooltip.vender"/>
            </a>
            <a href="/aislarAnimal.do?claveAnimal=${edificio_establo.animalid}">
                <html:img border="0" page="/images/iconos/16/separar.gif" altKey="tooltip.aislar"/>
            </a>
            <c:if test="${edificio_establo.puedeTrabajar == 1}">
                <html:link action="/asignarAnimalAgricultura" paramId="claveAnimal" paramName="edificio_establo"
                           paramProperty="animalid">
                    <c:if test="${edificio_establo.trabaja == 1}">
                        <html:img border="0" page="/images/iconos/16/trabaja_no.gif"
                                  altKey="tooltip.trabaja.agricultura.no"/>
                    </c:if>
                    <c:if test="${edificio_establo.trabaja == 0}">
                        <html:img border="0" page="/images/iconos/16/trabaja_si.gif"
                                  altKey="tooltip.trabaja.agricultura.si"/>
                    </c:if>
                </html:link>
            </c:if>
        </display:column>
    </display:table>

    <!-- Animales Aislados -->
    <display:table name="pageScope.AnimalesAislados" uid="edificio_establo_Aislados" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_establo_Aislados.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_establo_Aislados.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_establo_Aislados.csv"/>
        <display:caption><bean:message key="edificios.abadia.granja.aislados"/></display:caption>
        <display:column property="cantidad" titleKey="edificios.abadia.granja.animales.tabla.cantidad"
                        sortable="true"/>
        <display:column property="nivel" titleKey="edificios.abadia.granja.animales.tabla.nivel" sortable="true"/>
        <display:column titleKey="edificios.abadia.granja.animales.tabla.descripcion" sortable="true" class="textLeft">
            ${edificio_establo_Aislados.descTipo} (<strong>${edificio_establo_Aislados.nombre}</strong>)
        </display:column>
        <display:column property="barra_HTML" titleKey="edificios.abadia.granja.animales.tabla.salud" sortable="true"/>
        <display:column property="fecha_nacimiento" titleKey="edificios.abadia.granja.animales.tabla.fechanacimiento"
                        sortable="true"/>
        <display:column titleKey="edificios.abadia.tabla.opciones">
            <a href="javascript:vender('${edificio_establo_Aislados.clave}', 'N');">
                <html:img border="0" page="/images/iconos/16/vender.jpg" altKey="tooltip.vender"/>
            </a>
            <a href="/devolverAnimal.do?claveAnimal=${edificio_establo_Aislados.animalid}">
                <html:img border="0" page="/images/iconos/16/separar.gif" altKey="tooltip.aislar"/>
            </a>
            <c:if test="${edificio_establo_Aislados.puedeTrabajar == 1}">
                <html:link action="/asignarAnimalAgricultura" paramId="claveAnimal" paramName="edificio_establo_Aislados"
                           paramProperty="animalid">
                    <c:if test="${edificio_establo_Aislados.trabaja == 1}">
                        <html:img border="0" page="/images/iconos/16/trabaja_no.gif"
                                  altKey="tooltip.trabaja.agricultura.no"/>
                    </c:if>
                    <c:if test="${edificio_establo_Aislados.trabaja == 0}">
                        <html:img border="0" page="/images/iconos/16/trabaja_si.gif"
                                  altKey="tooltip.trabaja.agricultura.si"/>
                    </c:if>
                </html:link>
            </c:if>
        </display:column>
    </display:table>

    <!-- Animales Embarazados -->
    <display:table name="pageScope.AnimalesEmbarazo" uid="edificio_establo_embarazo" pagesize="10"
                   export="true" requestURI="/mostrarEdificio.do?clave=${requestScope.Edificio.idDeEdificio}"
                   sort="list" class="contenidoedificio alignCenter">
        <display:setProperty name="export.xsl.filename" value="edificio_establo_embarazo.xsl"/>
        <display:setProperty name="export.pdf.filename" value="edificio_establo_embarazo.pdf"/>
        <display:setProperty name="export.csv.filename" value="edificio_establo_embarazo.csv"/>
        <display:caption><bean:message key="edificios.abadia.establo.animales.embarazadas"/></display:caption>
        <display:column property="cantidad" titleKey="edificios.abadia.granja.animales.tabla.cantidad"
                        sortable="true"/>
        <display:column property="nivel" titleKey="edificios.abadia.granja.animales.tabla.nivel" sortable="true"/>
        <display:column titleKey="edificios.abadia.granja.animales.tabla.descripcion" sortable="true" class="textLeft">
            ${edificio_establo_embarazo.descTipo} (<strong>${edificio_establo_embarazo.nombre}</strong>)
        </display:column>
        <display:column property="barra_HTML" titleKey="edificios.abadia.granja.animales.tabla.salud" sortable="true"/>
        <display:column property="fecha_nacimiento" titleKey="edificios.abadia.granja.animales.tabla.fechanacimiento"
                        sortable="true"/>
        <display:column property="fecha_embarazo" titleKey="edificios.abadia.granja.animales.tabla.fechaparto"
                        sortable="true"/>
    </display:table>

</div>

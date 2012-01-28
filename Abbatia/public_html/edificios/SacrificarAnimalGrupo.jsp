<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>


<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title><bean:message key="edificio.abadia.sacrificar.title"/></title>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>
<body>

<div id="containerLite">

    <html:form action="/sacrificar_animal_grupo_confirmar">
        <display:table name="sessionScope.ListaAgrupada" uid="animales_sacrificio" pagesize="100"
                       sort="list" class="contenidoedificio alignCenter">
            <display:caption><bean:message key="edificio.abadia.sacrificar.title"/></display:caption>

            <display:column titleKey="edificios.abadia.granja.animales.tabla.descripcion" sortable="false"
                            class="textLeft">
                ${animales_sacrificio.animal.nombre} (${animales_sacrificio.animal.nivel})
            </display:column>
            <display:column property="numeroAnimales" titleKey="edificios.abadia.granja.animales.tabla.cantidad"
                            sortable="false"/>
            <display:column titleKey="edificio.abadia.sacrificar.alimento.cantidad" sortable="false">
                <img src="/images/iconos/16/icon_meat.png" alt="Carne"/>
                ${animales_sacrificio.datosAlimento.alimento_min}/${animales_sacrificio.datosAlimento.alimento_max}
                <c:if test="${animales_sacrificio.datosRecurso!=null}">
                    <c:if test="${animales_sacrificio.datosRecurso.recurso_id==703}">
                        <img src="/images/iconos/16/icon_feather.gif" alt="Plumas"/>
                    </c:if>
                    <c:if test="${animales_sacrificio.datosRecurso.recurso_id!=703}">
                        <img src="/images/iconos/16/icon_piel.png" alt="Piel"/>
                    </c:if>
                    ${animales_sacrificio.datosRecurso.recurso_min}/${animales_sacrificio.datosRecurso.recurso_max}
                </c:if>
            </display:column>
        </display:table>
        <div class="alignCenter">
            <ul>
                <li>
                    <html:cancel onclick="this.disabled=true;history.back(1);">
                        <bean:message key="general.atras"/>
                    </html:cancel>
                    <html:submit onclick="this.disabled=true; this.form.submit();">
                        <bean:message key="edificios.abadia.granja.sacrificar.confirmar"/>
                    </html:submit>
                </li>
            </ul>
        </div>
    </html:form>
</div>
</body>
</html>
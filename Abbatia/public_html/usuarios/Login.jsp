<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<script type="text/javascript" language="JavaScript">
    function validarCampos()
    {
        if (document.getElementById("usuario").value == "")
        {
            return false;
        }
        if (document.getElementById("pwd").value == "")
        {
            return false;
        }

    }
</script>

<HTML>
<HEAD>
    <TITLE><bean:message key="login.title"/></TITLE>
    <link rel="stylesheet" href="/theme/style-global.css" type="text/css"/>
    <link rel="stylesheet" href="/theme/style-revised.css" type="text/css"/>
    <script language="JavaScript" type="text/javascript" src="/theme/abbatia.js"></script>
</HEAD>
<body>
<div id="container">
    <html:form action="/login" focus="usuario">
        <div id="loginPanel">
            <h3 class="textCenter"><bean:message key="login.entrarenabadia"/></h3>

            <div id="loginAccess">
                <div class="row">
                    <label for="usuario"><bean:message key="login.usuario"/></label><html:text property="usuario"
                                                                                               styleId="usuario"/>
                </div>
                <div class="row">
                    <label for="pwd"><bean:message key="login.pwd"/></label><html:password property="pwd"
                                                                                           redisplay="false"
                                                                                           styleId="pwd"/>
                </div>
                <div class="row">
                    <html:submit onclick="return validarCampos()">
                        <bean:message key="login.validar"/>
                    </html:submit>
                </div>
                <div class="break">&nbsp;</div>
            </div>
            <div id="loginOption">
                <ul>
                    <li>
                        <html:link action="/registro">
                            <bean:message key="login.registrate"/>
                        </html:link>
                    </li>
                    <li>
                        <html:link action="/enviar_password">
                            <bean:message key="login.passwordolvidado"/>
                        </html:link>
                    </li>
                </ul>
            </div>
            <div class="break">&nbsp;</div>
            <div class="break">&nbsp;</div>
            <div class="row">
                <P class="rowRed"><html:errors/></P>
            </div>
            <div class="break">&nbsp;</div>
        </div>
    </html:form>
    <jsp:include page="/varios/publicidad.jsp"/>
</div>
<script type="text/javascript">
    <!--
    var focusControl = document.forms["LoginForm"].elements["usuario"];

    if (focusControl.type != "hidden" && !focusControl.disabled) {
        focusControl.focus();
    }
    // -->
</script>

<%--<p>&nbsp;</p>

<p>&nbsp;</p>--%>
<%--<center>
    <!-- Si disparamos el Action, recoger los datos de los libros e imprimirlos -->
    <logic:present scope="session" name="isbn">

        <!-- FICHA -->
        <table border=0 cellpadding=0 cellspacing=0 width=350 bgcolor="#FFFFFF"
               summary="Casa del Libro - libros : Comprar libros: arte, literatura, economía, derecho, diccionarios, informática, novela, viajes, gastronomía, ...">
            <tr>
                <td width=94></td>
                <td width=13 rowspan=2></td>
                <td width=243></td>
            </tr>
            <tr>
                <td valign=top>

                    <img src="images/casadellibro/<bean:write name="isbn" filter="false"/>.jpg"
                         alt="<bean:write name="titulo" filter="false"/>" width="93" height="141" border=0 vspace=1
                         hspace=1><br>

                </td>
                <td class=tgri11 valign=top align=left>
                    <font class=tve11b><bean:write name="titulo" filter="false"/></font><br>
                    <bean:message key="login.de"/>&nbsp;<span class="tve11u"><bean:write name="autor"
                                                                                         filter="false"/></span>
                    <br>
                    <table border=0 cellpadding=4 cellspacing=4 width=100%>
                        <tr>
                            <td align=center valign=top class=tvg11b>

                            </td>
                            <td>
                            </td>
                        </tr>
                    </table>
                    <br>
                    ISBN: <font class=tgrill><bean:write name="isbn" filter="false"/></font>
                    <br><br>
                    <a href="http://www.casadellibro.com/homeAfiliado?ca=499&isbn=<bean:write name="isbn" filter="false"/>"
                       target="_blank"><img src="/images/casadellibro/anyadirabolsa.gif" width="96" height="21"
                                            border="0" alt="Casa del Libro"></a>
                </td>
            </tr>
            <tr>
                <td colspan=3><font class=tve11b><bean:message key="login.publicidad.casadellibro"/></font></td>
            </tr>
        </table>
        <!-- FIN FICHA -->

    </logic:present>
</center>
<p>&nbsp;
</p>--%>

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

</BODY>
</HTML>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<script type="text/javascript" language="JavaScript">
    function validarCampos() {
        if (document.getElementById("usuario").value == "") {
            return false;
        }
        if (document.getElementById("pwd").value == "") {
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

</BODY>
</HTML>

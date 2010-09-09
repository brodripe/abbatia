<%--
  Created by IntelliJ IDEA.
  User: Benjamin
  Date: 01-nov-2006
  Time: 16:24:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>Mapa General Abbatia</title>
</head>
<body bgcolor="#E1C08B">
<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
        codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="100%"
        height="100%" id="mapabeta" align="middle">
    <param name="allowScriptAccess" value="sameDomain"/>
    <param name="movie" value="images/mapas/flash/mapa.swf"/>
    <param name="quality" value="high"/>
    <param name="bgcolor" value="#E1C08B"/>
    <param name="menu" value="false"/>
    <!--embed src="images/mapas/flash/mapabeta.swf" quality="high" menu="false" bgcolor="#E1C08B" width="700" height="540" name="mapaSendyMas" align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" /-->
    <embed src="images/mapas/flash/mapa.swf" quality="high" menu="false" bgcolor="#E1C08B" width="100%"
           height="100%" name="mapaSendyMas" align="middle" allowScriptAccess="sameDomain"
           type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer"/>
</object>
</body>
</html>
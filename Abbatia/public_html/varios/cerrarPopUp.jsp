<%--
  Created by IntelliJ IDEA.
  User: benjamin.rodriguez
  Date: 21-ago-2007
  Time: 19:59:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<body onload="cerrar();"></body>

<script language="javascript">
  function cerrar()
    {
        parent.location='<%=request.getSession().getAttribute("location")%>';
        //parent.cClick();
    }
</script>
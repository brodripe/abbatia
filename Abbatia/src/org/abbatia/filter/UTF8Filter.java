package org.abbatia.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: BenjaminHP
 * Date: 15/12/11
 * Time: 0:49
 * To change this template use File | Settings | File Templates.
 */

/**
 * Filtro para que la aplicación acepte codificación en formato UTF-8
 */
public class UTF8Filter implements Filter {
    private String encoding;

    /**
     * Recogemos el tipo de codificación definido en el web.xml
     * Si no se hubiera especificado ninguno se toma "UTF-8" por defecto
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("requestEncoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    /**
     * Metemos en la request el formato de codificacion UTF-8
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        fc.doFilter(request, response);
    }

    public void destroy() {
    }
}
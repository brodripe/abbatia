package org.abbatia.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Filter which avoid processing several identical requests coming to the server one by one.
 * When subsequent identical request is detected, first response is sent.
 * <br/>
 * It prevents double clicks at server side.
 * <br/>
 * <br/>The diagram below shows how it works:
 * <br/>
 * <br/><img src="doc-files/ResponseCacheFilter-1.png"/>
 *
 * @author <a href="mailto:wojciech.ganczarski@fingo.pl">Wojtek Ganczarski - FINGO</a>
 */
public class ResponseCacheFilter implements Filter {
    private FilterConfig filterConfig = null;
    private Logger logger = Logger.getLogger(this.getClass());

    private long responseCacheReadTimeOut = 20000L; // 20 seconds
    private String contentType = "text/html; charset=UTF-8";
    private String characterEncoding = "UTF-8";

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

        if (this.filterConfig.getInitParameter("responseCacheReadTimeOut") != null) {
            this.responseCacheReadTimeOut = Long.parseLong(this.filterConfig.getInitParameter("responseCacheReadTimeOut"));
        }
        if (this.filterConfig.getInitParameter("contentType") != null) {
            this.contentType = this.filterConfig.getInitParameter("contentType");
        }
        if (this.filterConfig.getInitParameter("characterEncoding") != null) {
            this.characterEncoding = this.filterConfig.getInitParameter("characterEncoding");
        }

        ResponseCache.setReadTimeOut(this.responseCacheReadTimeOut);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Always set the content type before getting the PrintWriter

/*
        response.setContentType(this.contentType);
        response.setCharacterEncoding(this.characterEncoding);
*/


        PrintWriter out = response.getWriter();
        CharResponseWrapper wrapper = new CharResponseWrapper(response);

        ResponseCache cache = ResponseCache.getInstance(request.getSession().getId());
        if (!ServletAction.checkAndMarkActionExpired(request)) {
            this.logger.debug("ResponseCacheFilter: single request.");

            // clear cache immediately
            cache.write(null);
            response.setContentType(this.contentType);
            response.setCharacterEncoding(this.characterEncoding);

            // -- before doFilter -- request has been sent and response buffer is not yet prepared
            chain.doFilter(request, wrapper); // request is being processed (possible time consuming operation)
            // -- after doFilter -- response buffer is ready and we can read it using wrapper

            response.setContentType(this.contentType);
            response.setCharacterEncoding(this.characterEncoding);


            // writing wrapper output to the response cache
            cache.write(wrapper.toString());

            // writing wrapper output to the real response buffer
            out.write(wrapper.toString());
        } else
        // double click detected
        {
            //do not call chain.doFilter - this way request won't be proceeded

            this.logger.warn("ResponseCacheFilter: double click!");

            // writing previously saved cache content to the real response
            response.setContentType(this.contentType);
            response.setCharacterEncoding(this.characterEncoding);
            out.write(cache.read());
        }
/*
        response.setContentType(this.contentType);
        response.setCharacterEncoding(this.characterEncoding);
*/

        out.close();
    }

    public void destroy() {
        this.filterConfig = null;
    }
}

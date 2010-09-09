package org.abbatia.action.exception;

import org.apache.struts.action.*;
import org.apache.struts.config.ExceptionConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.abbatia.utils.Constantes;

import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * User: benjamin.rodriguez
 * Date: 16-ago-2007
 * Time: 9:51:56
 */
public class AbbatiaExceptionHandler extends ExceptionHandler
{

    public ActionForward execute(Exception exception, ExceptionConfig exceptionConfig, ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        if (exception != null)
        {
            StringWriter s = new StringWriter();
            exception.printStackTrace(new PrintWriter(s));
            String stackTraceString = s.toString();
            request.setAttribute(Constantes.REQUEST_PARAM_ERROR_STACKTRACE, stackTraceString);
            request.setAttribute(Constantes.REQUEST_PARAM_ERROR_MESSAGE, exception.getMessage());
        }
        return super.execute(exception, exceptionConfig, actionMapping, actionForm, request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }
}

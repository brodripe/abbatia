package org.abbatia.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * This class contains functions for helpfull while securing servlet actions
 * against repeated calls by refreshing browser.
 * <br/>
 * This class need javax.servlet library to work.
 *
 * @author <a href="mailto:robert@fingo.pl">FINGO - Robert Marek</a>
 */
public class ServletAction {
    final static String SESSION_TOKEN = "biz.fingo.action.token";

    /**
     * Calculate hash values from all request parameters.
     * WARNING: do not use with multipart request
     *
     * @param request HttpServletRequest object
     * @return hash value
     */
    static public int getRequestHashCode(HttpServletRequest request) {
        StringBuffer sb = request.getRequestURL();
        Enumeration keys = request.getParameterNames();
        String[] keysArray = new String[request.getParameterMap().size()];
        int i = 0;
        while (keys.hasMoreElements()) {
            keysArray[i++] = (String) keys.nextElement();
        }
        Arrays.sort(keysArray);
        for (i = 0; i < keysArray.length; i++) {
            String key = keysArray[i];
            String values[] = null;
            try {
                values = request.getParameterValues(key);
            }
            catch (Exception ex) {
                /* ignore, this is needed for file fields */
            }
            if (values == null || values.length == 0) {
                continue;
            }
            Arrays.sort(values);
            for (int j = 0; j < values.length; j++) {
                sb.append('&');
                sb.append(key);
                sb.append('=');
                sb.append(values[j]);
            }
        }
        String str = sb.toString();
        return str.hashCode();
    }

    /**
     * Check if an action have been already done.
     * This avoid multiple actions caused by browser refreshing
     * WARNING: do not use with multipart request
     *
     * @param request HttpServletRequest object
     * @return true if the same action have be last preformed, false otherwise
     */
    static public boolean isActionExpired(HttpServletRequest request) {
        int requestHashCode = getRequestHashCode(request);
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute(SESSION_TOKEN);
        if (sessionToken == null) {
            return false;
        }
        int sessionHashCode = 0;
        try {
            sessionHashCode = Integer.parseInt(sessionToken);
        }
        catch (Exception ex) {
            return false;
        }
        return requestHashCode == sessionHashCode;
    }

    /**
     * Mark action as done. This avoid multiple actions caused by browser refreshing
     *
     * @param request HttpServletRequest object
     */
    static public void markActionExpired(HttpServletRequest request) {
        request.getSession().setAttribute(SESSION_TOKEN, "" + getRequestHashCode(request));
    }

    /**
     * Mark action as not done. This avoid multiple actions caused by browser refreshing
     *
     * @param request HttpServletRequest object
     */
    static public void clearActionExpired(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_TOKEN);
    }

    /**
     * Check if an action have been already done and mark it if not.
     * This avoid multiple actions caused by browser refreshing
     *
     * @param request HttpServletRequest object
     * @return boolean true if action wasn't previously expired
     */
    static public synchronized boolean checkAndMarkActionExpired(HttpServletRequest request) {
        boolean expired = isActionExpired(request);
        if( !expired )
		{
			markActionExpired( request );
		}
		return expired;
	}
}

package org.abbatia.filter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * Transparent wrapper that gives access to the output response stream.
 *
 * @author <a href="mailto:wojciech.ganczarski@fingo.pl">Wojtek Ganczarski - FINGO</a>
 */
public class CharResponseWrapper extends HttpServletResponseWrapper {
    private CharArrayWriter output;

    /**
     * @param response
     */
    public CharResponseWrapper(HttpServletResponse response) {
        super(response);
        this.output = new CharArrayWriter();
    }

    @Override
    public String toString() {
        return this.output.toString();
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(this.output);
	}
}
package org.abbatia.filter;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Cache that stores response output buffer.
 * <br/>
 * Kind of Singleton pattern adopted to web environment - one instance for a session.
 *
 * @author <a href="mailto:wojciech.ganczarski@fingo.pl">Wojtek Ganczarski - FINGO</a>
 */
public class ResponseCache {
    // cache buffer
    private StringBuilder buffer;

    // indicates, if buffer is ready
    private boolean isReady = false;

    // pool of ResponseCache instances (one for http session)
    private static Hashtable<String, ResponseCache> pool = new Hashtable<String, ResponseCache>();

    private static Logger logger = Logger.getLogger(ResponseCache.class);

    private static long readTimeOut = 20000; // 20 ms

    /**
     * Private constructor denies creating this object from the outside.
     *
     * @throws IOException
     */
    private ResponseCache() throws IOException {
    }

    /**
     * Creates / gets one ResponseCache instance for http session.<br/>
     *
     * @param sessionId
     * @return ResponseCache
     * @throws IOException
     */
    public static synchronized ResponseCache getInstance(String sessionId) throws IOException {
        if (pool.get(sessionId) == null) {
            pool.put(sessionId, new ResponseCache());
        }
        return pool.get(sessionId);
    }

    /**
     * Writes response output to the buffer.
     *
     * @param buffer
     * @throws IOException
     */
    public synchronized void write(String buffer) throws IOException {
        this.buffer = new StringBuilder();

        if (buffer != null) {
            this.buffer.append(buffer);
            this.isReady = true;
        } else {
            // Buffor is empty and not ready.
            // Called, when we want to clear previous content.
            this.isReady = false;
        }
        this.notifyAll();
    }

    /**
     * Reads response output from the buffer.
     *
     * @return String
     * @throws IOException
     */
    public synchronized String read() throws IOException {
        // wait, if buffer is not ready (when is empty)
        if (!this.isReady) {
            try {
                this.wait(ResponseCache.readTimeOut);
            }
            catch (InterruptedException exception) {
            }

            if (!this.isReady) {
                throw new RuntimeException("Buffer will never be ready");
            }
        }
        return this.buffer.toString();
    }

    /**
     * Removes instance from pool.<br/>
     * Method should be called from e.g. HttpSessionListener.sessionDestroyed
     * when session is no longer available and instance of ResponseCache can be removed from pool.
     *
     * @param sessionId
     */
    public static void removeInstanceFromPool(String sessionId) {
        pool.remove(sessionId);
        logger.info("response cache instance removed: " + sessionId);
    }

    /**
     * Sets timeout for reading the buffer when it's locked.
     *
     * @param readTimeOut in milliseconds
     */
    public static void setReadTimeOut( long readTimeOut )
	{
		ResponseCache.readTimeOut = readTimeOut;
	}
}
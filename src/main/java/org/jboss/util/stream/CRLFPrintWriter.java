/*
  * JBoss, Home of Professional Open Source
  * Copyright 2005, JBoss Inc., and individual contributors as indicated
  * by the @authors tag. See the copyright.txt in the distribution for a
  * full listing of individual contributors.
  *
  * This is free software; you can redistribute it and/or modify it
  * under the terms of the GNU Lesser General Public License as
  * published by the Free Software Foundation; either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  * Lesser General Public License for more details.
  *
  * You should have received a copy of the GNU Lesser General Public
  * License along with this software; if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
  */
package org.jboss.util.stream;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.InterruptedIOException;
import java.io.Writer;

/**
 * A <tt>PrintWriter</tt> that ends lines with a carriage return-line feed 
 * (<tt>CRLF</tt>).
 *
 * <h3>Concurrency</h3>
 * This class is <b>as</b> synchronized as <tt>PrintWriter</tt>.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class CRLFPrintWriter
   extends PrintWriter
{
   protected boolean autoFlush = false;

   public CRLFPrintWriter(final Writer out) {
      super(out);
   }

   public CRLFPrintWriter(final Writer out, final boolean autoFlush) {
      super(out, autoFlush);
      this.autoFlush = autoFlush;
   }

   public CRLFPrintWriter(final OutputStream out) {
      super(out);
   }

   public CRLFPrintWriter(final OutputStream out, final boolean autoFlush) {
      super(out, autoFlush);
      this.autoFlush = autoFlush;
   }

   protected void ensureOpen() throws IOException {
      if (out == null)
         throw new IOException("Stream closed");
   }

   public void println() {
      try {
         synchronized (lock) {
            ensureOpen();

            out.write("\r\n");

            if (autoFlush) {
               out.flush();
            }
         }
      }
      catch (InterruptedIOException e) {
         Thread.currentThread().interrupt();
      }
      catch (IOException e) {
         setError();
      }
   }      
}

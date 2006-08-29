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
package org.jboss.net.sockets;

import java.io.InputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

/** An InputStream that uses the SocketTimeoutException thrown during read
 * timeouts to check if the thread has been interrupted.
 *  
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class InterruptableInputStream extends InputStream
{
   private InputStream is;

   public InterruptableInputStream(InputStream is)
   {
      this.is = is;
   }

   public int read() throws IOException
   {
      byte[] b = {};
      int count = internalRead(b, 0, 1);
      return count > 0 ? b[0] : -1;
   }

   public int read(byte[] b) throws IOException
   {
      return internalRead(b, 0, b.length);
   }

   public int read(byte[] b, int off, int len) throws IOException
   {
      return internalRead(b, off, len);
   }

   public long skip(long n) throws IOException
   {
      return is.skip(n);
   }

   public int available() throws IOException
   {
      return is.available();
   }

   public void close() throws IOException
   {
      is.close();
   }

   public synchronized void mark(int readlimit)
   {
      is.mark(readlimit);
   }

   public synchronized void reset() throws IOException
   {
      is.reset();
   }

   public boolean markSupported()
   {
      return is.markSupported();
   }

   private int internalRead(byte[] b, int off, int len) throws IOException
   {
      int n = -1;
      while( true )
      {
         try
         {
            n = is.read(b, off, len);
            return n;
         }
         catch(SocketTimeoutException e)
         {
            // Test for thread interrupt
            if( Thread.interrupted() )
               throw e;
         }
      }
   }
}

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

import java.io.IOException;
import java.rmi.server.RMIClientSocketFactory;
import java.net.Socket;
import EDU.oswego.cs.dl.util.concurrent.FIFOSemaphore;
/**
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class QueuedClientSocketFactory
   implements RMIClientSocketFactory, java.io.Externalizable
{
   private transient FIFOSemaphore permits;
   private long numPermits;
   public QueuedClientSocketFactory()
   {
   }

   public QueuedClientSocketFactory(long nPermits)
   {
      permits = new FIFOSemaphore(nPermits);
      numPermits = nPermits;
   }
   /**
    * Create a server socket on the specified port (port 0 indicates
    * an anonymous port).
    * @param  port the port number
    * @return the server socket on the specified port
    * @exception IOException if an I/O error occurs during server socket
    * creation
    * @since 1.2
    */
   public Socket createSocket(String host, int port) throws IOException
   {
      try
      {
         permits.acquire();
         return new Socket(host, port);
      }
      catch (InterruptedException ex)
      {
         throw new IOException("Failed to acquire FIFOSemaphore for ClientSocketFactory");
      }
      finally
      {
         permits.release();
      }
   }
   
   public boolean equals(Object obj)
   {
      return obj instanceof QueuedClientSocketFactory;
   }
   public int hashCode()
   {
      return getClass().getName().hashCode();
   }
   
   public void writeExternal(java.io.ObjectOutput out)
      throws IOException
   {
      out.writeLong(numPermits);
   }
   public void readExternal(java.io.ObjectInput in)
      throws IOException, ClassNotFoundException
   {
      numPermits = in.readLong();
      permits = new FIFOSemaphore(numPermits);
   }
}

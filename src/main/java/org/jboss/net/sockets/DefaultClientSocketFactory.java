/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
import java.io.Serializable;
import java.rmi.server.RMIClientSocketFactory;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A RMIClientSocketFactory that adds a bind address override of the server
 * host to control what the address the client uses.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 30203 $
 */
public class DefaultClientSocketFactory
   implements RMIClientSocketFactory, Serializable
{
   private static final long serialVersionUID = -920483051658660269L;
   /** An override of the server address */
   private InetAddress bindAddress;

   public DefaultClientSocketFactory()
   {
   }

   public String getBindAddress()
   {
      String address = null;
      if( bindAddress != null )
         address = bindAddress.getHostAddress();
      return address;
   }
   public void setBindAddress(String host) throws UnknownHostException
   {
      bindAddress = InetAddress.getByName(host);
   }

   /**
    * Create a server socket on the specified port (port 0 indicates
    * an anonymous port).
    * @param  port the port number
    * @return the server socket on the specified port
    * @exception java.io.IOException if an I/O error occurs during server socket
    * creation
    * @since 1.2
    */
   public Socket createSocket(String host, int port) throws IOException
   {
      InetAddress addr = null;
      if( bindAddress != null )
         addr = bindAddress;
      else
         addr = InetAddress.getByName(host);
      Socket s = new Socket(addr, port);
      return s;
   }

   public boolean equals(Object obj)
   {
      boolean equals = obj instanceof DefaultClientSocketFactory;
      if( equals && bindAddress != null )
      {
         DefaultClientSocketFactory dcsf = (DefaultClientSocketFactory) obj;
         InetAddress dcsfa = dcsf.bindAddress;
         if( dcsfa != null )
            equals = bindAddress.equals(dcsfa);
         else
            equals = false;
      }
      return equals;
   }
   public int hashCode()
   {
      int hashCode = getClass().getName().hashCode();
      if( bindAddress != null )
         hashCode += bindAddress.toString().hashCode();
      return hashCode;
   }
   public String toString()
   {
      StringBuffer tmp = new StringBuffer(super.toString());
      tmp.append('[');
      tmp.append("bindAddress=");
      tmp.append(bindAddress);
      tmp.append(']');
      return tmp.toString();
   }
}

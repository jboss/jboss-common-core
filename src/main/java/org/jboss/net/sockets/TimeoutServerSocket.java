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

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;

/** A ServerSocket that returns a TimeoutSocket from the overriden accept.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class TimeoutServerSocket extends ServerSocket
{
   public TimeoutServerSocket(int port)
      throws IOException
   {
      this(port, 50);
   }
   public TimeoutServerSocket(int port, int backlog)
      throws IOException
   {
      this(port, backlog, null);
   }
   public TimeoutServerSocket(int port, int backlog, InetAddress bindAddr)
      throws IOException
   {
      super(port, backlog, bindAddr);
   }

   public Socket accept() throws IOException
   {
      Socket s = super.accept();
      s.setSoTimeout(1000);
      TimeoutSocket ts = new TimeoutSocket(s);
      return ts;
   }
}

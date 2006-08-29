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

import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;

/** A Socket that overrides the getInputStream to return a InterruptableInputStream
 *  
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class TimeoutSocket extends Socket
{
   private Socket s;

   public TimeoutSocket(Socket s)
   {
      this.s = s;
   }

   public InetAddress getInetAddress()
   {
      return s.getInetAddress();
   }

   public InetAddress getLocalAddress()
   {
      return s.getLocalAddress();
   }

   public int getPort()
   {
      return s.getPort();
   }

   public int getLocalPort()
   {
      return s.getLocalPort();
   }

   public SocketAddress getRemoteSocketAddress()
   {
      return s.getRemoteSocketAddress();
   }

   public SocketAddress getLocalSocketAddress()
   {
      return s.getLocalSocketAddress();
   }

   public SocketChannel getChannel()
   {
      return s.getChannel();
   }

   public InputStream getInputStream() throws IOException
   {
      InputStream is = s.getInputStream();
      InterruptableInputStream iis = new InterruptableInputStream(is);
      return iis;
   }

   public OutputStream getOutputStream() throws IOException
   {
      return s.getOutputStream();
   }

   public void setTcpNoDelay(boolean on) throws SocketException
   {
      s.setTcpNoDelay(on);
   }

   public boolean getTcpNoDelay() throws SocketException
   {
      return s.getTcpNoDelay();
   }

   public void setSoLinger(boolean on, int linger) throws SocketException
   {
      s.setSoLinger(on, linger);
   }

   public int getSoLinger() throws SocketException
   {
      return s.getSoLinger();
   }

   public void sendUrgentData(int data) throws IOException
   {
      s.sendUrgentData(data);
   }

   public void setOOBInline(boolean on) throws SocketException
   {
      s.setOOBInline(on);
   }

   public boolean getOOBInline() throws SocketException
   {
      return s.getOOBInline();
   }

   public synchronized void setSoTimeout(int timeout) throws SocketException
   {
      s.setSoTimeout(1000);
   }

   public synchronized int getSoTimeout() throws SocketException
   {
      return s.getSoTimeout();
   }

   public synchronized void setSendBufferSize(int size) throws SocketException
   {
      s.setSendBufferSize(size);
   }

   public synchronized int getSendBufferSize() throws SocketException
   {
      return s.getSendBufferSize();
   }

   public synchronized void setReceiveBufferSize(int size) throws SocketException
   {
      s.setReceiveBufferSize(size);
   }

   public synchronized int getReceiveBufferSize() throws SocketException
   {
      return s.getReceiveBufferSize();
   }

   public void setKeepAlive(boolean on) throws SocketException
   {
      s.setKeepAlive(on);
   }

   public boolean getKeepAlive() throws SocketException
   {
      return s.getKeepAlive();
   }

   public void setTrafficClass(int tc) throws SocketException
   {
      s.setTrafficClass(tc);
   }

   public int getTrafficClass() throws SocketException
   {
      return s.getTrafficClass();
   }

   public void setReuseAddress(boolean on) throws SocketException
   {
      s.setReuseAddress(on);
   }

   public boolean getReuseAddress() throws SocketException
   {
      return s.getReuseAddress();
   }

   public synchronized void close() throws IOException
   {
      s.close();
   }

   public void shutdownInput() throws IOException
   {
      s.shutdownInput();
   }

   public void shutdownOutput() throws IOException
   {
      s.shutdownOutput();
   }

   public String toString()
   {
      return s.toString();
   }

   public boolean isConnected()
   {
      return s.isConnected();
   }

   public boolean isBound()
   {
      return s.isBound();
   }

   public boolean isClosed()
   {
      return s.isClosed();
   }

   public boolean isInputShutdown()
   {
      return s.isInputShutdown();
   }

   public boolean isOutputShutdown()
   {
      return s.isOutputShutdown();
   }
}

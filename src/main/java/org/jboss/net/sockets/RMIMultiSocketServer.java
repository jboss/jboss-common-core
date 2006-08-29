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

import java.rmi.server.UnicastRemoteObject;
import java.rmi.Remote;
import java.lang.reflect.Proxy;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.RemoteException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.rmi.NoSuchObjectException;
/**
 *
 * @author bill@jboss.org
 * @version $Revision$
 */
public class RMIMultiSocketServer
{
   private static HashMap handlermap = new HashMap();
   private static HashMap stubmap = new HashMap();

   public static Remote exportObject(Remote obj,
                                     int port,
                                     RMIClientSocketFactory csf, 
                                     RMIServerSocketFactory ssf,
                                     Class[] interfaces,
                                     int numSockets)
      throws RemoteException
   {
      Remote[] stubs = new Remote[numSockets];

      Method[] methods = obj.getClass().getMethods();
      
      HashMap invokerMap = new HashMap();
      for (int i = 0; i < methods.length; i++) {
         Long methodkey = new Long(MethodHash.calculateHash(methods[i]));
         invokerMap.put(methodkey, methods[i]);
      }
      
      RMIMultiSocketHandler[] handlers = new RMIMultiSocketHandler[numSockets];
      for (int i = 0; i < numSockets; i++)
      {
         int theport = (port == 0) ? 0 : port + i;
         handlers[i] = new RMIMultiSocketHandler(obj, invokerMap);
         stubs[i] = UnicastRemoteObject.exportObject(handlers[i], theport, csf, ssf);
      }

      Remote remote = (Remote)Proxy.newProxyInstance(
         obj.getClass().getClassLoader(),
         interfaces,
         new RMIMultiSocketClient(stubs));
      stubmap.put(remote, stubs);
      handlermap.put(remote, handlers);
      return remote;
   }

   public static Remote exportObject(Remote obj,
                                     int port,
                                     RMIClientSocketFactory csf, 
                                     RMIServerSocketFactory ssf,
                                     int numSockets)
      throws RemoteException
   {
      return exportObject(obj, port, csf, ssf, obj.getClass().getInterfaces(), numSockets);
   }

   public static boolean unexportObject(Remote obj, boolean force)
      throws NoSuchObjectException
   {
      handlermap.remove(obj);
      Remote[] stubs = (Remote[])stubmap.remove(obj);
      for (int i = 0; i < stubs.length; i++)
      {
         UnicastRemoteObject.unexportObject(stubs[i], force);
      }
      
      return true;
   }
}

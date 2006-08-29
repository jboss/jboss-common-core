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

   
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;
import java.io.Serializable;
import java.util.Random;
import java.rmi.Remote;

/**
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class RMIMultiSocketClient implements InvocationHandler, Serializable
{
   protected Remote[] stubs;
   protected Random random;
   public RMIMultiSocketClient(Remote[] stubs)
   {
      this.stubs = stubs;
      random = new Random();
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      if (method.getName().equals("hashCode"))
      {
         return new Integer(stubs[0].hashCode());
      }
      if (method.getName().equals("equals"))
      {
         return new Boolean(stubs[0].equals(args[0]));
      }
      int i = random.nextInt(stubs.length);
      long hash = MethodHash.calculateHash(method);
      RMIMultiSocket target = (RMIMultiSocket)stubs[i];
      return target.invoke(hash, args);
   }   
}

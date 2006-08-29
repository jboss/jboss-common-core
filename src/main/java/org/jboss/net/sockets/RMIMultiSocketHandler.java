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
import java.util.Map;
/**
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class RMIMultiSocketHandler implements RMIMultiSocket
{
   Object target;
   Map invokerMap;
   public RMIMultiSocketHandler(Object target, Map invokerMap)
   {
      this.target = target;
      this.invokerMap = invokerMap;
   }

   public Object invoke (long methodHash, Object[] args) throws Exception
   {
      Method method = (Method)invokerMap.get(new Long(methodHash));
      return method.invoke(target, args);
   }
}

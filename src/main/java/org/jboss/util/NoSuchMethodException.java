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
package org.jboss.util;

import java.lang.reflect.Method;

/**
 * A better NoSuchMethodException which can take a Method object
 * and formats the detail message based on in.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class NoSuchMethodException
   extends java.lang.NoSuchMethodException
{
   /**
    * Construct a <tt>NoSuchMethodException</tt> with the specified detail 
    * message.
    *
    * @param msg  Detail message.
    */
   public NoSuchMethodException(String msg) {
      super(msg);
   }

   /**
    * Construct a <tt>NoSuchMethodException</tt> using the given method
    * object to construct the detail message.
    *
    * @param method  Method to determine detail message from.
    */
   public NoSuchMethodException(Method method) {
      super(format(method));
   }

   /**
    * Construct a <tt>NoSuchMethodException</tt> using the given method
    * object to construct the detail message.
    *
    * @param msg     Detail message prefix.
    * @param method  Method to determine detail message suffix from.
    */
   public NoSuchMethodException(String msg, Method method) {
      super(msg + format(method));
   }
   
   /**
    * Construct a <tt>NoSuchMethodException</tt> with no detail.
    */
   public NoSuchMethodException() {
      super();
   }

   /**
    * Return a string representation of the given method object.
    */
   public static String format(Method method)
   {
      StringBuffer buffer = new StringBuffer();
      buffer.append(method.getName()).append("(");
      Class[] paramTypes = method.getParameterTypes();
      for (int count = 0; count < paramTypes.length; count++) {
         if (count > 0) {
            buffer.append(",");
         }
         buffer.
            append(paramTypes[count].getName().substring(paramTypes[count].getName().lastIndexOf(".")+1));
      }
      buffer.append(")");

      return buffer.toString();
   }
}

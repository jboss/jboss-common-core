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
package org.jboss.util.loading;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * A helper for context classloading.<p>
 *
 * When a security manager is installed, the
 * constructor checks for the runtime permissions
 * &quot;getClassLoader&quot;
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 */
public class ContextClassLoader
{
   /**
    * Retrieve a classloader permission
    */
   public static final RuntimePermission GETCLASSLOADER = new RuntimePermission("getClassLoader");

   /**
    * Instantiate a new context class loader
    */
   public static final NewInstance INSTANTIATOR = new NewInstance();

   /**
    * Constructor.
    * 
    * @throws SecurityException when not authroized to get the context classloader
    */
   /*package*/ ContextClassLoader()
   {
      SecurityManager manager = System.getSecurityManager();
      if (manager != null)
      {
         manager.checkPermission(GETCLASSLOADER);
      }
   }

   /**
    * Retrieve the context classloader
    *
    * @return the context classloader
    */
   public ClassLoader getContextClassLoader()
   {
      return getContextClassLoader(Thread.currentThread());
   }

   /**
    * Retrieve the context classloader for the given thread
    *
    * @param thread the thread
    * @return the context classloader
    */
   public ClassLoader getContextClassLoader(final Thread thread)
   {
      return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction()
      {
         public Object run()
         {
            return thread.getContextClassLoader();
         }
      });
   }

   private static class NewInstance
      implements PrivilegedAction
   {
      public Object run()
      {
         return new ContextClassLoader();
      }
   }
}

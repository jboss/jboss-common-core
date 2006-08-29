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

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * A URL classloader that delegates to its parent, avoiding
 * synchronization.
 *
 * A standard flag is provided so it can be used as a parent class,
 * but later subclassed and to revert to standard class loading
 * if the subclass wants to load classes.
 *
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @version $Revision$
 */
public class DelegatingClassLoader
   extends URLClassLoader
{
   /** The value returned by {@link getURLs}. */
   public static final URL[] EMPTY_URL_ARRAY = {};

   /** Whether to use standard loading */
   protected boolean standard = false;

   /**
    * Constructor
    *
    * @param parent the parent classloader, cannot be null.
    */
   public DelegatingClassLoader(ClassLoader parent)
   {
      super(EMPTY_URL_ARRAY, parent);
      if (parent == null)
         throw new IllegalArgumentException("No parent");
   }

   /**
    * Constructor
    *
    * @param parent, the parent classloader, cannot be null.
    * @param factory the url stream factory.
    */
   public DelegatingClassLoader(ClassLoader parent, URLStreamHandlerFactory factory)
   {
      super(EMPTY_URL_ARRAY, parent, factory);
      if (parent == null)
         throw new IllegalArgumentException("No parent");
   }

   /**
    * Load a class, by asking the parent
    *
    * @param className the class name to load
    * @param resolve whether to link the class
    * @return the loaded class
    * @throws ClassNotFoundException when the class could not be found
    */
   protected Class loadClass(String className, boolean resolve)
      throws ClassNotFoundException
   {
      // Revert to standard rules
      if (standard)
         return super.loadClass(className, resolve);

      // Ask the parent
      Class clazz = getParent().loadClass(className);

      // Link the class
      if (resolve)
         resolveClass(clazz);

      return clazz;
   }
}

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
package org.jboss.util.collection;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A weak class cache that instantiates does not a hold a
 * strong reference to either the classloader or class.<p>
 * 
 * It creates the class specific data in two stages
 * to avoid recursion.<p>
 * 
 * instantiate - creates the data<br>
 * generate - fills in the details
 *
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 */
public abstract class WeakClassCache
{
   /** The cache */
   protected Map cache = new WeakHashMap(); 

   /**
    * Get the information for a class
    * 
    * @param clazz the class
    * @return the info
    */
   public Object get(Class clazz)
   {
      if (clazz == null)
         throw new IllegalArgumentException("Null class");
      
      Map classLoaderCache = getClassLoaderCache(clazz.getClassLoader());

      WeakReference weak = (WeakReference) classLoaderCache.get(clazz.getName());
      if (weak != null)
      {
         Object result = weak.get();
         if (result != null)
            return result;
      }

      Object result = instantiate(clazz);

      weak = new WeakReference(result);
      classLoaderCache.put(clazz.getName(), weak);
      
      generate(clazz, result);
      
      return result;
   }
   
   /**
    * Get the information for a class
    * 
    * @param name the name
    * @param cl the classloader
    * @return the info
    * @throws ClassNotFoundException when the class cannot be found
    */
   public Object get(String name, ClassLoader cl) throws ClassNotFoundException
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (cl == null)
         throw new IllegalArgumentException("Null classloader");
      Class clazz = cl.loadClass(name);
      return get(clazz);
   }
   
   /**
    * Instantiate for a class
    * 
    * @param clazz the class
    * @return the result
    */
   protected abstract Object instantiate(Class clazz);
   
   /**
    * Fill in the result
    * 
    * @param clazz the class
    * @param result the result
    */
   protected abstract void generate(Class clazz, Object result);
   
   /**
    * Get the cache for the classloader
    * 
    * @param cl the classloader
    * @return the map
    */
   protected Map getClassLoaderCache(ClassLoader cl)
   {
      synchronized (cache)
      {
         Map result = (Map) cache.get(cl);
         if (result == null)
         {
            result = CollectionsFactory.createConcurrentReaderMap();
            cache.put(cl, result);
         }
         return result;
      }
   }
}

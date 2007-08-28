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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.util.collection.LazyList;
import org.jboss.util.collection.LazyMap;
import org.jboss.util.collection.LazySet;

/**
 * Collections factory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class CollectionsFactory
{
   /**
    * Defines the map implementation
    * 
    * @param <K> the key type
    * @param <V> the value type
    * @return the map
    */
   public static final <K, V> Map<K, V> createLazyMap()
   {
      return new LazyMap<K, V>();
   }

   /**
    * Defines the list implementation
    * 
    * @param <T> the type
    * @return the list
    */
   public static final <T> List<T> createLazyList()
   {
      return new LazyList<T>();
   }

   /**
    * Defines the set implementation
    * 
    * @param <T> the type
    * @return the set
    */
   public static final <T> Set<T> createLazySet()
   {
      return new LazySet<T>();
   }

   /**
    * Defines the concurrent map implementation
    * 
    * @param <K> the key type
    * @param <V> the value type
    * @return the map
    */
   public static final <K, V> Map<K, V> createConcurrentReaderMap()
   {
      return new ConcurrentHashMap<K, V>();
   }

   /**
    * Defines the concurrent list implementation
    * 
    * @param <T> the type
    * @return the list
    */
   public static final <T> List<T> createCopyOnWriteList()
   {
      return new CopyOnWriteArrayList<T>();
   }

   /**
    * Defines the concurrent set implementation
    * 
    * @param <T> the type
    * @return the set
    */
   public static final <T> Set<T> createCopyOnWriteSet()
   {
      return new CopyOnWriteArraySet<T>();
   }
}

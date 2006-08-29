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

import org.jboss.util.collection.LazyList;
import org.jboss.util.collection.LazyMap;
import org.jboss.util.collection.LazySet;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArraySet;

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
    */
   public static final Map createLazyMap()
   {
      return new LazyMap();
   }

   /**
    * Defines the list implementation
    */
   public static final List createLazyList()
   {
      return new LazyList();
   }

   /**
    * Defines the set implementation
    */
   public static final Set createLazySet()
   {
      return new LazySet();
   }

   /**
    * Defines the concurrent map implementation
    */
   public static final Map createConcurrentReaderMap()
   {
      return new ConcurrentReaderHashMap();
   }

   /**
    * Defines the concurrent list implementation
    */
   public static final List createCopyOnWriteList()
   {
      return new CopyOnWriteArrayList();
   }

   /**
    * Defines the concurrent set implementation
    */
   public static final Set createCopyOnWriteSet()
   {
      return new CopyOnWriteArraySet();
   }
}

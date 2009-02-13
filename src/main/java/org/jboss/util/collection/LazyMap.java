/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

/**
 * LazyMap.
 * It's serializable if the elements are serializable.
 * 
 * @param <K> the key type
 * @param <V> the value type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class LazyMap<K, V> implements Map<K, V>, Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   /** The delegate map */
   private Map<K, V> delegate = Collections.emptyMap();

   /**
    * Create the map implementation
    * 
    * @return the map
    */
   private Map<K, V> createImplementation()
   {
      if (delegate instanceof HashMap == false)
         return new HashMap<K, V>(delegate);
      return delegate;
   }

   public void clear()
   {
      delegate = Collections.emptyMap();
   }

   public boolean containsKey(Object key)
   {
      return delegate.containsKey(key);
   }

   public boolean containsValue(Object value)
   {
      return delegate.containsValue(value);
   }

   public Set<Entry<K, V>> entrySet()
   {
      return delegate.entrySet();
   }

   public V get(Object key)
   {
      return delegate.get(key);
   }

   public boolean isEmpty()
   {
      return delegate.isEmpty();
   }

   public Set<K> keySet()
   {
      return delegate.keySet();
   }

   public V put(K key, V value)
   {
      if (delegate.isEmpty())
      {
         delegate = Collections.singletonMap(key, value);
         return null;
      }
      else
      {
         delegate = createImplementation();
         return delegate.put(key, value);
      }
   }

   public void putAll(Map<? extends K, ? extends V> t)
   {
      delegate = createImplementation();
      delegate.putAll(t);
   }

   public V remove(Object key)
   {
      delegate = createImplementation();
      return delegate.remove(key);
   }

   public int size()
   {
      return delegate.size();
   }

   public Collection<V> values()
   {
      return delegate.values();
   }

   @Override
   public String toString()
   {
      return delegate.toString();
   }
}

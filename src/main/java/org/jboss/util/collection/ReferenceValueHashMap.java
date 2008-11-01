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

import java.lang.ref.ReferenceQueue;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * This Map will remove entries when the value in the map has been
 * cleaned from garbage collection
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author  <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author  <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @author  <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class ReferenceValueHashMap<K, V> extends AbstractMap<K, V>
{
   /** Hash table mapping keys to ref values */
   private Map<K, ValueRef<K, V>> hash;

   /** Reference queue for cleared RefKeys */
   private ReferenceQueue<V> queue = new ReferenceQueue<V>();

   protected ReferenceValueHashMap(int initialCapacity, float loadFactor)
   {
      hash = new HashMap<K, ValueRef<K, V>>(initialCapacity, loadFactor);
   }

   protected ReferenceValueHashMap(int initialCapacity)
   {
      hash = new HashMap<K, ValueRef<K, V>>(initialCapacity);
   }

   protected ReferenceValueHashMap()
   {
      hash = new HashMap<K, ValueRef<K, V>>();
   }

   protected ReferenceValueHashMap(Map<K, V> t)
   {
      this(Math.max(2*t.size(), 11), 0.75f);
      putAll(t);
   }

   @Override
   public int size()
   {
      processQueue();
      return hash.size();
   }

   @Override
   public boolean containsKey(Object key)
   {
      processQueue();
      return hash.containsKey(key);
   }

   @Override
   public V get(Object key)
   {
      processQueue();
      ValueRef<K, V> ref = hash.get(key);
      if (ref != null)
         return ref.get();
      return null;
   }

   @Override
   public V put(K key, V value)
   {
      processQueue();
      ValueRef<K, V> ref = create(key, value, queue);
      ValueRef<K, V> result = hash.put(key, ref);
      if (result != null)
         return result.get();
      return null;
   }

   @Override
   public V remove(Object key)
   {
      processQueue();
      ValueRef<K, V> result = hash.remove(key);
      if (result != null)
         return result.get();
      return null;
   }

   @Override
   public Set<Entry<K,V>> entrySet()
   {
      processQueue();
      return new EntrySet();
   }

   @Override
   public void clear()
   {
      processQueue();
      hash.clear();
   }

   /**
    * Remove all entries whose values have been discarded.
    */
   @SuppressWarnings("unchecked")
   private void processQueue()
   {
      ValueRef<K, V> ref = (ValueRef<K, V>) queue.poll();
      while (ref != null)
      {
         // only remove if it is the *exact* same WeakValueRef
         if (ref == hash.get(ref.getKey()))
            hash.remove(ref.getKey());

         ref = (ValueRef<K, V>) queue.poll();
      }
   }

   /**
    * EntrySet.
    */
   private class EntrySet extends AbstractSet<Entry<K, V>>
   {
      @Override
      public Iterator<Entry<K, V>> iterator()
      {
         return new EntrySetIterator(hash.entrySet().iterator());
      }

      @Override
      public int size()
      {
         return ReferenceValueHashMap.this.size();
      }
   }

   /**
    * EntrySet iterator
    */
   private class EntrySetIterator implements Iterator<Entry<K, V>>
   {
      /** The delegate */
      private Iterator<Entry<K, ValueRef<K, V>>> delegate;

      /**
       * Create a new EntrySetIterator.
       *
       * @param delegate the delegate
       */
      public EntrySetIterator(Iterator<Entry<K, ValueRef<K, V>>> delegate)
      {
         this.delegate = delegate;
      }

      public boolean hasNext()
      {
         return delegate.hasNext();
      }

      public Entry<K, V> next()
      {
         Entry<K, ValueRef<K, V>> next = delegate.next();
         return next.getValue();
      }

      public void remove()
      {
         throw new UnsupportedOperationException("remove");
      }
   }

   /**
    * Create new value ref instance.
    *
    * @param key the key
    * @param value the value
    * @param q the ref queue
    * @return new value ref instance
    */
   protected abstract ValueRef<K, V> create(K key, V value, ReferenceQueue<V> q);
}
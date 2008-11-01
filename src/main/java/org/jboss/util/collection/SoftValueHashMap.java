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
import java.lang.ref.SoftReference;
import java.util.Map;

/**
 * This Map will remove entries when the value in the map has been
 * cleaned from garbage collection
 *
 * @author  <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author  <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SoftValueHashMap<K, V> extends ReferenceValueHashMap<K, V>
{
   /**
    * Constructs a new, empty <code>SoftValueHashMap</code> with the given
    * initial capacity and the given load factor.
    *
    * @param  initialCapacity  The initial capacity of the
    *                          <code>SoftValueHashMap</code>
    *
    * @param  loadFactor       The load factor of the <code>SoftValueHashMap</code>
    *
    * @throws IllegalArgumentException  If the initial capacity is less than
    *                                   zero, or if the load factor is
    *                                   nonpositive
    */
   public SoftValueHashMap(int initialCapacity, float loadFactor)
   {
      super(initialCapacity, loadFactor);
   }

   /**
    * Constructs a new, empty <code>SoftValueHashMap</code> with the given
    * initial capacity and the default load factor, which is
    * <code>0.75</code>.
    *
    * @param  initialCapacity  The initial capacity of the
    *                          <code>SoftValueHashMap</code>
    *
    * @throws IllegalArgumentException  If the initial capacity is less than
    *                                   zero
    */
   public SoftValueHashMap(int initialCapacity)
   {
      super(initialCapacity);
   }

   /**
    * Constructs a new, empty <code>SoftValueHashMap</code> with the default
    * initial capacity and the default load factor, which is
    * <code>0.75</code>.
    */
   public SoftValueHashMap()
   {
   }

   /**
    * Constructs a new <code>SoftValueHashMap</code> with the same mappings as the
    * specified <tt>Map</tt>.  The <code>SoftValueHashMap</code> is created with an
    * initial capacity of twice the number of mappings in the specified map
    * or 11 (whichever is greater), and a default load factor, which is
    * <tt>0.75</tt>.
    *
    * @param   t the map whose mappings are to be placed in this map.
    * @since    1.3
    */
   public SoftValueHashMap(Map<K, V> t)
   {
      super(t);
   }

   protected ValueRef<K, V> create(K key, V value, ReferenceQueue<V> q)
   {
      return SoftValueRef.create(key, value, q);
   }

   /**
    * Soft value ref impl
    */
   private static class SoftValueRef<K, V> extends SoftReference<V> implements ValueRef<K, V>
   {
      /** The key */
      public K key;

      /**
       * Safely create a new WeakValueRef
       *
       * @param <K> the key type
       * @param <V> the value type
       * @param key the key
       * @param val the value
       * @param q the reference queue
       * @return the reference or null if the value is null
       */
      private static <K, V> SoftValueRef<K, V> create(K key, V val, ReferenceQueue<V> q)
      {
         if (val == null)
            return null;
         else
            return new SoftValueRef<K, V>(key, val, q);
      }

      /**
       * Create a new WeakValueRef.
       *
       * @param key the key
       * @param val the value
       * @param q the reference queue
       */
      private SoftValueRef(K key, V val, ReferenceQueue<V> q)
      {
         super(val, q);
         this.key = key;
      }

      public K getKey()
      {
         return key;
      }

      public V getValue()
      {
         return get();
      }

      public V setValue(V value)
      {
         throw new UnsupportedOperationException("setValue");
      }
   }
}

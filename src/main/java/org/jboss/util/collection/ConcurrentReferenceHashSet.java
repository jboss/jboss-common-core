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

import java.util.EnumSet;
import java.util.Set;

/**
 * Set based on top of ConcurrentReferenceHashMap.
 * It's serializable if the elements are serializable.
 *
 * @author <a href="ales.justin@jboss.org">Ales Justin</a>
 * @param <E> the element type
 */
public class ConcurrentReferenceHashSet<E> extends MapDelegateSet<E>
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = 1L;

   /**
    * Creates a new, empty set with the specified initial
    * capacity, load factor and concurrency level.
    *
    * @param initialCapacity  the initial capacity. The implementation
    *                         performs internal sizing to accommodate this many elements.
    * @param loadFactor       the load factor threshold, used to control resizing.
    *                         Resizing may be performed when the average number of elements per
    *                         bin exceeds this threshold.
    * @param concurrencyLevel the estimated number of concurrently
    *                         updating threads. The implementation performs internal sizing
    *                         to try to accommodate this many threads.
    * @throws IllegalArgumentException if the initial capacity is
    *                                  negative or the load factor or concurrencyLevel are
    *                                  nonpositive.
    */
   public ConcurrentReferenceHashSet(int initialCapacity, float loadFactor, int concurrencyLevel)
   {
      super(new ConcurrentReferenceHashMap<E, Object>(initialCapacity, loadFactor, concurrencyLevel));
   }

   /**
    * Creates a new, empty set with the specified initial capacity
    * and load factor and with the default reference types (weak keys,
    * strong values), and concurrencyLevel (16).
    *
    * @param initialCapacity The implementation performs internal
    *                        sizing to accommodate this many elements.
    * @param loadFactor      the load factor threshold, used to control resizing.
    *                        Resizing may be performed when the average number of elements per
    *                        bin exceeds this threshold.
    * @throws IllegalArgumentException if the initial capacity of
    *                                  elements is negative or the load factor is nonpositive
    * @since 1.6
    */
   public ConcurrentReferenceHashSet(int initialCapacity, float loadFactor)
   {
      super(new ConcurrentReferenceHashMap<E, Object>(initialCapacity, loadFactor));
   }

   /**
    * Creates a new, empty set with the specified initial capacity,
    * reference type and with default load factor (0.75) and concurrencyLevel (16).
    *
    * @param initialCapacity the initial capacity. The implementation
    *                        performs internal sizing to accommodate this many elements.
    * @param type         the reference type to use
    * @throws IllegalArgumentException if the initial capacity of
    *                                  elements is negative.
    */
   public ConcurrentReferenceHashSet(int initialCapacity, ConcurrentReferenceHashMap.ReferenceType type)
   {
      super(new ConcurrentReferenceHashMap<E, Object>(initialCapacity, type, ConcurrentReferenceHashMap.ReferenceType.STRONG));
   }

   /**
    * Creates a new, empty reference set with the specified key
    * and value reference types.
    *
    * @param type         the reference type to use
    * @throws IllegalArgumentException if the initial capacity of
    *                                  elements is negative.
    */
   public ConcurrentReferenceHashSet(ConcurrentReferenceHashMap.ReferenceType type)
   {
      super(new ConcurrentReferenceHashMap<E, Object>(type, ConcurrentReferenceHashMap.ReferenceType.STRONG));
   }

   /**
    * Creates a new, empty reference set with the specified reference types
    * and behavioral options.
    *
    * @param type         the reference type to use
    * @param options   the options
    * @throws IllegalArgumentException if the initial capacity of
    *                                  elements is negative.
    */
   public ConcurrentReferenceHashSet(ConcurrentReferenceHashMap.ReferenceType type, EnumSet<ConcurrentReferenceHashMap.Option> options)
   {
      super(new ConcurrentReferenceHashMap<E, Object>(type, ConcurrentReferenceHashMap.ReferenceType.STRONG, options));
   }

   /**
    * Creates a new, empty set with the specified initial capacity,
    * and with default reference types (weak keys, strong values),
    * load factor (0.75) and concurrencyLevel (16).
    *
    * @param initialCapacity the initial capacity. The implementation
    *                        performs internal sizing to accommodate this many elements.
    * @throws IllegalArgumentException if the initial capacity of
    *                                  elements is negative.
    */
   public ConcurrentReferenceHashSet(int initialCapacity)
   {
      super(new ConcurrentReferenceHashMap<E, Object>(initialCapacity));
   }

   /**
    * Creates a new, empty set with a default initial capacity (16),
    * reference types (weak keys, strong values), default
    * load factor (0.75) and concurrencyLevel (16).
    */
   public ConcurrentReferenceHashSet()
   {
      super(new ConcurrentReferenceHashMap<E, Object>());
   }

   /**
    * Creates a new set with the same contents as the given set.
    *
    * @param s the set
    */
   public ConcurrentReferenceHashSet(Set<? extends E> s)
   {
      super(new ConcurrentReferenceHashMap<E, Object>());
      addAll(s);
   }
}
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

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentSet based on top of ConcurrenthashMap.
 * It's serializable if the elements are serializable.
 *
 * @param <E> the element type
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ConcurrentSet<E> extends AbstractSet<E> implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   /** The delegate set */
   private ConcurrentHashMap<E, Object> map;
   /** The dummy object */
   private static final Object PRESENT = new Object();

   /**
    * Constructs a new, empty set; the backing <tt>ConcurrentHashMap</tt> instance has
    * default initial capacity (16) and load factor (0.75).
    */
   public ConcurrentSet()
   {
      map = new ConcurrentHashMap<E, Object>();
   }

   /**
    * Constructs a new set containing the elements in the specified
    * collection.  The <tt>ConcurrentHashMap</tt> is created with default load factor
    * (0.75) and an initial capacity sufficient to contain the elements in
    * the specified collection.
    *
    * @param c the collection whose elements are to be placed into this set.
    * @throws NullPointerException if the specified collection is null.
    */
   public ConcurrentSet(Collection<? extends E> c)
   {
      map = new ConcurrentHashMap<E, Object>(Math.max((int)(c.size() / .75f) + 1, 16));
      addAll(c);
   }

   /**
    * Constructs a new, empty set; the backing <tt>ConcurrentHashMap</tt> instance has
    * the specified initial capacity and the specified load factor.
    *
    * @param initialCapacity  the initial capacity. The implementation
    *                         performs internal sizing to accommodate this many elements.
    * @param loadFactor       the load factor threshold, used to control resizing.
    *                         Resizing may be performed when the average number of elements per
    *                         bin exceeds this threshold.
    * @param concurrencyLevel the estimated number of concurrently
    *                         updating threads. The implementation performs internal sizing
    *                         to try to accommodate this many threads.
    * @throws IllegalArgumentException if the initial capacity is less
    *                                  than zero, or if the load factor is nonpositive.
    */
   public ConcurrentSet(int initialCapacity, float loadFactor, int concurrencyLevel)
   {
      map = new ConcurrentHashMap<E, Object>(initialCapacity, loadFactor, concurrencyLevel);
   }

   /**
    * Constructs a new, empty set; the backing <tt>ConcurrentHashMap</tt> instance has
    * the specified initial capacity and default load factor, which is
    * <tt>0.75</tt>.
    *
    * @param initialCapacity the initial capacity of the hash table.
    * @throws IllegalArgumentException if the initial capacity is less
    *                                  than zero.
    */
   public ConcurrentSet(int initialCapacity)
   {
      map = new ConcurrentHashMap<E, Object>(initialCapacity);
   }

   /**
    * Returns an iterator over the elements in this set.  The elements
    * are returned in no particular order.
    *
    * @return an Iterator over the elements in this set.
    * @see java.util.ConcurrentModificationException
    */
   public Iterator<E> iterator()
   {
      return map.keySet().iterator();
   }

   /**
    * Returns the number of elements in this set (its cardinality).
    *
    * @return the number of elements in this set (its cardinality).
    */
   public int size()
   {
      return map.size();
   }

   /**
    * Returns <tt>true</tt> if this set contains no elements.
    *
    * @return <tt>true</tt> if this set contains no elements.
    */
   public boolean isEmpty()
   {
      return map.isEmpty();
   }

   /**
    * Returns <tt>true</tt> if this set contains the specified element.
    *
    * @param o element whose presence in this set is to be tested.
    * @return <tt>true</tt> if this set contains the specified element.
    */
   @SuppressWarnings({"SuspiciousMethodCalls"})
   public boolean contains(Object o)
   {
      return map.containsKey(o);
   }

   /**
    * Adds the specified element to this set if it is not already
    * present.
    *
    * @param o element to be added to this set.
    * @return <tt>true</tt> if the set did not already contain the specified
    *         element.
    */
   public boolean add(E o)
   {
      return map.put(o, PRESENT) == null;
   }

   /**
    * Removes the specified element from this set if it is present.
    *
    * @param o object to be removed from this set, if present.
    * @return <tt>true</tt> if the set contained the specified element.
    */
   public boolean remove(Object o)
   {
      return map.remove(o) == PRESENT;
   }

   /**
    * Removes all of the elements from this set.
    */
   public void clear()
   {
      map.clear();
   }
}
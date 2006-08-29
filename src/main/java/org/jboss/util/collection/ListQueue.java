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
import java.util.LinkedList;
import java.util.Iterator;

import org.jboss.util.NullArgumentException;

/**
 * A ListQueue implements a first-in, first-out container using a List as
 * a data structure.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ListQueue
   extends AbstractQueue
{
   /** List container */
   protected final List list;

   /**
    * Construct a new <i>constrained</i> ListQueue.
    *
    * @param list    The list which will be used to store queued objects.
    * @param maxSize The maximum size of the queue.
    *
    * @exception IllegalArgumentException    List is <i>null</i>.
    */
   public ListQueue(final List list, final int maxSize) {
      super(maxSize);

      if (list == null)
         throw new NullArgumentException("list");

      this.list = list;
   }

   /**
    * Construct a new <i>constrained</i> ListQueue using a
    * <code>LinkedList</code> for a data-structure.
    *
    * @param maxSize The maximum size of the queue.
    */
   public ListQueue(final int maxSize) {
      super(maxSize);
      this.list = new LinkedList();
   }

   /**
    * Construct a new <i>unconstrained</i> ListQueue.
    *
    * @param list    The list which will be used to store queued objects.
    *
    * @exception IllegalArgumentException    List is <i>null</i>
    */
   public ListQueue(final List list) {
      this(list, UNLIMITED_MAXIMUM_SIZE);
   }

   /**
    * Construct a new <i>unconstrained</i> ListQueue using a
    * <code>LinkedList</code> for a data-structure.
    */
   public ListQueue() {
      this(new LinkedList(), UNLIMITED_MAXIMUM_SIZE);
   }

   /**
    * Appends the given element to the end of this list.
    *
    * @param obj  Object to append.
    */
   protected boolean addLast(final Object obj) {
      return list.add(obj);
   }

   /**
    * Remove the first object in the queue.
    *
    * @return  First object in the queue.
    */
   protected Object removeFirst() {
      return list.remove(0);
   }

   /**
    * Get the size of the queue.
    *
    * @return  The number of elements in the queue.
    */
   public int size() {
      return list.size();
   }

   /**
    * Returns an iterator over the elements in this list in proper sequence.
    * 
    * @return  An iterator over the elements in this list in proper sequence.
    */
   public Iterator iterator() {
      return list.iterator();
   }

   /**
    * Get the object at the front of the queue.
    *
    * @return  Object at the front of the queue.
    *
    * @exception EmptyCollectionException    The queue is empty.
    */
   public Object getFront() throws EmptyCollectionException {
      if (isEmpty())
         throw new EmptyCollectionException();

      return list.get(0);
   }

   /**
    * Get the object at the back of the queue.
    *
    * @return  Object at the back of the queue.
    *
    * @exception EmptyCollectionException    The queue is empty.
    */
   public Object getBack() throws EmptyCollectionException {
      if (isEmpty())
         throw new EmptyCollectionException();

      return list.get(list.size() - 1);
   }

   /**
    * Returns an iterator over the elements in this list in reverse sequence.
    *
    * @return  An iterator over the elements in this list in reverse sequence.
    */
   public Iterator reverseIterator() {
      return new ReverseListIterator(list);
   }

   /**
    * Return a String representation of this queue.
    *
    * @return  String
    */
   public String toString() {
      return list.toString();
   }
}

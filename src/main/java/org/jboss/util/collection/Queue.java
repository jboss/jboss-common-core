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

import java.util.Collection;

/**
 * An iterface used to implement a first-in, first-out container.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public interface Queue
   extends Collection
{
   /** Unlimited maximum queue size identifier. */
   int UNLIMITED_MAXIMUM_SIZE = -1;

   /**
    * Get the maximum size of the queue.
    *
    * @return  Maximum pool size or {@link #UNLIMITED_MAXIMUM_SIZE}.
    */
   int getMaximumSize();

   /**
    * Set the maximum size of the queue.
    *
    * @param size    New maximim pool size or {@link #UNLIMITED_MAXIMUM_SIZE}.
    *
    * @exception IllegalArgumentException    Illegal size.
    */
   void setMaximumSize(int size) throws IllegalArgumentException;

   /**
    * Check if the queue is full.
    *
    * @return  True if the queue is full.
    */
   boolean isFull();

   /**
    * Check if the queue is empty.
    *
    * @return True if the queue is empty.
    */
   boolean isEmpty();

   /**
    * Enqueue an object onto the queue.
    *
    * @param obj     Object to enqueue.
    * @return        True if collection was modified.
    *
    * @exception FullCollectionException     The queue is full.
    */
   boolean add(Object obj) throws FullCollectionException;

   /**
    * Dequeue an object from the queue.
    *
    * @return     Dequeued object.
    *
    * @exception EmptyCollectionException    The queue is empty.
    */
   Object remove() throws EmptyCollectionException;

   /**
    * Get the object at the front of the queue.
    *
    * @return  Object at the front of the queue.
    *
    * @exception EmptyCollectionException    The queue is empty.
    */
   Object getFront() throws EmptyCollectionException;

   /**
    * Get the object at the back of the queue.
    *
    * @return  Object at the back of the queue.
    *
    * @exception EmptyCollectionException    The queue is empty.
    */
   Object getBack() throws EmptyCollectionException;
}

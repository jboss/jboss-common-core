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

import java.util.Iterator;
import java.util.NoSuchElementException;

import java.io.Serializable;

import org.jboss.util.NullArgumentException;

/**
 * An array iterator.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ArrayIterator
   implements Iterator, Serializable, Cloneable
{
   /** Array to iterate over. */
   protected final Object[] array;

   /** The current position in the array. */
   protected int index;

   /**
    * Construct an ArrayIterator.
    *
    * @param array   The array to iterate over.
    */
   public ArrayIterator(final Object[] array) {
      if (array == null)
         throw new NullArgumentException("array");

      this.array = array;
   }

   /**
    * Returns true if there are more elements in the iteration.
    *
    * @return  True if there are more elements in the iteration.
    */
   public boolean hasNext() {
      return index < array.length;
   }

   /**
    * Returns the next element in the iteration.
    *
    * @return  The next element in the iteration.
    *
    * @throws NoSuchElementException   The are no more elements available.
    */
   public Object next() {
      if (! hasNext())
         throw new NoSuchElementException();

      return array[index++];
   }

   /**
    * Unsupported.
    *
    * @throws UnsupportedOperationException
    */
   public void remove() {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns a shallow cloned copy of this object.
    *
    * @return  A shallow cloned copy of this object.
    */
   public Object clone() {
      try {
         return super.clone();
      }
      catch (CloneNotSupportedException e) {
         throw new InternalError();
      }
   }
}

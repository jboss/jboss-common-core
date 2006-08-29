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

/**
 * A compound iterator, which iterates over all of the elements in the
 * given iterators.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class CompoundIterator
   implements Iterator
{
   /** The array of iterators to iterate over. */
   protected final Iterator iters[];

   /** The index of the current iterator. */
   protected int index;

   /**
    * Construct a CompoundIterator over the given array of iterators.
    *
    * @param iters   Array of iterators to iterate over.
    *
    * @throws IllegalArgumentException    Array is <kk>null</kk> or empty.
    */
   public CompoundIterator(final Iterator iters[]) {
      if (iters == null || iters.length == 0)
         throw new IllegalArgumentException("array is null or empty");
     
      this.iters = iters;
   }

   /**
    * Check if there are more elements.
    *
    * @return  True if there are more elements.
    */
   public boolean hasNext() {
      for (; index < iters.length; index++) {
         if (iters[index] != null && iters[index].hasNext()) {
            return true;
         }
      }

      return false;
   }

   /**
    * Return the next element from the current iterator.
    *
    * @return  The next element from the current iterator.
    *
    * @throws NoSuchElementException   There are no more elements.
    */
   public Object next() {
      if (!hasNext()) {
         throw new NoSuchElementException();
      }

      return iters[index].next();
   }

   /**
    * Remove the current element from the current iterator.
    *
    * @throws IllegalStateException
    * @throws UnsupportedOperationException
    */
   public void remove() {
      iters[index].remove();
   }
}

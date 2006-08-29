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
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An iterator that returns elements in reverse order from a list.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ReverseListIterator
   implements Iterator
{
   /** The list to get elements from */
   protected final List list;
   
   /** The current index of the list */
   protected int current;

   /**
    * Construct a ReverseListIterator for the given list.
    *
    * @param list    List to iterate over.
    */
   public ReverseListIterator(final List list) {
      this.list = list;
      current = list.size() - 1;
   }

   /**
    * Check if there are more elements.
    *
    * @return  True if there are more elements.
    */
   public boolean hasNext() {
      return current > 0;
   }

   /**
    * Get the next element.
    *
    * @return  The next element.
    *
    * @throws NoSuchElementException
    */
   public Object next() {
      if (current <= 0) {
         throw new NoSuchElementException();
      }
      
      return list.get(current--);
   }

   /**
    * Remove the current element.
    */
   public void remove() {
      list.remove(current);
   }
}

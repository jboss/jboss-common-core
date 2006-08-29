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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * LazyList.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class LazyList implements List
{
   /** The delegate list */
   private List delegate = Collections.EMPTY_LIST; 

   public void add(int index, Object element)
   {
      if (delegate instanceof ArrayList == false)
         delegate = new ArrayList(delegate);
      delegate.add(index, element);
   }

   public boolean add(Object o)
   {
      if (delegate == Collections.EMPTY_LIST)
      {
         delegate = Collections.singletonList(o);
         return true;
      }
      else
      {
         if (delegate instanceof ArrayList == false)
            delegate = new ArrayList(delegate);
         return delegate.add(o);
      }
   }

   public boolean addAll(Collection c)
   {
      if (delegate instanceof ArrayList == false)
         delegate = new ArrayList(delegate);
      return delegate.addAll(c);
   }

   public boolean addAll(int index, Collection c)
   {
      if (delegate instanceof ArrayList == false)
         delegate = new ArrayList(delegate);
      return delegate.addAll(index, c);
   }

   public void clear()
   {
      delegate = Collections.EMPTY_LIST;
   }

   public boolean contains(Object o)
   {
      return delegate.contains(o);
   }

   public boolean containsAll(Collection c)
   {
      return delegate.containsAll(c);
   }

   public Object get(int index)
   {
      return delegate.get(index);
   }

   public int indexOf(Object o)
   {
      return delegate.indexOf(o);
   }

   public boolean isEmpty()
   {
      return delegate.isEmpty();
   }

   public Iterator iterator()
   {
      return delegate.iterator();
   }

   public int lastIndexOf(Object o)
   {
      return delegate.lastIndexOf(o);
   }

   public ListIterator listIterator()
   {
      return delegate.listIterator();
   }

   public ListIterator listIterator(int index)
   {
      return delegate.listIterator(index);
   }

   public Object remove(int index)
   {
      if (delegate instanceof ArrayList == false)
         delegate = new ArrayList(delegate);
      return delegate.remove(index);
   }

   public boolean remove(Object o)
   {
      if (delegate instanceof ArrayList == false)
         delegate = new ArrayList(delegate);
      return delegate.remove(o);
   }

   public boolean removeAll(Collection c)
   {
      if (delegate instanceof ArrayList == false)
         delegate = new ArrayList(delegate);
      return delegate.remove(c);
   }

   public boolean retainAll(Collection c)
   {
      if (delegate instanceof ArrayList == false)
         delegate = new ArrayList(delegate);
      return delegate.retainAll(c);
   }

   public Object set(int index, Object element)
   {
      if (delegate instanceof ArrayList == false)
         delegate = new ArrayList(delegate);
      return delegate.set(index, element);
   }

   public int size()
   {
      return delegate.size();
   }

   public List subList(int fromIndex, int toIndex)
   {
      return delegate.subList(fromIndex, toIndex);
   }

   public Object[] toArray()
   {
      return delegate.toArray();
   }

   public Object[] toArray(Object[] a)
   {
      return delegate.toArray(a);
   }
}

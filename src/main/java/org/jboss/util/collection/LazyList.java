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
import java.io.Serializable;

/**
 * LazyList.
 * It's serializable if the elements are serializable.
 * 
 * @param <T> the collection type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class LazyList<T> implements List<T>, Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   /** The delegate list */
   private List<T> delegate = Collections.emptyList(); 

   /**
    * Create the list implementation
    * 
    * @return the list
    */
   private List<T> createImplementation()
   {
      if (delegate instanceof ArrayList == false)
         return new ArrayList<T>(delegate);
      return delegate;
   }
   
   public void add(int index, T element)
   {
      delegate = createImplementation();
      delegate.add(index, element);
   }

   public boolean add(T o)
   {
      if (delegate.isEmpty())
      {
         delegate = Collections.singletonList(o);
         return true;
      }
      else
      {
         delegate = createImplementation();
         return delegate.add(o);
      }
   }

   public boolean addAll(Collection<? extends T> c)
   {
      delegate = createImplementation();
      return delegate.addAll(c);
   }

   public boolean addAll(int index, Collection<? extends T> c)
   {
      delegate = createImplementation();
      return delegate.addAll(index, c);
   }

   public void clear()
   {
      delegate = Collections.emptyList();
   }

   public boolean contains(Object o)
   {
      return delegate.contains(o);
   }

   public boolean containsAll(Collection<?> c)
   {
      return delegate.containsAll(c);
   }

   public T get(int index)
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

   public Iterator<T> iterator()
   {
      return delegate.iterator();
   }

   public int lastIndexOf(Object o)
   {
      return delegate.lastIndexOf(o);
   }

   public ListIterator<T> listIterator()
   {
      return delegate.listIterator();
   }

   public ListIterator<T> listIterator(int index)
   {
      return delegate.listIterator(index);
   }

   public T remove(int index)
   {
      delegate = createImplementation();
      return delegate.remove(index);
   }

   public boolean remove(Object o)
   {
      delegate = createImplementation();
      return delegate.remove(o);
   }

   public boolean removeAll(Collection<?> c)
   {
      delegate = createImplementation();
      return delegate.removeAll(c);
   }

   public boolean retainAll(Collection<?> c)
   {
      delegate = createImplementation();
      return delegate.retainAll(c);
   }

   public T set(int index, T element)
   {
      delegate = createImplementation();
      return delegate.set(index, element);
   }

   public int size()
   {
      return delegate.size();
   }

   public List<T> subList(int fromIndex, int toIndex)
   {
      return delegate.subList(fromIndex, toIndex);
   }

   public Object[] toArray()
   {
      return delegate.toArray();
   }

   public <U> U[] toArray(U[] a)
   {
      return delegate.toArray(a);
   }
}

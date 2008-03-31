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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.io.Serializable;

/**
 * LazySet.
 * It's serializable if the elements are serializable.
 *
 * @param <T> the element type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class LazySet<T> implements Set<T>, Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   /** The delegate set */
   private Set<T> delegate = Collections.emptySet();

   /**
    * Create the set implementation
    * 
    * @return the set
    */
   private Set<T> createImplementation()
   {
      if (delegate instanceof HashSet == false)
         return new HashSet<T>(delegate);
      return delegate;
   }

   public boolean add(T o)
   {
      if (delegate.isEmpty())
      {
         delegate = Collections.singleton(o);
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

   public void clear()
   {
      delegate = Collections.emptySet();
   }

   public boolean contains(Object o)
   {
      return delegate.contains(o);
   }

   public boolean containsAll(Collection<?> c)
   {
      return delegate.containsAll(c);
   }

   public boolean isEmpty()
   {
      return delegate.isEmpty();
   }

   public Iterator<T> iterator()
   {
      return delegate.iterator();
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

   public int size()
   {
      return delegate.size();
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

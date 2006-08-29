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

/**
 * LazySet.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class LazySet implements Set
{
   /** The delegate set */
   private Set delegate = Collections.EMPTY_SET;
   
   public boolean add(Object o)
   {
      if (delegate == Collections.EMPTY_SET)
      {
         delegate = Collections.singleton(o);
         return true;
      }
      else
      {
         if (delegate instanceof HashSet == false)
            delegate = new HashSet(delegate);
         return delegate.add(o);
      }
   }

   public boolean addAll(Collection c)
   {
      if (delegate instanceof HashSet == false)
         delegate = new HashSet(delegate);
      return delegate.addAll(c);
   }

   public void clear()
   {
      delegate = Collections.EMPTY_SET;
   }

   public boolean contains(Object o)
   {
      return delegate.contains(o);
   }

   public boolean containsAll(Collection c)
   {
      return delegate.containsAll(c);
   }

   public boolean isEmpty()
   {
      return delegate.isEmpty();
   }

   public Iterator iterator()
   {
      return delegate.iterator();
   }

   public boolean remove(Object o)
   {
      if (delegate instanceof HashSet == false)
         delegate = new HashSet(delegate);
      return delegate.remove(o);
   }

   public boolean removeAll(Collection c)
   {
      if (delegate instanceof HashSet == false)
         delegate = new HashSet(delegate);
      return delegate.removeAll(c);
   }

   public boolean retainAll(Collection c)
   {
      if (delegate instanceof HashSet == false)
         delegate = new HashSet(delegate);
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

   public Object[] toArray(Object[] a)
   {
      return delegate.toArray(a);
   }
}

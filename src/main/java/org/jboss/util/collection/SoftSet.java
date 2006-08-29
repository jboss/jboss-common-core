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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * An implementation of Set that manages a map of soft references to
 * the set values. The map is keyed by the value hashCode and so this
 * is only useful for value whose hashCode is a valid identity
 * representation (String, primative wrappers, etc).
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class SoftSet implements Set
{
   private HashMap map = new HashMap();
   /** The queue of garbage collected soft references */
   private ReferenceQueue gcqueue = new ReferenceQueue();

   static class ComparableSoftReference extends SoftReference
   {
      private Integer key;
      ComparableSoftReference(Integer key, Object o, ReferenceQueue q)
      {
         super(o, q);
         this.key = key;
      }
      Integer getKey()
      {
         return key;
      }
   }
   static class ComparableSoftReferenceIterator implements Iterator
   {
      Iterator theIter;
      ComparableSoftReferenceIterator(Iterator theIter)
      {
         this.theIter = theIter;
      }
      public boolean hasNext()
      {
         return theIter.hasNext();
      }
      public Object next()
      {
         ComparableSoftReference csr = (ComparableSoftReference) theIter.next();
         return csr.get();
      }
      public void remove()
      {
         theIter.remove();
      }
   }

   /**
    * 
    */
   public SoftSet()
   {
   }

   public int size()
   {
      processQueue();
      return map.size();
   }

   public boolean isEmpty()
   {
      processQueue();
      return map.isEmpty();
   }

   public boolean contains(Object o)
   {
      processQueue();
      Integer key = new Integer(o.hashCode());
      boolean contains = map.containsKey(key);
      return contains;
   }

   public Iterator iterator()
   {
      processQueue();
      Iterator theIter = map.values().iterator();
      return new ComparableSoftReferenceIterator(theIter);
   }

   public Object[] toArray()
   {
      processQueue();
      return toArray(new Object[0]);
   }

   public Object[] toArray(Object[] a)
   {
      processQueue();
      int size = map.size();
      Object[] array = {};
      if( a.length >= size )
         array = a;
      Iterator iter = map.values().iterator();
      int index = 0;
      while( iter.hasNext() )
      {
         ComparableSoftReference csr = (ComparableSoftReference) iter.next();
         Object value = csr.get();
         // Create the correct array type
         if( array.length == 0 )
         {
            if( value == null )
            {
               index ++;
               continue;
            }
            Array.newInstance(value.getClass(), size);
         }
         array[index] = value;
         index ++;
      }
      return array;
   }

   public boolean add(Object o)
   {
      processQueue();
      Integer key = new Integer(o.hashCode());
      ComparableSoftReference sr = new ComparableSoftReference(key, o, gcqueue);
      return map.put(key, sr) == null;
   }

   public boolean remove(Object o)
   {
      processQueue();
      Integer key = new Integer(o.hashCode());
      return map.remove(key) != null;
   }

   public boolean containsAll(Collection c)
   {
      processQueue();
      Iterator iter = c.iterator();
      boolean contains = true;
      while( iter.hasNext() )
      {
         Object value = iter.next();
         Integer key = new Integer(value.hashCode());
         contains &= map.containsKey(key);
      }
      return contains;
   }

   public boolean addAll(Collection c)
   {
      processQueue();
      Iterator iter = c.iterator();
      boolean added = false;
      while( iter.hasNext() )
      {
         Object value = iter.next();
         Integer key = new Integer(value.hashCode());
         ComparableSoftReference sr = new ComparableSoftReference(key, value, gcqueue);
         added |= map.put(key, sr) == null;
      }
      return added;
   }

   public boolean retainAll(Collection c)
   {
      Iterator iter = iterator();
      boolean removed = false;
      while( iter.hasNext() )
      {
         Object value = iter.next();
         if( c.contains(value) == false )
         {
            iter.remove();
            removed = true;
         }
      }
      return removed;
   }

   public boolean removeAll(Collection c)
   {
      processQueue();
      Iterator iter = c.iterator();
      boolean removed = false;
      while( iter.hasNext() )
      {
         Object value = iter.next();
         removed |= remove(value);
      }
      return removed;
   }

   public void clear()
   {
      while( gcqueue.poll() != null )
         ;
      map.clear();
   }

   public boolean equals(Object o)
   {
      return map.equals(o);
   }

   public int hashCode()
   {
      return map.hashCode();
   }

   /**
    * Iterate through the gcqueue for for any cleared reference, remove
    * the associated value from the underlying set.
    */
   private void processQueue()
   {
      ComparableSoftReference cr;
      while( (cr = (ComparableSoftReference) gcqueue.poll()) != null )
      {
         map.remove(cr.getKey());
      }
   }

}

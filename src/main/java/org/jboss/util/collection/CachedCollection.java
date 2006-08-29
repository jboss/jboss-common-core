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
import java.util.AbstractCollection;
import java.util.Iterator;

import java.lang.ref.ReferenceQueue;

import org.jboss.util.SoftObject;
import org.jboss.util.Objects;

/**
 * A wrapper around a <code>Collection</code> which translates added objects
 * into {@link SoftObject} references, allowing the VM to garbage collect
 * objects in the collection when memory is low.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class CachedCollection
   extends AbstractCollection
{
   /** Reference queue */
   protected final ReferenceQueue queue = new ReferenceQueue();

   /** Wrapped collection */
   protected final Collection collection;

   /**
    * Construct a CachedCollection.
    *
    * @param collection    Collection to wrap.
    */
   public CachedCollection(final Collection collection) {
      this.collection = collection;
   }

   /**
    * Returns an iterator over the elements contained in this collection.
    *
    * @return An iterator over the elements contained in this collection.
    */
   public Iterator iterator() {
      maintain();
      return new MyIterator(collection.iterator());
   }

   /**
    * Returns the size of the collection.
    *
    * @return  The number of elements in the collection.
    */
   public int size() {
      maintain();
      return collection.size();
   }

   /**
    * Add an object to the collection.
    *
    * @param obj  Object (or <i>null</i> to add to the collection.
    * @return     True if object was added.
    */
   public boolean add(final Object obj) {
      maintain();

      SoftObject soft = SoftObject.create(obj, queue);
      
      return collection.add(soft);
   }

   /**
    * Maintains the collection by removing garbage collected objects.
    */
   private void maintain() {
      SoftObject obj;
      int count = 0;

      while ((obj = (SoftObject)queue.poll()) != null) {
         count++;
         collection.remove(obj);
      }

      if (count != 0) {
         // some temporary debugging fluff
         System.err.println("vm reclaimed " + count + " objects");
      }
   }


   /////////////////////////////////////////////////////////////////////////
   //                       De-Referencing Iterator                       //
   /////////////////////////////////////////////////////////////////////////

   /**
    * A dereferencing iterator.
    */
   private final class MyIterator
      implements Iterator
   {
      private final Iterator iter;

      public MyIterator(final Iterator iter) {
         this.iter = iter;
      }

      public boolean hasNext() {
         maintain();
         return iter.hasNext();
      }

      private Object nextObject() {
         Object obj = iter.next();

         return Objects.deref(obj);
      }

      public Object next() {
         maintain();
         return nextObject();
      }

      public void remove() {
         maintain();
         iter.remove();
      }
   }
}

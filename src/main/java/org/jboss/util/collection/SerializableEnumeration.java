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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * ???
 *      
 * @author ???
 * @version $Revision$
 */
public class SerializableEnumeration
   extends ArrayList
   implements Enumeration
{
   private int index;

   public SerializableEnumeration () {
      index = 0;
   }

   public SerializableEnumeration (Collection c) {
      super(c);
      index = 0;
   }
	 
   public SerializableEnumeration (int initialCapacity) {
      super(initialCapacity);
      index = 0;
   }
	 
   public boolean hasMoreElements() {
      return (index < size());
   }
	
   public Object nextElement() throws NoSuchElementException
   {
      try {
         Object nextObj = get(index);
         index++;
         return nextObj;
      }
      catch (IndexOutOfBoundsException e) {
         throw new NoSuchElementException();
      }
   }

   private void writeObject(java.io.ObjectOutputStream out)
      throws java.io.IOException
   {
      // the only thing to write is the index field
      out.defaultWriteObject();
   }
   
   private void readObject(java.io.ObjectInputStream in)
      throws java.io.IOException, ClassNotFoundException
   {
      in.defaultReadObject();
   }
}

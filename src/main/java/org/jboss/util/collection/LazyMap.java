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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * LazyMap.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class LazyMap implements Map
{
   /** The delegate map */
   private Map delegate = Collections.EMPTY_MAP;
   
   public void clear()
   {
      delegate = Collections.EMPTY_MAP;
   }

   public boolean containsKey(Object key)
   {
      return delegate.containsKey(key);
   }

   public boolean containsValue(Object value)
   {
      return delegate.containsValue(value);
   }

   public Set entrySet()
   {
      return delegate.entrySet();
   }

   public Object get(Object key)
   {
      return delegate.get(key);
   }

   public boolean isEmpty()
   {
      return delegate.isEmpty();
   }

   public Set keySet()
   {
      return delegate.keySet();
   }

   public Object put(Object key, Object value)
   {
      if (delegate == Collections.EMPTY_MAP)
      {
         delegate = Collections.singletonMap(key, value);
         return null;
      }
      else
      {
         if (delegate instanceof HashMap == false)
            delegate = new HashMap(delegate);
         return delegate.put(key, value);
      }
   }

   public void putAll(Map t)
   {
      if (delegate instanceof HashMap == false)
         delegate = new HashMap(delegate);
      delegate.putAll(t);
   }

   public Object remove(Object key)
   {
      if (delegate instanceof HashMap == false)
         delegate = new HashMap(delegate);
      return delegate.remove(key);
   }

   public int size()
   {
      return delegate.size();
   }

   public Collection values()
   {
      return delegate.values();
   }
}

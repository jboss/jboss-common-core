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
package org.jboss.util;


/**
 * A synchronized cache policy wrapper.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision$
 * @see CachePolicy
 */
public final class SynchronizedCachePolicy
   implements CachePolicy
{

   // Attributes ----------------------------------------------------

   private final CachePolicy delegate;

   // Constructors --------------------------------------------------

   public SynchronizedCachePolicy(CachePolicy delegate)
   {
      this.delegate = delegate;
   }

   // CachePolicy implementation ------------------------------------

   synchronized public Object get(Object key)
   {
      return delegate.get(key);
   }

   synchronized public Object peek(Object key)
   {
      return delegate.get(key);
   }

   synchronized public void insert(Object key, Object object)
   {
      delegate.insert(key, object);
   }

   synchronized public void remove(Object key)
   {
      delegate.remove(key);
   }

   synchronized public void flush()
   {
      delegate.flush();
   }

   synchronized public int size()
   {
      return delegate.size();
   }

   synchronized public void create() throws Exception
   {
      delegate.create();
   }

   synchronized public void start() throws Exception
   {
      delegate.start();
   }

   synchronized public void stop()
   {
      delegate.stop();
   }

   synchronized public void destroy()
   {
      delegate.destroy();
   }
}

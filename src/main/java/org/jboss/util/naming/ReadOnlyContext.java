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

package org.jboss.util.naming;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.OperationNotSupportedException;

/**
 A JNDI context wrapper implementation that delegates read-only methods to
 its delegate Context, and throws OperationNotSupportedException for any
 method with a side-effect.

 @author Scott.Stark@jboss.org
 @version $Revision$
 */
public class ReadOnlyContext implements Context
{
   /** The actual context we impose the read-only behavior on */
   private Context delegate;

   public ReadOnlyContext(Context delegate)
   {
      this.delegate = delegate;
   }

// Supported methods -----------------------------------------------------------
   public void close()
      throws NamingException
   {
      delegate.close();
   }

   public String composeName(String name, String prefix)
      throws NamingException
   {
      return delegate.composeName(name, prefix);
   }

   public Name composeName(Name name, Name prefix)
      throws NamingException
   {
      return delegate.composeName(name, prefix);
   }

   public String getNameInNamespace()
      throws NamingException
   {
      return delegate.getNameInNamespace();
   }

   public Hashtable getEnvironment()
      throws NamingException
   {
      return delegate.getEnvironment();
   }

   public Object lookup(String name)
      throws NamingException
   {
      return delegate.lookup(name);
   }

   public Object lookupLink(String name)
      throws NamingException
   {
      return delegate.lookupLink(name);
   }

   public Object lookup(Name name)
      throws NamingException
   {
      return delegate.lookup(name);
   }

   public Object lookupLink(Name name)
      throws NamingException
   {
      return delegate.lookupLink(name);
   }

   public NameParser getNameParser(String name)
      throws NamingException
   {
      return delegate.getNameParser(name);
   }

   public NameParser getNameParser(Name name)
      throws NamingException
   {
      return delegate.getNameParser(name);
   }

   public NamingEnumeration list(String name)
      throws NamingException
   {
      return delegate.list(name);
   }

   public NamingEnumeration listBindings(String name)
      throws NamingException
   {
      return delegate.listBindings(name);
   }

   public NamingEnumeration list(Name name)
      throws NamingException
   {
      return delegate.list(name);
   }

   public NamingEnumeration listBindings(Name name)
      throws NamingException
   {
      return delegate.listBindings(name);
   }

// Unsupported methods ---------------------------------------------------------

   public Object addToEnvironment(String propName, Object propVal)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void bind(String name, Object obj)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }
   public void bind(Name name, Object obj)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public Context createSubcontext(String name)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public Context createSubcontext(Name name)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void destroySubcontext(String name)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void destroySubcontext(Name name)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public Object removeFromEnvironment(String propName)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void rebind(String name, Object obj)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void rebind(Name name, Object obj)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void rename(String oldName, String newName)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void rename(Name oldName, Name newName)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void unbind(String name)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }
   public void unbind(Name name)
      throws NamingException
   {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

}

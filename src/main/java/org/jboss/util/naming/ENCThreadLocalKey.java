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
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.RefAddr;
import javax.naming.LinkRef;
import javax.naming.spi.ObjectFactory;

import org.jboss.logging.Logger;

/**
 * Return a LinkRef based on a ThreadLocal key.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public class ENCThreadLocalKey
      implements ObjectFactory
{
   private static final Logger log = Logger.getLogger(ENCThreadLocalKey.class);

   // We need all the weak maps to make sure everything is released properly
   // and we don't have any memory leaks

   private final static ThreadLocal key = new ThreadLocal();

   public static void setKey(String tlkey)
   {
      key.set(tlkey);
   }

   public static String getKey()
   {
      return (String) key.get();
   }

   public Object getObjectInstance(Object obj,
         Name name,
         Context nameCtx,
         Hashtable environment)
         throws Exception
   {
      Reference ref = (Reference) obj;
      String reftype = (String) key.get();
      boolean trace = log.isTraceEnabled();

      if (reftype == null)
      {
         if (trace)
            log.trace("using default in ENC");
         reftype = "default";
      }

      RefAddr addr = ref.get(reftype);
      if (addr == null)
      {
         if (trace)
            log.trace("using default in ENC");
         addr = ref.get("default"); // try to get default linking
      }
      if (addr != null)
      {
         String target = (String) addr.getContent();
         if (trace)
            log.trace("found Reference " + reftype + " with content " + target);
         return new LinkRef(target);
      }
      return null;
   }

}

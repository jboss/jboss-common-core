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
package org.jboss.util.file;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Comment
 *
 * @deprecated
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public abstract class ArchiveBrowser
{
   public interface Filter
   {
      boolean accept(String filename);
   }

   //use concurrent hashmap since a protocol can be added on the fly through the public attribute
   public static Map factoryFinder = new ConcurrentHashMap();

   static
   {
      factoryFinder.put("file", new FileProtocolArchiveBrowserFactory());
      factoryFinder.put("jar", new JarProtocolArchiveBrowserFactory());
   }

   public static Iterator getBrowser(URL url, Filter filter)
   {
      ArchiveBrowserFactory factory = (ArchiveBrowserFactory)factoryFinder.get(url.getProtocol());
      if (factory == null) throw new RuntimeException("Archive browser cannot handle protocol: " + url);
      return factory.create(url, filter);
   }
}

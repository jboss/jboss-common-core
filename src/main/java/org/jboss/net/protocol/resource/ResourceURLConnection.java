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
package org.jboss.net.protocol.resource;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.net.URL;
import java.net.MalformedURLException;

import org.jboss.net.protocol.DelegatingURLConnection;

import org.jboss.logging.Logger;

/**
 * Provides access to system resources as a URLConnection.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author Scott.Stark@jboss.org
 */
public class ResourceURLConnection
   extends DelegatingURLConnection
{
   private static final Logger log = Logger.getLogger(ResourceURLConnection.class);

   public ResourceURLConnection(final URL url)
      throws MalformedURLException, IOException
   {
      super(url);
   }

   protected URL makeDelegateUrl(final URL url)
      throws MalformedURLException, IOException
   {
      String name = url.getHost();
      String file = url.getFile();
      if (file != null && !file.equals(""))
      {
         name += file;
      }

      // first try TCL and then SCL

      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      URL target = cl.getResource(name);

      if (target == null)
      {
         cl = ClassLoader.getSystemClassLoader();
         target = cl.getResource(name);
      }

      if (target == null)
         throw new FileNotFoundException("Could not locate resource: " + name);

      if (log.isTraceEnabled())
      {
         log.trace("Target resource URL: " + target);
         try
         {
            log.trace("Target resource URL connection: " + target.openConnection());
         }
         catch (Exception ignore)
         {
         }
      }

      // Return a new URL as the cl version does not use the JB stream factory
      return new URL(target.toExternalForm());
   }
}

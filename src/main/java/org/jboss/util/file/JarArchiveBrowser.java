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

import java.util.Iterator;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.io.File;
import java.io.IOException;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 *
 **/
public class JarArchiveBrowser implements Iterator
{
   ZipFile zip;
   Enumeration entries;
   ZipEntry next;
   ArchiveBrowser.Filter filter;

   public JarArchiveBrowser(File f, ArchiveBrowser.Filter filter)
   {
      this.filter = filter;
      try
      {
         zip = new ZipFile(f);
         entries = zip.entries();
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
      setNext();
   }

   public boolean hasNext()
   {
      return next != null;
   }

   private void setNext()
   {
      next = null;
      while (entries.hasMoreElements() && next == null)
      {
         do
         {
            next = (ZipEntry) entries.nextElement();
         } while (entries.hasMoreElements() && next.isDirectory());
         if (next.isDirectory()) next = null;

         if (next != null && !filter.accept(next.getName()))
         {
            next = null;
         }
      }
   }

   public Object next()
   {
      ZipEntry entry = next;
      setNext();

      try
      {
         return zip.getInputStream(entry);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   public void remove()
   {
      throw new RuntimeException("Illegal operation on ArchiveBrowser");
   }
}

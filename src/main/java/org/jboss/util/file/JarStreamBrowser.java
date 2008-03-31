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
import java.util.jar.JarInputStream;
import java.util.jar.JarEntry;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 1958 $
 */
@SuppressWarnings("unchecked")
public class JarStreamBrowser implements Iterator
{
//   ZipFile zip;
//   Enumeration entries;
   JarInputStream jar;
   JarEntry next;
   @SuppressWarnings("deprecation")
   ArchiveBrowser.Filter filter;

   @SuppressWarnings("deprecation")
   public JarStreamBrowser(File file, ArchiveBrowser.Filter filter) throws IOException
   {
      this(new FileInputStream(file), filter);
   }

   @SuppressWarnings("deprecation")
   public JarStreamBrowser(InputStream is, ArchiveBrowser.Filter filter) throws IOException
   {
      this.filter = filter;
      jar = new JarInputStream(is);
      setNext();
   }

   public boolean hasNext()
   {
      return next != null;
   }

   private void setNext()
   {
      try
      {
         if (next != null) jar.closeEntry();
         next = null;
         do
         {
            next = jar.getNextJarEntry();
         } while (next != null && (next.isDirectory() || !filter.accept(next.getName())));
         if (next == null) jar.close();
      }
      catch (IOException e)
      {
         throw new RuntimeException("failed to browse jar", e);
      }
   }

   public Object next()
   {
      int size = (int) next.getSize();
      byte[] buf = new byte[size];
      int count = 0;
      int current = 0;
      try
      {
         while ((
                 (
                         current = jar.read(buf, count,
                                 size - count)
                 ) != -1
         ) && (count < size))
         {
            count += current;
         }
         ByteArrayInputStream bais = new ByteArrayInputStream(buf);
         setNext();
         return bais;
      }
      catch (IOException e)
      {
         try
         {
            jar.close();
         }
         catch (IOException ignored)
         {

         }
         throw new RuntimeException(e);
      }
   }

   public void remove()
   {
      throw new RuntimeException("Illegal operation on ArchiveBrowser");
   }
}

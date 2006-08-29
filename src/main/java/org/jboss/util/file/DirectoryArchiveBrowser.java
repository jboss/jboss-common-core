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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class DirectoryArchiveBrowser implements Iterator
{
   private Iterator files;

   public DirectoryArchiveBrowser(File file, ArchiveBrowser.Filter filter)
   {
      ArrayList list = new ArrayList();
      try
      {
         create(list, file, filter);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
      files = list.iterator();
   }

   public static void create(List list, File dir, ArchiveBrowser.Filter filter) throws Exception
   {
      File[] files = dir.listFiles();
      for (int i = 0; i < files.length; i++)
      {
         if (files[i].isDirectory())
         {
            create(list, files[i], filter);
         }
         else
         {
            if (filter.accept(files[i].getAbsolutePath()))
            {
               list.add(files[i]);
            }
         }
      }
   }

   public boolean hasNext()
   {
      return files.hasNext();
   }

   public Object next()
   {
      File fp = (File) files.next();
      try
      {
         return new FileInputStream(fp);
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   public void remove()
   {
      throw new RuntimeException("Illegal operation call");
   }


}

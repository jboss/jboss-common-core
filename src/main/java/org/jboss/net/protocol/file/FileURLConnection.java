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
package org.jboss.net.protocol.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.net.URLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLDecoder;

import java.security.Permission;
import java.io.FilePermission;
import java.io.BufferedInputStream;

/**
 * Provides local file access via URL semantics, correctly returning
 * the last modified time of the underlying file.
 *
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author  <a href="mailto:scott.stark@jboss.org">Scott.Stark</a>
 * @version $Revision$
 */
public class FileURLConnection extends URLConnection
{
   static boolean decodeFilePaths = true;
   static
   {
      String flag = System.getProperty("org.jboss.net.protocol.file.decodeFilePaths");
      if (flag != null)
         decodeFilePaths = Boolean.valueOf(flag).booleanValue();
   }
   
   protected File file;

   public FileURLConnection(final URL url) throws MalformedURLException, IOException
   {
      super(url);
      String path = url.getPath();
      if (decodeFilePaths)
         path = URLDecoder.decode(path, "UTF-8");
      
      // Convert the url '/' to the os file separator
      file = new File(path.replace('/', File.separatorChar).replace('|', ':'));

      doOutput = false;
   }

   /**
    * Returns the underlying file for this connection.
    */
   public File getFile()
   {
      return file;
   }

   /**
    * Checks if the underlying file for this connection exists.
    *
    * @throws FileNotFoundException
    */
   public void connect() throws IOException
   {
      if (connected)
         return;

      if (!file.exists())
      {
         throw new FileNotFoundException(file.getPath());
      }
      
      connected = true;
   }

   public InputStream getInputStream() throws IOException
   {
      if (!connected)
         connect();

      return new FileInputStream(file);
   }

   public OutputStream getOutputStream() throws IOException
   {
      if (!connected)
         connect();
      SecurityManager sm = System.getSecurityManager();
      if( sm != null )
      {
         // Check for write access
         FilePermission p = new FilePermission(file.getPath(), "write");
         sm.checkPermission(p);
      }
      return new FileOutputStream(file);
   }

   /**
    * Provides support for returning the value for the
    * <tt>last-modified</tt> header.
    */
   public String getHeaderField(final String name)
   {
      String headerField = null;
      if (name.equalsIgnoreCase("last-modified"))
         headerField = String.valueOf(getLastModified());
      else if (name.equalsIgnoreCase("content-length"))
         headerField = String.valueOf(file.length());
      else if (name.equalsIgnoreCase("content-type"))
      {
         headerField = getFileNameMap().getContentTypeFor(file.getName());
         if( headerField == null )
         {
            try
            {
               InputStream is = getInputStream();
               BufferedInputStream bis = new BufferedInputStream(is);
               headerField = URLConnection.guessContentTypeFromStream(bis);
               bis.close();
            }
            catch(IOException e)
            {
            }
         }
      }
      else if (name.equalsIgnoreCase("date"))
         headerField = String.valueOf(file.lastModified());
      else
      {
         // This always returns null currently
         headerField = super.getHeaderField(name);
      }
      return headerField;
   }

   /** 
    * Return a permission for reading of the file
    */
   public Permission getPermission() throws IOException
   {
      return new FilePermission(file.getPath(), "read");
   }

   /**
    * Returns the last modified time of the underlying file.
    */
   public long getLastModified()
   {
      return file.lastModified();
   }
}

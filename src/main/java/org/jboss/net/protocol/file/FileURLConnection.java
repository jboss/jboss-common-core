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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Provides local file access via URL semantics, correctly returning
 * the last modified time of the underlying file.
 *
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author  <a href="mailto:scott.stark@jboss.org">Scott Stark</a>
 * @author  <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 * @version $Revision$
 */
public class FileURLConnection extends URLConnection
{
   static boolean decodeFilePaths = true;
   static
   {
      String flag = System.getProperty("org.jboss.net.protocol.file.decodeFilePaths");
      if (flag != null)
      {
         decodeFilePaths = Boolean.valueOf(flag).booleanValue();
      }
   }
   
   /** The underlying file */
   protected File file;

   public FileURLConnection(final URL url) throws MalformedURLException, IOException
   {
      super(url);
      
      String path = url.getPath();
      if (decodeFilePaths)
      {
         path = URLDecoder.decode(path, "UTF-8");
      }
      // Convert the url '/' to the os file separator
      file = new File(path.replace('/', File.separatorChar).replace('|', ':'));

      super.doOutput = false;
   }

   /**
    * Returns the underlying file for this connection.
    * @return the file
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
      connect();

      if (file.isDirectory())
      {
         // return a sorted list of the directory contents
         String[] files = file.list();
         Arrays.sort(files);
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < files.length; i++)
         {
            sb.append(files[i]).append("\n");
         }
         return new ByteArrayInputStream(sb.toString().getBytes());
      }
      else
      {
         return new FileInputStream(file);
      }
   }

   // We should probably disallow this?
   public OutputStream getOutputStream() throws IOException
   {
      connect();
      
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
      {
         // Check for write access
         FilePermission p = new FilePermission(file.getPath(), "write");
         sm.checkPermission(p);
      }
      return new FileOutputStream(file);
   }

   /**
    * Provides support for the following headers:
    * 
    * <tt>last-modified</tt>
    * <tt>content-length</tt>
    * <tt>content-type</tt>
    * <tt>date</tt>
    */
   public String getHeaderField(final String name)
   {
      String headerField = null;
      if (name.equalsIgnoreCase("last-modified"))
      {
         long lastModified = getLastModified();
         if (lastModified != 0)
         {
            // return the last modified date formatted according to RFC 1123
            Date modifiedDate = new Date(lastModified);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            headerField = sdf.format(modifiedDate);
         }
      }
      else if (name.equalsIgnoreCase("content-length"))
      {
         headerField = String.valueOf(file.length());
      }
      else if (name.equalsIgnoreCase("content-type"))
      {
         if (file.isDirectory())
         {
            headerField = "text/plain";
         }
         else
         {
            headerField = getFileNameMap().getContentTypeFor(file.getName());
            if (headerField == null)
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
                  // ignore
               }
            }
         }
      }
      else if (name.equalsIgnoreCase("date"))
      {
         headerField = String.valueOf(getLastModified());
      }
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

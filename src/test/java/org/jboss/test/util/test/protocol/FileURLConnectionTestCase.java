/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.util.test.protocol;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.TestCase;

/**
 * Tests of the expected jdk file: url connection protocol handler behaviors.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class FileURLConnectionTestCase extends TestCase
{
   public void testLastModified()
      throws Exception
   {
      File tmp = File.createTempFile("testLastModified", "test");
      tmp.deleteOnExit();
      long lastModified = tmp.lastModified();
      System.out.println(tmp.getAbsolutePath()+", lastModified:"+lastModified);
      URL tmpURL = tmp.toURL();
      URLConnection tmpConn = tmpURL.openConnection();
      assertEquals(lastModified, tmpConn.getLastModified());
      long lastModifiedHdr = tmpConn.getHeaderFieldDate("Last-Modified", 0);
      System.out.println("Last-Modified: "+lastModifiedHdr);
      assertEquals(lastModified, lastModifiedHdr);
   }

   public void testLength()
      throws Exception
   {
      File tmp = File.createTempFile("testLastModified", "test");
      tmp.deleteOnExit();
      FileOutputStream fos = new FileOutputStream(tmp);
      fos.write("testLength".getBytes());
      fos.close();

      URL tmpURL = tmp.toURL();
      URLConnection tmpConn = tmpURL.openConnection();
      int length = tmpConn.getContentLength();
      System.out.println(tmp.getAbsolutePath()+", length:"+length);
      assertEquals(tmp.length(), length);
      int lengthHdr = tmpConn.getHeaderFieldInt("Content-Length", 0);
      assertEquals(length, lengthHdr);
   }
}

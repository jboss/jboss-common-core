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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.TestCase;

import org.jboss.net.protocol.file.FileURLConnection;
import org.jboss.util.file.Files;

/**
 * Tests of the expected jdk file: url connection protocol handler behaviors.
 * 
 * @author Scott.Stark@jboss.org
 * @author Dimitris.Andreadis@jboss.org
 * @version $Revision:$
 */
public class FileURLConnectionTestCase extends TestCase
{
   public void testLastModified() throws Exception
   {
      File tmp = File.createTempFile("testLastModified", "");
      long lastModified = tmp.lastModified();
      System.out.println("Created file: " + tmp.getAbsolutePath() + ", lastModified:" + lastModified);
      
      // This will return JDK's provided FileURLConnection
      URL tmpURL = tmp.toURL();
      URLConnection tmpConn = tmpURL.openConnection();
      System.out.println("Got URLConnection of type: " + tmpConn.getClass().getName());
      assertEquals(lastModified, tmpConn.getLastModified());
      
      long lastModifiedHdr = tmpConn.getHeaderFieldDate("last-modified", 0);
      System.out.println("last-modified header: "+lastModifiedHdr);
      // the last-modified header is expected to strip the milliseconds to
      // comply with the (dd MMM yyyy HH:mm:ss) format, so the following assertions
      // is invalid on windows that provide millisecond accuracy to File.lastModified()
      // see, http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4504473
      // assertEquals(lastModified, lastModifiedHdr);
      
      // Test that JBoss FileURLConnection matches JDK's FileURLConnection results
       tmpConn = new FileURLConnection(tmpURL);
       tmpConn.connect();
       System.out.println("Got URLConnection of type: " + tmpConn.getClass().getName());
       assertEquals(lastModified, tmpConn.getLastModified());       
      
       lastModifiedHdr = tmpConn.getHeaderFieldDate("last-modified", 0);
       System.out.println("last-modified header: "+lastModifiedHdr);

       // cleanup
       tmp.delete();
   }

   public void testLength() throws Exception
   {
      File tmp = File.createTempFile("testLength", "");
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

      // cleanup
      tmp.delete();
   }
   
   public void testDirectoryList() throws Exception
   {
      // create a directory structure
      //    testDirectoryList####/
      //    +-test.txt
      //    \-test.dir/
      File rootDir = File.createTempFile("testDirectoryList", "");
      rootDir.delete();
      rootDir.mkdir();
      System.out.println(rootDir);
      File tmpFile = new File(rootDir, "test.txt");
      tmpFile.createNewFile();
      System.out.println(tmpFile);
      FileOutputStream fos = new FileOutputStream(tmpFile);
      fos.write("this is a test file".getBytes());
      fos.close();
      File tmpDir = new File(rootDir, "test.dir");
      tmpDir.mkdir();
      System.out.println(tmpDir);
      
      URL rootURL = rootDir.toURL();
      URLConnection rootConn = rootURL.openConnection();
      System.out.println("Got URLConnection of type: " + rootConn.getClass().getName());      
      BufferedReader in = new BufferedReader(new InputStreamReader(rootConn.getInputStream()));
      // verify we can read the sorted file list
      assertEquals("directory entry #1", "test.dir", in.readLine());
      assertEquals("directory entry #2", "test.txt", in.readLine());
      assertEquals("directory entry #3", null, in.readLine());
      in.close();
      
      // Test that JBoss FileURLConnection matches JDK's FileURLConnection results
      rootConn = new FileURLConnection(rootURL);
      rootConn.connect();
      System.out.println("Got URLConnection of type: " + rootConn.getClass().getName());
      in = new BufferedReader(new InputStreamReader(rootConn.getInputStream()));
      // verify we can read the sorted file list
      assertEquals("directory entry #1", "test.dir", in.readLine());
      assertEquals("directory entry #2", "test.txt", in.readLine());
      assertEquals("directory entry #3", null, in.readLine());
      in.close();      
      
      // cleanup
      Files.delete(rootDir);
   }
}

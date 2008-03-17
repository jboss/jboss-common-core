/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.test.util.test.xml.resolver;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.util.xml.JBossEntityResolver;
import org.xml.sax.InputSource;

import junit.framework.TestCase;


/**
 * A JBossEntityResolverUnitTestCase.
 * 
 * @author <a href="alex@jboss.com">Alexey Loubyansky</a>
 * @version $Revision: 1.1 $
 */
public class JBossEntityResolverUnitTestCase
   extends TestCase
{
   public JBossEntityResolverUnitTestCase(String arg0)
   {
      super(arg0);
   }

   /**
    * The spcial thing about the resolution of xsd:redefine is that
    * the parser passes the namespace of the redefining schema as publicId
    * and the schema location of the redefined schema as systemId. Now, if
    * the redefining schema's namespace has already been mapped
    * to a schema location of the redefining schema then schema location
    * argument is ignored and the redefining schema is returned instead of the
    * redefined schema.
    * 
    * @throws Exception
    */
   public void testResolveRedefine() throws Exception
   {
      String baseName = getRootName() + "_" + getName() + "_";
      String redefiningName = baseName + "redefining.xsd";
      InputStream redefiningStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(redefiningName);
      assertNotNull("Expected to find " + redefiningName + " in the classpath", redefiningStream);
      int redefiningSize = bytesTotal(redefiningStream);

      String redefinedName = baseName + "redefined.xsd";
      InputStream redefinedStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(redefinedName);
      assertNotNull("Expected to find " + redefinedName + " in the classpath", redefinedStream);
      int redefinedSize = bytesTotal(redefinedStream);
      
      assertTrue(redefiningSize != redefinedSize);

      JBossEntityResolver resolver = new JBossEntityResolver();
      resolver.registerLocalEntity("urn:jboss:xml:test", redefiningName);
      InputSource resolvedSource = resolver.resolveEntity("urn:jboss:xml:test", redefinedName);
      assertNotNull(resolvedSource);
      InputStream resolvedStream = resolvedSource.getByteStream();
      assertNotNull(resolvedStream);
      int resolvedSize = bytesTotal(resolvedStream);
      assertEquals("Schema sizes: redefined=" + redefinedSize + ", redefining=" + redefiningSize, redefinedSize, resolvedSize);
   }

   private int bytesTotal(InputStream redefinedStream) throws IOException
   {
      byte[] bytes = new byte[1024];
      int redefinedSize = 0;
      try
      {
         for(int i = 0; (i = redefinedStream.read(bytes)) > 0; redefinedSize += i);
      }
      finally
      {
         redefinedStream.close();
      }
      return redefinedSize;
   }

   protected String getRootName()
   {
      String longName = getClass().getName();
      int dot = longName.lastIndexOf('.');
      if (dot != -1)
         return longName.substring(dot + 1);
      return longName;
   }
}

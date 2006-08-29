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
package org.jboss.util.loading;

import java.security.ProtectionDomain;

/** An interface for transforming byte code before Class creation. This is
 * compatible with the JDK1.5 java.lang.instrument.ClassFileTransformer
 * proposal.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public interface Translator
{
   /** Optionally transform the supplied class file and return a new replacement
    * class file.
    *
    * <P> If a transformer has been registered with the class loading layer,
    * the transformer will be called for every new class definition.
    * The request for a new class definition is made with
    * {@link java.lang.ClassLoader#defineClass ClassLoader.defineClass}.
    * The transformer is called during the processing of the request, before
    * the class file bytes have been verified or applied.
    *
    * <P>
    * If the implementing method determines that no transformations are needed,
    * it should return <code>null</code>. Otherwise, it should create a new
    * byte[] array and copy the input <code>classfileBuffer</code> into it,
    * along with all desired transformations. The input <code>classfileBuffer</code>
    * must not be modified.
    *
    * @param loader - the defining loader of the class to be transformed, may
    *    be <code>null</code> if the bootstrap loader
    * @param className - the fully-qualified name of the class
    * @param classBeingRedefined - if this is a redefine, the class being
    *    redefined, otherwise <code>null</code>
    * @param protectionDomain - the protection domain of the class being
    *    defined or redefined
    * @param classfileBuffer - the input byte buffer in class file format - must
    *    not be modified
    *
    * @throws Exception - if the input does not represent a well-formed class file
    * @return a well-formed class file buffer (the result of the transform), 
    * or <code>null</code> if no transform is performed.
    */
   public byte[] transform(ClassLoader loader,
      String className,
      Class classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
      throws Exception;

   /** Called to indicate that the ClassLoader is being discarded by the server.
    * 
    * @param loader - a class loader that has possibly been used previously
    *    as an argument to transform.
    */ 
   public void unregisterClassLoader(ClassLoader loader);
}

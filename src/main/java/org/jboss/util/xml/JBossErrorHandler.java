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
package org.jboss.util.xml;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.jboss.logging.Logger;
import org.jboss.util.JBossStringBuilder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class JBossErrorHandler implements ErrorHandler, ErrorListener
{
   private static final Logger log =Logger.getLogger(JBossErrorHandler.class);
   
   // The xml file being parsed
   private String fileName;
   private JBossEntityResolver resolver;
   private boolean error;
   
   public JBossErrorHandler(String fileName, JBossEntityResolver resolver)
   {
      this.fileName = fileName;
      this.resolver = resolver;
      this.error = false;
   }
   
   public void error(SAXParseException e)
   {
      if (resolver == null || resolver.isEntityResolved())
      {
         error = true;
         log.error(formatError("error", e));
      }
   }
   
   public void fatalError(SAXParseException e)
   {
      if (resolver == null || resolver.isEntityResolved())
      {
         error = true;
         log.error(formatError("fatal", e));
      }
   }
   
   public void warning(SAXParseException e)
   {
      if (resolver == null || resolver.isEntityResolved())
      {
         error = true;
         log.error(formatError("warning", e));
      }
   }
   
   public void error(TransformerException e)
   {
      if (resolver == null || resolver.isEntityResolved())
      {
         error = true;
         log.error(formatError("error", e));
      }
   }
   
   public void fatalError(TransformerException e)
   {
      if (resolver == null || resolver.isEntityResolved())
      {
         error = true;
         log.error(formatError("fatal", e));
      }
   }
   
   public void warning(TransformerException e)
   {
      if (resolver == null || resolver.isEntityResolved())
      {
         error = true;
         log.error(formatError("warning", e));
      }
   }

   protected String formatError(String context, SAXParseException e)
   {
      JBossStringBuilder buffer = new JBossStringBuilder();
      buffer.append("File ").append(fileName);
      buffer.append(" process ").append(context);
      buffer.append(". Line: ").append(e.getLineNumber());
      buffer.append(". Error message: ").append(e.getMessage());
      return buffer.toString();
   }

   protected String formatError(String context, TransformerException e)
   {
      JBossStringBuilder buffer = new JBossStringBuilder();
      buffer.append("File ").append(fileName);
      buffer.append(" process ").append(context);
      buffer.append(". Location: ").append(e.getLocationAsString());
      buffer.append(". Error message: ").append(e.getMessage());
      return buffer.toString();
   }
   
   public boolean hadError()
   {
      return error;
   }
}

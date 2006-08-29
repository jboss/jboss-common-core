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
package org.jboss.util.property;

/**
 * The listener interface for receiving bound property events (as well as
 * property events).
 *
 * <p>Classes that are interested in processing a bound property event 
 *    implement this interface, and register instance objects with a given
 *    {@link PropertyMap} or via
 *    {@link PropertyManager#addPropertyListener(PropertyListener)}.
 *
 * <p>Note that this is not the typical listener interface, as it extends
 *    from {@link PropertyListener}, and defines {@link #getPropertyName()}
 *    which is not an event triggered method.  This method serves to instruct
 *    the {@link PropertyMap} the listener is registered with, which property
 *    it will bind to.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public interface BoundPropertyListener
   extends PropertyListener
{
   /**
    * Get the property name which this listener is bound to.
    *
    * @return  Property name.
    */
   String getPropertyName();

   /**
    * Notifies that this listener was bound to a property.
    *
    * @param map     <tt>PropertyMap</tt> which contains property bound to.
    */
   void propertyBound(PropertyMap map);

   /**
    * Notifies that this listener was unbound from a property.
    *
    * @param map     <tt>PropertyMap</tt> which contains property bound to.
    */
   void propertyUnbound(PropertyMap map);
}

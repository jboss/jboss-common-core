/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.util.timeout;

/**
 * TimeoutPriorityQueue.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface TimeoutPriorityQueue
{
   /**
    * Add a timeout to the queue
    * 
    * @param time the time of the timeout
    * @param target the timeout target
    * @return timeout when it was added to the queue, false otherwise
    */
   TimeoutExt offer(long time, TimeoutTarget target);
   
   /**
    * Take a timeout when it times out
    * 
    * @return the top the queue or null if the queue is cancelled
    */
   TimeoutExt take();
   
   /**
    * Retrieves and removes the top of the queue if it times out
    * or null if there is no such element
    * 
    * @return the top the queue or null if the queue is empty
    */
   TimeoutExt poll();
   
   /**
    * Retrieves and removes the top of the queue if it times out
    * or null if there is no such element
    * 
    * @param wait how to long to wait in milliseconds
    *        if the queue is empty
    * @return the top of the queue or null if the queue is empty
    */
   TimeoutExt poll(long wait);
   
   /**
    * Retrieves but does not remove the top of the queue
    * or null if there is no such element
    * 
    * @return the top of the queue or null if the queue is empty
    */
   TimeoutExt peek();
   
   /**
    * Removes the passed timeout from the queue
    * 
    * @return true when the timeout was removed
    */
   boolean remove(TimeoutExt timeout);
   
   /**
    * Clears the queue
    */
   void clear();
   
   /**
    * Cancels the queue
    */
   void cancel();
   
   /**
    * The size of the queue
    */
   int size();
}

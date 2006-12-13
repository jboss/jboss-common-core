/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.util.graph;

/**
 * A directed, weighted edge in a graph
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class Edge<T>
{
   private Vertex<T> from;
   private Vertex<T> to;
   private int cost;
   private boolean mark;

   // Create an edge with 0 cost
   public Edge(Vertex<T> one, Vertex<T> two)
   {
      from = one;
      to = two;
      cost = 0;
      mark = false;
   }

   // Create an edge and define the cost
   public Edge(Vertex<T> one, Vertex<T> two, int c)
   {
      from = one;
      to = two;
      cost = c;
      mark = false;
   }

   public Vertex<T> getTo()
   {
      return to;
   }

   public Vertex<T> getFrom()
   {
      return from;
   }

   public int getCost()
   {
      return cost;
   }

   // Mark an edge
   public void mark()
   {
      mark = true;
   }

   // Clear the mark
   public void clearMark()
   {
      mark = false;
   }

   // Test the mark
   public boolean isMarked()
   {
      return mark;
   }

   public String toString()
   {
      StringBuffer tmp = new StringBuffer("Edge[from: ");
      tmp.append(from.getName());
      tmp.append(",to: ");
      tmp.append(to.getName());
      tmp.append(", cost: ");
      tmp.append(cost);
      tmp.append("]");
      return tmp.toString();
   }
}

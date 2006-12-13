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

import java.util.ArrayList;

/**
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class Vertex<T>
{
   private ArrayList<Edge<T>> incomingEdges;
   private ArrayList<Edge<T>> outgoingEdges;
   private String name;
   private boolean mark;
   private int markState;
   private T data;

   /**
    * Calls this(null, null).
    */ 
   public Vertex()
   {
      this(null, null);
   }
   /**
    * Create a vertex with the given name
    * @param n
    */ 
   public Vertex(String n)
   {
      this(null, null);
   }
   public Vertex(String n, T data)
   {
      incomingEdges = new ArrayList<Edge<T>>();
      outgoingEdges = new ArrayList<Edge<T>>();
      name = new String(n);
      mark = false;
      this.data = data;
   }

   public String getName()
   {
      return name;
   }

   /**
    * @return Returns the data.
    */
   public T getData()
   {
      return this.data;
   }
   /**
    * @param data The data to set.
    */
   public void setData(T data)
   {
      this.data = data;
   }

   /**
    * 
    * @param e
    * @return true if the edge was added, false otherwise
    */ 
   public boolean addEdge(Edge<T> e)
   {
      if (e.getFrom() == this)
         outgoingEdges.add(e);
      else if (e.getTo() == this)
         incomingEdges.add(e);
      else 
         return false;
      return true;
   }
   
   /**
    * 
    * @param e
    * @return
    */ 
   public boolean hasEdge(Edge<T> e)
   {
      if (e.getFrom() == this)
         return incomingEdges.contains(e);
      else if (e.getTo() == this)
         return outgoingEdges.contains(e);
      else 
         return false;
   }
   
   // Remove an edge from this vertex
   public boolean remove(Edge<T> e)
   {
      if (e.getFrom() == this)
         incomingEdges.remove(e);
      else if (e.getTo() == this)
         outgoingEdges.remove(e);
      else 
         return false;
      return true;
   }
   
   public int getIncomingEdgeCount()
   {
      return incomingEdges.size();
   }
   
   public Edge<T> getIncomingEdge(int i)
   {
      Edge<T> e = incomingEdges.get(i);
      return e;
   }

   public int getOutgoingEdgeCount()
   {
      return outgoingEdges.size();
   }
   public Edge<T> getOutgoingEdge(int i)
   {
      Edge<T> e = outgoingEdges.get(i);
      return e;
   }
   
   // Do we have an edge that goes to dest?
   public Edge<T> findEdge(Vertex<T> dest)
   {
      for (int i = 0; i < incomingEdges.size(); i++)
      {
         Edge<T> e = incomingEdges.get(i);
         if (e.getTo() == dest)
            return e;
      }
      return null;
   }  
   
   // Do we have the edge e?  Only looks at sucessors
   public Edge<T> findEdge(Edge<T> e)
   {
      if (incomingEdges.contains(e))
         return e;
      else
         return null;
   }  

   /**
    * What is the cost to this vertex.
    * Return Integer.MAX_VALUE if we have no edge to dest
    * @param dest
    * @return
    */ 
   public int cost(Vertex<T> dest)
   {
      if (dest == this)
         return 0;
         
      Edge<T> e = findEdge(dest);
      if (e != null)
         return e.getCost();
      else
         return Integer.MAX_VALUE;
   }
      
   // Do we have an edge to dest?
   public boolean hasEdge(Vertex<T> dest)
   {
      return (findEdge(dest) != null);
   }
   
   // Have we been here before?
   public boolean visited()
   {
      return mark;
   }
   
   public void mark()
   {
      mark = true;
   }
   public void setMarkState(int state)
   {
      markState = state;
   }
   public int getMarkState()
   {
      return markState;
   }

   public void visit()
   {
      mark();
   }
   
   // Clear the mark
   public void clearMark()
   {
      mark = false;
   }
   
   public String toString()
   {
      StringBuffer tmp = new StringBuffer("Vertex(");
      tmp.append(name);
      tmp.append("), in:[");
      for (int i = 0; i < incomingEdges.size(); i++)
      {
         Edge<T> e = incomingEdges.get(i);
         if( i > 0 )
            tmp.append(',');
         tmp.append('{');
         tmp.append(e.getFrom().name);
         tmp.append(',');
         tmp.append(e.getCost());
         tmp.append('}');
      }
      tmp.append("], out:[");
      for (int i = 0; i < outgoingEdges.size(); i++)
      {
         Edge<T> e = outgoingEdges.get(i);
         if( i > 0 )
            tmp.append(',');
         tmp.append('{');
         tmp.append(e.getTo().name);
         tmp.append(',');
         tmp.append(e.getCost());
         tmp.append('}');
      }
      tmp.append(']');
      return tmp.toString();
   }
}

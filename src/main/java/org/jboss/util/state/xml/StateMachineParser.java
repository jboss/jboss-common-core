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
package org.jboss.util.state.xml;

import java.net.URL;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;

import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import org.jboss.logging.Logger;
import org.jboss.util.state.StateMachine;
import org.jboss.util.state.State;
import org.jboss.util.state.Transition;

/** Parse an xml representation of a state machine. A sample document is:
 
<state-machine description="JACC PolicyConfiguration States">
   <state name="open">
      <transition name="inService" target="open" />
      <transition name="getContextID" target="open" />
      <transition name="getPolicyConfiguration" target="open" />
      <transition name="addToRole" target="open" />
      <transition name="removeRole" target="open" />
      <transition name="addToExcludedPolicy" target="open" />
      <transition name="removeExcludedPolicy" target="open" />
      <transition name="addToUncheckedPolicy" target="open" />
      <transition name="removeUncheckedPolicy" target="open" />
      <transition name="linkConfiguration" target="open" />
      <transition name="commit" target="inService" />
      <transition name="delete" target="deleted" />
   </state>
   <state name="inService">
      <transition name="getPolicyConfiguration" target="open" />
      <transition name="getContextID" target="inService" />
      <transition name="inService" target="inService" />
      <transition name="delete" target="deleted" />
   </state>
   <state name="deleted" isStartState="true">
      <transition name="getPolicyConfiguration" target="open" />
      <transition name="delete" target="deleted" />      
      <transition name="inService" target="deleted" />
      <transition name="getContextID" target="deleted" />
   </state>
</state-machine>

 @author Scott.Stark@jboss.org
 @version $Revision$
 */
public class StateMachineParser
{
   private static Logger log = Logger.getLogger(StateMachineParser.class);

   /**
    * 
    * @param source
    * @return
    * @throws DocumentException
    */ 
   public StateMachine parse(URL source) throws DocumentException
   {
      SAXReader reader = new SAXReader();
      Document document = reader.read(source);
      Element root = document.getRootElement();
      String description = root.attributeValue("description");
      Iterator i = root.elementIterator();
      HashMap nameToStateMap = new HashMap();
      HashMap nameToTransitionsMap = new HashMap();
      HashSet states = new HashSet();
      State startState = null;
      while( i.hasNext() )
      {
         Element stateElement = (Element) i.next();
         String stateName = stateElement.attributeValue("name");
         State s = new State(stateName);
         states.add(s);
         nameToStateMap.put(stateName, s);
         HashMap transitions = new HashMap();
         Iterator it = stateElement.elementIterator();
         while( it.hasNext() )
         {
            Element transElement = (Element) it.next();
            String name = transElement.attributeValue("name");
            String targetName = transElement.attributeValue("target");
            transitions.put(name, targetName);
         }
         nameToTransitionsMap.put(stateName, transitions);
         if( Boolean.valueOf(stateElement.attributeValue("isStartState")) == Boolean.TRUE )
            startState = s;
      }

      // Resolve all transition targets
      Iterator transitions = nameToTransitionsMap.keySet().iterator();
      StringBuffer resolveFailed = new StringBuffer();
      while( transitions.hasNext() )
      {
         String stateName = (String) transitions.next();
         State s = (State) nameToStateMap.get(stateName);
         HashMap stateTransitions = (HashMap) nameToTransitionsMap.get(stateName);
         Iterator it = stateTransitions.keySet().iterator();
         while( it.hasNext() )
         {
            String name = (String) it.next();
            String targetName = (String) stateTransitions.get(name);
            State target = (State) nameToStateMap.get(targetName);
            if( target == null )
            {
               String msg = "Failed to resolve target state: "+targetName+" for transition: "+name;
               resolveFailed.append(msg);
               log.debug(msg);
            }
            Transition t = new Transition(name, target);
            s.addTransition(t);
         }
      }

      if( resolveFailed.length() > 0 )
         throw new DocumentException("Failed to resolve transition targets: "+resolveFailed);

      StateMachine sm = new StateMachine(states, startState, description);
      return sm;
   }
}

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
package org.jboss.util.id;

/**
 * A globally unique identifier (globally across a cluster of virtual 
 * machines).
 *
 * <p>The identifier is composed of:
 * <ol>
 *    <li>The VMID for the virtual machine.</li>
 *    <li>A UID to provide uniqueness over a VMID.</li>
 * </ol>
 *
 * <pre>
 *    [ address ] - [ process id ] - [ time ] - [ counter ] - [ time ] - [ counter ]
 *                                   |------- UID --------|   |------- UID --------|
 *    |---------------------- VMID -----------------------|
 * </pre>
 *
 * @see VMID
 * @see UID
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GUID
   implements ID, Comparable
{
   /** The serial version id, @since 1.6 */
   static final long serialVersionUID = 3289509836244263718L;
   /** The virtual machine identifier */
   protected final VMID vmid;

   /** The unique identifier */
   protected final UID uid;

   /** The hash code of this GUID */
   protected final int hashCode;

   /**
    * Construct a new GUID.
    */
   public GUID() {
      this.vmid = VMID.getInstance();
      this.uid = new UID();

      // generate a hash code for this GUID
      int code = vmid.hashCode();
      code ^= uid.hashCode();
      hashCode = code;
   }

   /**
    * Copy a GUID.
    *
    * @param guid    GUID to copy.
    */
   protected GUID(final GUID guid) {
      this.vmid = guid.vmid;
      this.uid = guid.uid;
      this.hashCode = guid.hashCode;
   }

   /**
    * Get the VMID portion of this GUID.
    *
    * @return  The VMID portion of this GUID.
    */
   public final VMID getVMID() {
      return vmid;
   }

   /**
    * Get the UID portion of this GUID.
    *
    * @return  The UID portion of this GUID.
    */
   public final UID getUID() {
      return uid;
   }
   
   /**
    * Return a string representation of this GUID.
    *
    * @return  A string representation of this GUID.
    */
   public String toString() {
      return vmid.toString() + "-" + uid.toString();
   }

   /**
    * Return the hash code of this GUID.
    *
    * @return  The hash code of this GUID.
    */
   public int hashCode() {
      return hashCode;
   }

   /**
    * Check if the given object is equal to this GUID.
    *
    * <p>A GUID is equal to another GUID if the VMID and UID portions are
    *    equal.
    *
    * @param obj  Object to test equality with.
    * @return     True if object is equal to this GUID.
    */
   public boolean equals(final Object obj) {
      if (obj == this) return true;

      if (obj != null && obj.getClass() == getClass()) {
         GUID guid = (GUID)obj;

         return 
            guid.vmid.equals(vmid) &&
            guid.uid.equals(uid);
      }

      return false;
   }

   /**
    * Returns a copy of this GUID.
    *
    * @return  A copy of this GUID.
    */
   public Object clone() {
      try {
         return super.clone();
      }
      catch (CloneNotSupportedException e) {
         throw new InternalError();
      }
   }

   /**
    * Returns a GUID as a string.
    *
    * @return  GUID as a string.
    */
   public static String asString() {
      return new GUID().toString();
   }

   public int compareTo(Object o)
   {
      GUID guid = (GUID)o;
      return this.toString().compareTo(guid.toString());
   }
   
}

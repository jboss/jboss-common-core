package org.jboss.test.util.test.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.jboss.util.stream.MarshalledValueInputStream;
import org.jboss.util.stream.MarshalledValueOutputStream;

/**
 * Test MarshalledValueInput/OutputStream 
 *
 * @see org.jboss.util.propertyeditor.PropertyEditors
 * 
 * @author Jason.Greene@jboss.org 
 * @version $Revision$
 */
public class MarshallValueStreamTestCase extends TestCase
{
   public void testArrayMarshall() throws Exception
   {
      Byte[] bytes = new Byte[] {1, 2, 3};
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      MarshalledValueOutputStream os = new MarshalledValueOutputStream(baos);
      os.writeObject(bytes);
      os.flush();
      MarshalledValueInputStream is = new MarshalledValueInputStream(new ByteArrayInputStream(baos.toByteArray())); 
      assertTrue(Arrays.equals(bytes, (Byte[]) is.readObject()));
   }

   public void testPrimitive() throws Exception
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(int.class);
      oos.close();
      baos.close();
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      MarshalledValueInputStream mvis = new MarshalledValueInputStream(bais);
      Object o = mvis.readObject();
      assertNotNull(o);
      assertEquals(o, int.class);
   }
}

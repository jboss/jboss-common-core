package org.jboss.test.util.test.collection;

import java.util.Set;

import org.jboss.util.collection.WeakSet;

/**
 * Unit tests for WeakSet
 * 
 * @author <a href="mailto:sven@meiers.net">Sven Meier</a> 
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @version $Revision: 43534 $
 */
@SuppressWarnings("unchecked")
public class WeakSetUnitTestCase extends AbstractNullableSetUnitTest
{
   protected Set createSet()
   {
      return new WeakSet();
   }
}

package edu.ucsb.cs156.happiercows.jobs;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import edu.ucsb.cs156.happiercows.helpers.ReportCSVHelper;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class ReportCSVHelperTests {
    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<ReportCSVHelper> constructor = ReportCSVHelper.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()), "Constructor is not private");
        constructor.setAccessible(true);
        ReportCSVHelper instance = constructor.newInstance();
        assertEquals(ReportCSVHelper.class, instance.getClass(), "Unexpected instance type");
    }


}

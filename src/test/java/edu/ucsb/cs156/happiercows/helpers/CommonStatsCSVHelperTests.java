package edu.ucsb.cs156.happiercows.helpers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonStatsCSVHelperTests {
    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<CommonStatsCSVHelper> constructor = CommonStatsCSVHelper.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()), "Constructor is not private");
        constructor.setAccessible(true);
        CommonStatsCSVHelper instance = constructor.newInstance();
        assertEquals(CommonStatsCSVHelper.class, instance.getClass(), "Unexpected instance type");
    }

}

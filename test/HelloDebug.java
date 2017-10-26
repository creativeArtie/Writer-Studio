import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableListMultimap;

@RunWith(Parameterized.class)
public class HelloDebug{

     @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {0}, {1}, {2}
        });
    }
    
    private int index;
    
    public HelloDebug(int i){
        index = i;
    }
    
    @BeforeClass
    public static void beforeClass(){
       // System.out.println("BeforeClass");
    }
    
    @Before
    public void before(){
        //System.out.println("Before[" + index + "]");
    }
    
    @Test
    public void test1(){
        //System.out.println("Test1[" + index + "]");
    }
    
    @Test
    public void test2(){
        //System.out.println("Test2[" + index + "]");
    }
    
    @After
    public void after(){
        //System.out.println("After[" + index + "]");
    }
    
    @AfterClass
    public static void afterClass(){
        //System.out.println("AfterClass");
    }
}

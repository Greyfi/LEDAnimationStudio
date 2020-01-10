package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LEDTest {
    LED ana;
    LED dig;

    @BeforeEach
    public void before()
    {
        ana = new LED(0, 0, true);
        dig = new LED(0, 0, false);
    }

    //This test may expire in future development
    //Test if binary insertion is functioning properly
    @Test
    public void testBinaryInsertion()
    {
//        ana.createNode(100, 255, true);
//        ana.createNode(1000, 255, true);
//        ana.createNode(200, 255, true);
//        ana.createNode(900, 255, true);
//        //ana.createNode(899, 255, true);
//        ana.createNode(300, 255, true);
//        ana.createNode(800, 255, true);
//        ana.createNode(400, 255, true);
//        ana.createNode(700, 255, true);
//        ana.createNode(500, 255, true);
//        ana.createNode(600, 255, true);
//        ana.createNode(1100, 255, true);
//        ana.createNode(2000, 255, true);
//        ana.createNode(1200, 255, true);
//        ana.createNode(1900, 255, true);
//        //ana.createNode(899, 255, true);
//        ana.createNode(1300, 255, true);
//        ana.createNode(1800, 255, true);
//        ana.createNode(1400, 255, true);
//        ana.createNode(1700, 255, true);
//        ana.createNode(1500, 255, true);
//        ana.createNode(1600, 255, true);
        int x = 100000000;
        //+100 because there is an overlapping error within 1 element in the middle due to the range provided by bounce back[due to 0 being the initial multiplier]. (Would throw an exception generally)
        for(int i = 0; (i*100)+100 <= x; i++)
        {
            if(i%2 == 1)
            {
                ana.createNode(x-(((i/2)+1)*100), 255, true);
            }
            else
            {
                ana.createNode(((i/2))*100, 255, true);
            }
        }
        int count = 0;
        for(Node n: ana)
        {
            count++;
            System.out.println(n.getStartTime());
        }
        System.out.println("count: " + count);
    }

    @Test
    public void testBinarySearch()
    {
        System.out.println();
        int x = 10000000;
        //+100 because there is an overlapping error within 1 element in the middle due to the range provided by bounce back[due to 0 being the initial multiplier]. (Would throw an exception generally)
        for(int i = 0; (i*100)+100 <= x; i++)
        {
            if(i%2 == 1)
            {
                ana.createNode(x-(((i/2)+1)*100), 255, true);
            }
            else
            {
                ana.createNode(((i/2))*100, 255, true);
            }
        }
        //There's an error range of 1-2 near the index of Nodes bunched together, by 1-2 MS (Not a big deal?)
        System.out.println(ana.getSelection(20498).getStartTime());
    }
}

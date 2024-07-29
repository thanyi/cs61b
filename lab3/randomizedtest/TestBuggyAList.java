package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> aListNoResizing = new AListNoResizing<>();
        BuggyAList<Integer> buggyAList = new BuggyAList<>();

        /* 向两个List添加相同的值 */
        aListNoResizing.addLast(4);
        aListNoResizing.addLast(5);
        aListNoResizing.addLast(6);

        buggyAList.addLast(4);
        buggyAList.addLast(5);
        buggyAList.addLast(6);

        /* 检查两个list中的remove的值 */
        int aListNoResizingItem1 = aListNoResizing.removeLast();
        int aListNoResizingItem2 = aListNoResizing.removeLast();
        int aListNoResizingItem3 = aListNoResizing.removeLast();

        int buggyAListItem1 = buggyAList.removeLast();
        int buggyAListItem2 = buggyAList.removeLast();
        int buggyAListItem3 = buggyAList.removeLast();

        assertTrue("the first remove are not the same!",aListNoResizingItem1 == buggyAListItem1);
        assertTrue("the second remove are not the same!",aListNoResizingItem2 == buggyAListItem2);
        assertTrue("the third remove are not the same!",aListNoResizingItem3 == buggyAListItem3);



    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L1 = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 5);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L1.addLast(randVal);
                L2.addLast(randVal);
//                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size1 = L1.size();
                int size2 = L2.size();
//                System.out.println("size: " + size1);

            }else if (operationNumber == 2) {
                // getLast
                if(L1.size() != 0 && L2.size() != 0)
                {
                    int last1 = L1.getLast();
                    int last2 = L2.getLast();
//                    System.out.println("item: " + last1 + " and "+ last2);
                    assertTrue("the getlast are not the same!",last1 == last2);
                }


            }else if (operationNumber == 3) {
                // removelast
                if (L1.size() != 0 && L2.size() != 0)
                {
                    L1.removeLast();
                    L2.removeLast();
//                    System.out.println("remove: " + L1.size());
                }
            }
        }
    }

}

package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        /* 开始对每一种次数进行次数的添加 */
        for (int i = 1000; i <= 128000; i = i*2 ) {
            SLList<Integer> testSLList = new SLList<>();

            /* 初始化List，以免计算时间包括初始化时间 */
            for (int j = 0; j < i; j++) {
                testSLList.addLast(10);
            }

            /* 运算计时 */
            Stopwatch sw1 = new Stopwatch();
            for (int j = 0; j < 10000; j++) {
                testSLList.addLast(10);
            }
            double timeInSeconds = sw1.elapsedTime();

            /* 添加进入第一列 */
            Ns.addLast(i);

            /* 添加进入第二列 */
            times.addLast(timeInSeconds);

            /* 添加进入第三列 */
            opCounts.addLast(10000);

        }

        printTimingTable(Ns,times,opCounts);


    }

}

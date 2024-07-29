package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        /* 开始对每一种次数进行次数的添加 */
        for (int i = 1000; i <= Math.pow(2,23); i = i*2 ) {
            AList<Integer> testAList = new AList<>();

            /* 运算计时 */
            Stopwatch sw1 = new Stopwatch();
            for (int j = 0; j < i; j++) {
                testAList.addLast(10);
            }
            double timeInSeconds = sw1.elapsedTime();

            /* 添加进入第一列 */
            Ns.addLast(i);

            /* 添加进入第二列 */
            times.addLast(timeInSeconds);

            /* 添加进入第三列 */
            opCounts.addLast(i);

        }

        printTimingTable(Ns,times,opCounts);


    }
}

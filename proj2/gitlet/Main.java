package gitlet;

import static gitlet.Repository.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // check if args is empty.
        checkArgsEmpty(args);
        String firstArg = args[0];

        switch(firstArg) {
            case "init":
                // 如果存在参数数量上的错误
                if (args.length != 1){
                    System.out.println("Incorrect operands.");
                }
                initPersistence();
                break;
            case "add":
                // TODO: handle the `add [filename]` command\
                String addFileName = args[1];
                addStage(addFileName);
                break;
            case "commit":
                String commitMsg = args[1];


            // TODO: FILL THE REST IN
        }
    }
}

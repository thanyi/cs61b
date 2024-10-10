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
                //
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
                commitFile(commitMsg);
                break;
            case "rm":
                String removeFile = args[1];
                removeStage(removeFile);
                break;
            case "log":
                if (args.length != 1){
                    System.out.println("Incorrect operands.");
                }
                printLog();
                break;
            case "global-log":
                if (args.length != 1){
                    System.out.println("Incorrect operands.");
                }
                printGlobalLog();
                break;
            case "find":
                String findMsg = args[1];
                findCommit(findMsg);
                break;
            case "status":
                if (args.length != 1){
                    System.out.println("Incorrect operands.");
                }
//                findCommit(findMsg);
                showStatus();
                break;

            case "checkout":
                if (args.length == 1){
                    System.out.println("Incorrect operands.");
                }
                checkOut(args);
                break;
            // TODO: FILL THE REST IN
        }
    }
}

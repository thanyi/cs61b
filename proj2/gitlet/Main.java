package gitlet;

import static gitlet.Repository.checkArgsEmpty;
import static gitlet.Repository.initPersistence;

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
                // TODO: handle the `init` command
                initPersistence();
                break;
            case "add":
                // TODO: handle the `add [filename]` command\
                String addFileName = args[1];

                break;
            // TODO: FILL THE REST IN
        }
    }
}

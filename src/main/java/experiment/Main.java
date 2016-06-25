package experiment;

import gui.Window;
import tea.TEAManager;
import md5.MD5Manager;
import mersenne.MersenneManager;
import quicksort.QuickSortManager;
import sudoku.SudokuManager;
import zip.ZipManager;

/**
 * Created by bdanglot on 02/05/16.
 */
public class Main {

    public static Manager manager;

    public static int numberOfTask = 20;

    public static int sizeOfTask = 100;

    public static Integer seed = 23;

    public static String typePerturbed = "Numerical";

    public static int numberOfSecondsToWait = 20;

    public static boolean verbose = false;

    public static void main(String[] args) {

        runGui();

//        if (getIndexOfOption("-help", args) != -1 || getIndexOfOption("-h", args) != -1)
//            usage();
//
//        if (getIndexOfOption("-v", args) != -1)
//            verbose = true;
//
//        int currentIndex = -1;
//        if ((currentIndex = getIndexOfOption("-time", args)) != -1) {
//            try {
//                numberOfSecondsToWait = Integer.parseInt(args[currentIndex + 1]);
//            } catch (NumberFormatException e) {
//                System.err.println("Time specified must be an integer.");
//                usage();
//            }
//        }
//
//        if ((currentIndex = getIndexOfOption("-size", args)) != -1) {
//            try {
//                sizeOfTask = Integer.parseInt(args[currentIndex + 1]);
//            } catch (NumberFormatException e) {
//                System.err.println("Size specified must be an integer.");
//                usage();
//            }
//        }
//
//        if ((currentIndex = getIndexOfOption("-nb", args)) != -1) {
//            try {
//                numberOfTask = Integer.parseInt(args[currentIndex + 1]);
//            } catch (NumberFormatException e) {
//                System.err.println("Number of Task specified must be an integer.");
//                usage();
//            }
//        }
//
//        if ((currentIndex = getIndexOfOption("-type", args)) != -1) {
//            if (args[currentIndex + 1].equals("Numerical") || args[currentIndex + 1].equals("Boolean"))
//                typePerturbed = args[currentIndex + 1];
//            else
//                usage();
//        }
//
//        if ((currentIndex = getIndexOfOption("-s", args)) != -1) {
//            buildSubject(currentIndex + 1, args);
//        }
//
//       if ((currentIndex = getIndexOfOption("-run", args)) != -1) {
//            run(currentIndex + 1, args);
//       } else
//           usage();

    }

    private static void run(int index, String[] args) {
        switch (args[index]) {
            case "gui":
                runGui();
                break;
            default:
                usage();
        }
        System.exit(0);
    }

    private static void runGui() {
        Window.launch();
    }

    private static void buildSubject(int index, String[] args) {
        switch (args[index]) {
            case "zip":
                manager = new ZipManager(numberOfTask, sizeOfTask, seed);
                break;
            case "mersenne":
            case "mt":
                manager = new MersenneManager(numberOfTask, sizeOfTask, seed);
                break;
            case "md5":
                manager = new MD5Manager(numberOfTask, sizeOfTask, seed);
                break;
            case "sudoku":
                manager = new SudokuManager(numberOfTask, sizeOfTask, seed);
                break;
            case "tea":
                manager = new TEAManager(numberOfTask, sizeOfTask, seed);
                break;
            default:
            case "qs":
            case "quicksort":
                manager = new QuickSortManager(numberOfTask, sizeOfTask, seed);
                break;
        }
    }

    private static int getIndexOfOption(String opt, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(opt))
                return i;
        }
        return -1;
    }

    public static void usage() {
        System.out.println("This is the light version for jsweet : options available : ");
        System.out.println("\t-run gui to run the GUI");
        System.out.println("\t-exp <subject>, with ");
        System.out.println("\t<subject> : quicksort tea sudoku mersenne zip md5");
        System.exit(-1);
    }


}
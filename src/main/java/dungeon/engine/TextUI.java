package dungeon.engine;
import java.util.Scanner;

public class TextUI
{
    private GameEngine engine;
    private Scanner scanner;
    private boolean isRunning;

    public TextUI(GameEngine engine)
    {
        this.engine = engine;
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    public void start()
    {
        printWelcome();
        displayGrid();
        gameLoop();
    }

    private void printWelcome()
    {
        System.out.println("Welcome to MiniDungeon!");
        System.out.println("Move with UP, DOWN, LEFT, RIGHT");
        System.out.println("Type HELP for commands, QUIT to exit");
        System.out.println("----------------------------------------");
    }

    private void gameLoop()
    {
        while (isRunning)
        {
            System.out.print("Enter command (UP/DOWN/LEFT/RIGHT/HELP/QUIT): ");
            String command = scanner.nextLine().toUpperCase();
            processCommand(command);
        }
        scanner.close();
    }

    private void displayGrid()
    {
        Cell[][] map = engine.getMap();
        int size = engine.getSize();

        //Print top border
        System.out.println("-".repeat(size * 2 + 3));

        // Print each row
        for (int i = 0; i < size; i++) {
            System.out.print("| "); // Left border
            for (int j = 0; j < size; j++) {
                char symbol = getSymbolForEntity(map[i][j].getEntityType());
                System.out.print(symbol + " ");
            }
            System.out.println("|"); // Right border
        }

        //Print bottom border
        System.out.println("-".repeat(size * 2 + 3));
    }


    private void processCommand(String command)
    {
        switch (command)
        {
            case "UP":
                System.out.println("Moving up");
                break;
            case "DOWN":
                System.out.println("Moving down");
                break;
            case "LEFT":
                System.out.println("Moving left");
                break;
            case "RIGHT":
                System.out.println("Moving right");
                break;
            case "HELP":
                printHelp();
                break;
            case "QUIT":
                isRunning = false;
                System.out.println("Thanks for playing!");
                return; //Don't display map after quitting
            default:
                System.out.println("Unknown command. Type HELP for commands.");
        }
        displayGrid(); //Display map after each command
    }

    private void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("UP     - Move up");
        System.out.println("DOWN   - Move down");
        System.out.println("LEFT   - Move left");
        System.out.println("RIGHT  - Move right");
        System.out.println("HELP   - Show this help");
        System.out.println("QUIT   - Exit the game\n");
    }

    private char getSymbolForEntity(EntityType type)
    {
        return switch (type)
        {
            case ENTRY -> 'E';
            case EXIT -> 'L';
            case PLAYER -> 'P';
            case WALL -> '#';
            case TRAP -> 'T';
            case GOLD -> 'G';
            case MELEE_MUTANT -> 'M';
            case RANGED_MUTANT -> 'R';
            case HEALTH_POTION -> 'H';
            case EMPTY -> '.';
        };
    }
}
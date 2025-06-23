package dungeon.engine;

public class CellFactory
{
    public static Cell createCell(String type)
    {
        return switch (type)
        {
            case "EMPTY" -> new EmptyCell();
            case "WALL" -> new WallCell();
            case "GOLD" -> new GoldCell();
            case "HEALTH_POTION" -> new HealthPotionCell();
            case "TRAP" -> new TrapCell();
            case "MELEE_MUTANT" -> new MeleeMutantCell();
            case "RANGED_MUTANT" -> new RangedMutantCell();
            case "PLAYER" -> new PlayerCell();
            case "ENTRY" -> new EntryCell();
            case "EXIT" -> new ExitCell();
            default -> new EmptyCell();
        };
    }
}

class EmptyCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        return false;
    }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    @Override
    public String getType()
    {
        return "EMPTY";
    }
}

class WallCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        return false;
    }

    @Override
    public boolean blocksMovement()
    {
        return true;
    }

    @Override
    public String getType()
    {
        return "WALL";
    }
}

class GoldCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        gameState.addScore(10);
        System.out.println("You found gold! +10 points!");
        return true;
    }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    @Override
    public String getType()
    {
        return "GOLD";
    }
}

class HealthPotionCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        int oldHP = gameState.getPlayerHP();
        gameState.changeHP(4);
        int healedAmount = gameState.getPlayerHP() - oldHP;
        System.out.println("You found a health potion! Healed for " + healedAmount + " HP!");
        return true;
    }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    @Override
    public String getType()
    {
        return "HEALTH_POTION";
    }
}

class TrapCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        gameState.changeHP(-1);
        System.out.println("Ouch! You stepped on a trap!");
        return true;
    }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    @Override
    public String getType()
    {
        return "TRAP";
    }
}

class MeleeMutantCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        gameState.changeHP(-2);
        gameState.addScore(2);
        System.out.println("You defeated a melee mutant! Took 2 damage but gained 2 points!");
        return true;
    }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    @Override
    public String getType()
    {
        return "MELEE_MUTANT";
    }
}

class RangedMutantCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        gameState.addScore(2);
        System.out.println("You defeated a ranged mutant! Gained 2 points!");
        return true;
    }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    @Override
    public String getType()
    {
        return "RANGED_MUTANT";
    }
}

class PlayerCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        return false;
    }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    @Override
    public String getType()
    {
        return "PLAYER";
    }
}

class EntryCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        return false;
    }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    @Override
    public String getType()
    {
        return "ENTRY";
    }
}

class ExitCell extends Cell
{
    @Override
    public boolean interact(GameState gameState)
    {
        return false;
    }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    @Override
    public String getType()
    {
        return "EXIT";
    }
}
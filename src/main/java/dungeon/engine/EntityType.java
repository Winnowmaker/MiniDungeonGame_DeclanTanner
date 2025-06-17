package dungeon.engine;

public enum EntityType
{
    // Basic entities
    EMPTY,
    WALL,
    ENTRY,
    EXIT,

    // Consumables
    GOLD,
    HEALTH_POTION,

    // Hazards
    TRAP,

    // Characters
    PLAYER,
    MELEE_MUTANT,
    RANGED_MUTANT;
}

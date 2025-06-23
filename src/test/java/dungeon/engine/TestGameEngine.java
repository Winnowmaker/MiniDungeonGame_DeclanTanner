package dungeon.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GameEngine(10);
    }

    @Test
    void testInitialState() {
        assertFalse(engine.isGameCompleted());
        assertEquals(10, engine.getSize());
        assertNotNull(engine.getGameState());
    }

    @Test
    void testMovePlayer() {
    // Test valid move - move DOWN instead of RIGHT since we know (0,1) is a wall
    assertTrue(engine.movePlayer("DOWN"));
    
    // Test moving into a wall
    assertTrue(engine.movePlayer("RIGHT")); // This should work from (1,0)
    assertFalse(engine.movePlayer("RIGHT")); // This should fail because of wall at (1,2)
    
    // Test moving out of bounds
    GameState.Position pos = engine.getGameState().getPlayerPosition();
    // Move to edge first
    while (pos.getRow() > 0) {
        engine.movePlayer("UP");
    }
    assertFalse(engine.movePlayer("UP")); // Should be at top edge
}

    @Test
    void testGameOver() {
        // Reduce HP to 0
        GameState gameState = engine.getGameState();
        gameState.changeHP(-10);

        // Try to move after game over
        assertFalse(engine.movePlayer("RIGHT"));
        assertTrue(engine.isGameCompleted());
    }
}
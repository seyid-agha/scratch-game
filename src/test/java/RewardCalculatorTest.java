import models.ConfigParser;
import models.GameConfiguration;
import models.TotalRewardCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RewardCalculatorTest {
    private TotalRewardCalculator calculator;
    private GameConfiguration config;

    private final int betAmount = 100;

    @BeforeEach
    public void setUp() throws Exception {
        config = ConfigParser.parseConfig("config.json");
        calculator = new TotalRewardCalculator(config);
    }

    //  1. Test Case: Same Symbol Win (same_symbol_3_times)
    @Test
    public void testSameSymbolThreeTimesWin() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "C", "D"},
                {"E", "F", "G"}
        };

        // Calculation:
        // A: 100 * 5 (symbol multiplier) * 1 (same_symbol_3_times) * 2 (same_symbol_horizontally) = 1000
        double expectedReward = 1000;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward for same_symbol_3_times is incorrect.");
    }

    //  2. Test Case: Linear Win (same_symbols_vertically)
    @Test
    public void testVerticalWin() {
        String[][] matrix = {
                {"B", "C", "A"},
                {"B", "D", "E"},
                {"B", "F", "G"}
        };

        // Calculation:
        // B: 100 * 3 (symbol multiplier) * 2 (same_symbols_vertically) = 600
        double expectedReward = 600;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward for vertical win is incorrect.");
    }

    //  3. Test Case: Bonus Multiplier (5x)
    @Test
    public void testBonusMultiplier() {
        String[][] matrix = {
                {"C", "F", "C"},
                {"5x", "F", "A"},
                {"C", "E", "E"}
        };

        // Calculation:
        // C: 100 * 2.5 (symbol multiplier) * 1 (same_symbol_3_times) = 250
        // Total reward after 5x bonus: 250 * 5 = 1250
        double expectedReward = 1250;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward with bonus multiplier is incorrect.");
    }

    //  4. Test Case: Flat Bonus (+1000)
    @Test
    public void testFlatBonus() {
        String[][] matrix = {
                {"A", "B", "+1000"},
                {"C", "D", "E"},
                {"F", "G", "H"}
        };

        // Calculation:
        // A: 100 * 5 (symbol multiplier), but no win combinations, so no reward from A.
        // Flat bonus: +1000
        double expectedReward = 1000;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward with flat bonus is incorrect.");
    }

    //  5. Test Case: Mixed Win with Flat Bonus and Multiplier
    @Test
    public void testMixedWin() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"+1000", "B", "10x"},
                {"C", "B", "B"}
        };

        // Calculation:
        // A: 100 * 5 (symbol multiplier) * 1 (same_symbol_3_times) * 2 (same_symbol_horizontally) = 1000
        // B: 100 * 3 (symbol multiplier) * 1 (same_symbol_3_times) = 300
        // Flat bonus: +1000
        // Bonus multiplier: 10x
        // Total reward: (1000 + 300) * 10 + 1000 = 14000
        double expectedReward = 14000;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward for mixed win scenario is incorrect.");
    }

    //  6. Test Case: No Win Scenario
    @Test
    public void testNoWin() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"D", "E", "F"},
                {"G", "H", "I"}
        };

        // No winning combinations, no bonuses.
        double expectedReward = 0;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward for no win scenario should be zero.");
    }


    @Test
    public void test4x4MultipleLinearWins() {
        String[][] matrix = {
                {"A", "A", "A", "A"},
                {"B", "C", "D", "E"},
                {"F", "F", "E", "C"},
                {"D", "E", "F", "B"}
        };

        // Winning Combinations:
        // A: same_symbol_4_times (1.5x), same_symbols_horizontally (2x)
        // F: same symbol 3 times
        // E: same symbol 3 times
        // Calculation: 100 * 5 (symbol multiplier) * 1.5 * 2 + 100 + 120 = 1720


        double expectedReward = 1720;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward for 4x4 multiple linear wins is incorrect.");
    }

    @Test
    public void test5x5OverlappingWinsAndBonuses() {
        String[][] matrix = {
                {"B", "B", "B", "+500", "10x"},
                {"C", "D", "E", "F", "A"},
                {"A", "A", "A", "C", "D"},
                {"F", "F", "F", "E", "E"},
                {"D", "E", "F", "C", "B"}
        };

//        B	4	same_symbol_4_times, same_symbols_horizontally	900
//        A	4	same_symbol_4_times, same_symbols_horizontally	1500
//        F	5	same_symbol_5_times, same_symbols_horizontally	does not count as the config file does not include those cells. So 200
//        C	3	same_symbol_3_times	250
//        E	4	same_symbol_4_times	180
//        D	3	same_symbol_3_times	200
//        Flat Bonus	+500	Added to total reward	500
//        Bonus Multiplier	10x	Multiplies total reward	Multiplied total
//        Final Total	-	Sum + Bonus Multiplier + Flat Bonus	32800

        double expectedReward = 32800;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward for 5x5 overlapping wins and bonuses is incorrect.");
    }

    @Test
    public void test4x4DiagonalWin() {
        String[][] matrix = {
                {"C", "B", "A", "D"},
                {"E", "C", "F", "G"},
                {"H", "I", "C", "J"},
                {"K", "L", "M", "C"}
        };

        // Winning Combinations:
        // C: same_symbol_4_times (1.5x), same_symbols_diagonally_left_to_right (5x)
        // Calculation: 100 * 2.5 (symbol multiplier) * 1.5 * 5 = 1875

        double expectedReward = 1875;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward for 4x4 diagonal win is incorrect.");
    }


    @Test
    public void test4x4MultipleBonusesAndMultipliers() {
        String[][] matrix = {
                {"A", "A", "A", "+500"},
                {"B", "C", "D", "E"},
                {"F", "5x", "E", "C"},
                {"+1000", "E", "F", "B"}
        };

        // Winning Combinations:
        // A: same_symbol_3_times (1x), same_symbols_horizontally (2x)
        // Calculation for A: 100 * 5 * 1 * 2 = 1000
        // E : same symbol 3 times(1x)
        //calculation for E : 100*1.2*5 = 120
        //
        // Flat Bonuses: +500, +1000 = 1500
        // Bonus Multiplier: 5x
        //
        // Total Reward = (120*5)+ (1000 * 5) + 1500 =600+  5000 + 1500 = 7100

        double expectedReward = 7100;
        double actualReward = calculator.calculateReward(matrix, betAmount);
        assertEquals(expectedReward, actualReward, "Expected reward for 4x4 multiple bonuses and multipliers is incorrect.");
    }


}

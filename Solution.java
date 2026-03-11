
public class Solution {

    private static final char UNKNOWN = 'x';
    private static final String NO_SOLUTION = "No solution";
    private static final String INFINITE_SOLUTIONS = "Infinite solutions";

    private record CumulativeValues(int xValue, int intValue) {}

    public String solveEquation(String equation) {
        int indexOfEqualsSign = equation.indexOf('=');
        CumulativeValues leftSide = extractCumulativeValuesForEquationSide(equation, 0, indexOfEqualsSign);
        CumulativeValues rightSide = extractCumulativeValuesForEquationSide(equation, 1 + indexOfEqualsSign, equation.length());

        int xValuesMovedToLeftSide = leftSide.xValue - rightSide.xValue;
        int intValuesMovedToRightSide = rightSide.intValue - leftSide.intValue;

        if (xValuesMovedToLeftSide != 0) {
            return UNKNOWN + "=" + (intValuesMovedToRightSide / xValuesMovedToLeftSide);
        }
        if (intValuesMovedToRightSide == 0) {
            return INFINITE_SOLUTIONS;
        }

        return NO_SOLUTION;
    }

    private static CumulativeValues extractCumulativeValuesForEquationSide(String equation, int startIndex, int endBoundary) {
        int xValue = 0;
        int intValue = 0;

        // If the side of the equation starts with '-' or '+' then jump to the next index.
        int index = (equation.charAt(startIndex) == '-' || equation.charAt(startIndex) == '+')
                    ? 1 + startIndex
                    : startIndex;

        while (index < endBoundary) {
            int operationSign = 1;
            if (index > startIndex && equation.charAt(index - 1) == '-') {
                operationSign = -1;
            }

            int[] pairExtractedValueUpdatedIndex = extractValue(equation, index);
            int value = pairExtractedValueUpdatedIndex[0];
            index = pairExtractedValueUpdatedIndex[1];

            if (index < endBoundary && equation.charAt(index) == UNKNOWN) {
                xValue += value * operationSign;
                index += 2;
            } else {
                intValue += value * operationSign;
                ++index;
            }
        }

        return new CumulativeValues(xValue, intValue);
    }

    private static int[] extractValue(String equation, int index) {
        int value = 0;
        while (index < equation.length() && isDigit(equation.charAt(index))) {
            value = 10 * value + equation.charAt(index) - '0';
            ++index;
        }
        // If 'x' is not preceded by any value, then set the value to '1'.
        value = (index > 0 && isDigit(equation.charAt(index - 1))) ? value : 1;
        return new int[]{value, index};
    }

    private static boolean isDigit(char current) {
        return current >= '0' && current <= '9';
    }
}


using System;

public class Solution
{
    private static readonly char UNKNOWN = 'x';
    private static readonly string NO_SOLUTION = "No solution";
    private static readonly string INFINITE_SOLUTIONS = "Infinite solutions";

    private record CumulativeValues(int xValue, int intValue) { }

    public string SolveEquation(string equation)
    {
        int indexOfEqualsSign = equation.IndexOf('=');
        CumulativeValues leftSide = ExtractCumulativeValuesForEquationSide(equation, 0, indexOfEqualsSign);
        CumulativeValues rightSide = ExtractCumulativeValuesForEquationSide(equation, 1 + indexOfEqualsSign, equation.Length);

        int xValuesMovedToLeftSide = leftSide.xValue - rightSide.xValue;
        int intValuesMovedToRightSide = rightSide.intValue - leftSide.intValue;

        if (xValuesMovedToLeftSide != 0)
        {
            return UNKNOWN + "=" + (intValuesMovedToRightSide / xValuesMovedToLeftSide);
        }
        if (intValuesMovedToRightSide == 0)
        {
            return INFINITE_SOLUTIONS;
        }

        return NO_SOLUTION;
    }

    private static CumulativeValues ExtractCumulativeValuesForEquationSide(string equation, int startIndex, int endBoundary)
    {
        int xValue = 0;
        int intValue = 0;

        // If the side of the equation starts with '-' or '+' then jump to the next index.
        int index = (equation[startIndex] == '-' || equation[startIndex] == '+')
                    ? 1 + startIndex
                    : startIndex;

        while (index < endBoundary)
        {
            int operationSign = 1;
            if (index > startIndex && equation[index - 1] == '-')
            {
                operationSign = -1;
            }

            int[] pairExtractedValueUpdatedIndex = ExtractValue(equation, index);
            int value = pairExtractedValueUpdatedIndex[0];
            index = pairExtractedValueUpdatedIndex[1];

            if (index < endBoundary && equation[index] == UNKNOWN)
            {
                xValue += value * operationSign;
                index += 2;
            }
            else
            {
                intValue += value * operationSign;
                ++index;
            }
        }

        return new CumulativeValues(xValue, intValue);
    }

    private static int[] ExtractValue(string equation, int index)
    {
        int value = 0;
        while (index < equation.Length && IsDigit(equation[index]))
        {
            value = 10 * value + equation[index] - '0';
            ++index;
        }
        // If 'x' is not preceded by any value, then set the value to '1'.
        value = (index > 0 && IsDigit(equation[index - 1])) ? value : 1;
        return [value, index];
    }

    private static bool IsDigit(char current)
    {
        return current >= '0' && current <= '9';
    }
}

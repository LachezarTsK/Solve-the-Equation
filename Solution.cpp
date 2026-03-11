
#include <array>
#include <vector>
#include <string>
#include <string_view>
using namespace std;

class Solution {

    struct CumulativeValues {
        int xValue{};
        int intValue{};
        CumulativeValues(int xValue, int intValue) :xValue{ xValue }, intValue{ intValue } {}
    };

    static const char UNKNOWN = 'x';
    inline static const string NO_SOLUTION = "No solution";
    inline static const string INFINITE_SOLUTIONS = "Infinite solutions";

public:
    string solveEquation(string equation) const {
        int indexOfEqualsSign = equation.find_first_of('=');
        CumulativeValues leftSide = extractCumulativeValuesForEquationSide(equation, 0, indexOfEqualsSign);
        CumulativeValues rightSide = extractCumulativeValuesForEquationSide(equation, 1 + indexOfEqualsSign, equation.length());

        int xValuesMovedToLeftSide = leftSide.xValue - rightSide.xValue;
        int intValuesMovedToRightSide = rightSide.intValue - leftSide.intValue;

        if (xValuesMovedToLeftSide != 0) {
            return string(1, UNKNOWN) + "=" + to_string(intValuesMovedToRightSide / xValuesMovedToLeftSide);
        }
        if (intValuesMovedToRightSide == 0) {
            return INFINITE_SOLUTIONS;
        }

        return NO_SOLUTION;
    }

private:
    static CumulativeValues extractCumulativeValuesForEquationSide(string_view equation, int startIndex, int endBoundary) {
        int xValue = 0;
        int intValue = 0;

        // If the side of the equation starts with '-' or '+' then jump to the next index.
        int index = (equation[startIndex] == '-' || equation[startIndex] == '+')
                    ? 1 + startIndex
                    : startIndex;

        while (index < endBoundary) {
            int operationSign = 1;
            if (index > startIndex && equation[index - 1] == '-') {
                operationSign = -1;
            }

            array<int, 2> pairExtractedValueUpdatedIndex = extractValue(equation, index);
            int value = pairExtractedValueUpdatedIndex[0];
            index = pairExtractedValueUpdatedIndex[1];

            if (index < endBoundary && equation[index] == UNKNOWN) {
                xValue += value * operationSign;
                index += 2;
            }
            else {
                intValue += value * operationSign;
                ++index;
            }
        }

        return  CumulativeValues(xValue, intValue);
    }

    static array<int, 2> extractValue(string_view equation, int index) {
        int value = 0;
        while (index < equation.length() && isDigit(equation[index])) {
                value = 10 * value + equation[index] - '0';
                ++index;
        }
        // If 'x' is not preceded by any value, then set the value to '1'.
        value = (index > 0 && isDigit(equation[index - 1])) ? value : 1;
        return  { value, index };
    }

    static bool isDigit(char current) {
        return current >= '0' && current <= '9';
    }
};

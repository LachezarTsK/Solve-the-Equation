
class Solution {

    private companion object {
        const val UNKNOWN = 'x'
        const val NO_SOLUTION = "No solution"
        const val INFINITE_SOLUTIONS = "Infinite solutions"
    }

    private data class CumulativeValues(val xValue: Int, val intValue: Int) {}

    fun solveEquation(equation: String): String {
        val indexOfEqualsSign = equation.indexOf('=')
        val leftSide = extractCumulativeValuesForEquationSide(equation, 0, indexOfEqualsSign)
        val rightSide = extractCumulativeValuesForEquationSide(equation, 1 + indexOfEqualsSign, equation.length)

        val xValuesMovedToLeftSide = leftSide.xValue - rightSide.xValue
        val intValuesMovedToRightSide = rightSide.intValue - leftSide.intValue

        if (xValuesMovedToLeftSide != 0) {
            return UNKNOWN + "=" + (intValuesMovedToRightSide / xValuesMovedToLeftSide);
        }
        if (intValuesMovedToRightSide == 0) {
            return INFINITE_SOLUTIONS
        }

        return NO_SOLUTION
    }

    private fun extractCumulativeValuesForEquationSide(equation: String, startIndex: Int, endBoundary: Int): CumulativeValues {
        var xValue = 0
        var intValue = 0

        // If the side of the equation starts with '-' or '+' then jump to the next index.
        var index = if (equation[startIndex] == '-' || equation[startIndex] == '+')
                    1 + startIndex
                    else startIndex

        while (index < endBoundary) {
            var operationSign = 1
            if (index > startIndex && equation[index - 1] == '-') {
                operationSign = -1
            }

            val pairExtractedValueUpdatedIndex = extractValue(equation, index)
            val value = pairExtractedValueUpdatedIndex[0]
            index = pairExtractedValueUpdatedIndex[1]

            if (index < endBoundary && equation[index] == UNKNOWN) {
                xValue += value * operationSign
                index += 2
            } else {
                intValue += value * operationSign
                ++index
            }
        }

        return CumulativeValues(xValue, intValue)
    }

    private fun extractValue(equation: String, index: Int): IntArray {
        var value = 0
        var index = index
        while (index < equation.length && isDigit(equation[index])) {
            value = 10 * value + (equation[index] - '0')
            ++index
        }
        // If 'x' is not preceded by any value, then set the value to '1'.
        value = if (index > 0 && isDigit(equation[index - 1])) value else 1
        return intArrayOf(value, index)
    }

    private fun isDigit(current: Char): Boolean {
        return current in '0'..'9'
    }
}

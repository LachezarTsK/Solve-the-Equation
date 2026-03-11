
package main

import (
    "fmt"
    "strings"
)

const UNKNOWN = 'x'
const NO_SOLUTION = "No solution"
const INFINITE_SOLUTIONS = "Infinite solutions"

func solveEquation(equation string) string {

    indexOfEqualsSign := strings.Index(equation, "=")
    var leftSide CumulativeValues = extractCumulativeValuesForEquationSide(equation, 0, indexOfEqualsSign)
    var rightSide CumulativeValues = extractCumulativeValuesForEquationSide(equation, 1 + indexOfEqualsSign, len(equation))

    xValuesMovedToLeftSide := leftSide.xValue - rightSide.xValue
    intValuesMovedToRightSide := rightSide.intValue - leftSide.intValue

    if xValuesMovedToLeftSide != 0 {
        return string(UNKNOWN) + "=" + fmt.Sprint(intValuesMovedToRightSide/xValuesMovedToLeftSide)
    }
    if intValuesMovedToRightSide == 0 {
        return INFINITE_SOLUTIONS
    }

    return NO_SOLUTION
}

func extractCumulativeValuesForEquationSide(equation string, startIndex int, endBoundary int) CumulativeValues {
    xValue := 0
    intValue := 0

    // If the side of the equation starts with '-' or '+' then jump to the next index.
    index := Ternary((equation[startIndex] == '-' || equation[startIndex] == '+'),
             1 + startIndex,
             startIndex)

    for index < endBoundary {
        operationSign := 1
        if index > startIndex && equation[index - 1] == '-' {
            operationSign = -1
        }

        pairExtractedValueUpdatedIndex := extractValue(equation, index)
        value := pairExtractedValueUpdatedIndex[0]
        index = pairExtractedValueUpdatedIndex[1]

        if index < endBoundary && equation[index] == UNKNOWN {
            xValue += value * operationSign
            index += 2
        } else {
            intValue += value * operationSign
            index++
        }
    }

    return CumulativeValues{xValue, intValue}
}

func extractValue(equation string, index int) []int {
    value := 0
    for index < len(equation) && isDigit(equation[index]) {
        value = 10 * value + int(equation[index] - '0')
        index++
    }
    // If 'x' is not preceded by any value, then set the value to '1'.
    value = Ternary((index > 0 && isDigit(equation[index - 1])), value, 1)
    return []int{value, index}
}

func isDigit(current byte) bool {
    return current >= '0' && current <= '9'
}

func Ternary[T any](condition bool, first T, second T) T {
    if condition {
        return first
    }
    return second
}

type CumulativeValues struct {
    xValue   int
    intValue int
}

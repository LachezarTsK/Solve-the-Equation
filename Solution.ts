
function solveEquation(equation: string): string {
    const indexOfEqualsSign = equation.indexOf('=');
    const leftSide = extractCumulativeValuesForEquationSide(equation, 0, indexOfEqualsSign);
    const rightSide = extractCumulativeValuesForEquationSide(equation, 1 + indexOfEqualsSign, equation.length);

    const xValuesMovedToLeftSide = leftSide.xValue - rightSide.xValue;
    const intValuesMovedToRightSide = rightSide.intValue - leftSide.intValue;

    if (xValuesMovedToLeftSide !== 0) {
        return `${Util.UNKNOWN}=` + (intValuesMovedToRightSide / xValuesMovedToLeftSide);
    }
    if (intValuesMovedToRightSide === 0) {
        return Util.INFINITE_SOLUTIONS;
    }

    return Util.NO_SOLUTION;
};

/**
 * @param {string} equation
 * @param {number} startIndex
 * @param {number} endBoundary 
 * @return {CumulativeValues}
 */
function extractCumulativeValuesForEquationSide(equation: string, startIndex: number, endBoundary: number): CumulativeValues {
    let xValue = 0;
    let intValue = 0;

    // If the side of the equation starts with '-' or '+' then jump to the next index.
    let index = (equation.charAt(startIndex) === '-' || equation.charAt(startIndex) === '+')
                ? 1 + startIndex
                : startIndex;

    while (index < endBoundary) {
        let operationSign = 1;
        if (index > startIndex && equation.charAt(index - 1) === '-') {
            operationSign = -1;
        }

        const pairExtractedValueUpdatedIndex = extractValue(equation, index);
        const value = pairExtractedValueUpdatedIndex[0];
        index = pairExtractedValueUpdatedIndex[1];

        if (index < endBoundary && equation.charAt(index) === 'x') {
            xValue += value * operationSign;
            index += 2;
        } else {
            intValue += value * operationSign;
            ++index;
        }
    }

    return new CumulativeValues(xValue, intValue);
}

function extractValue(equation: string, index: number): number[] {
    let value = 0;
    while (index < equation.length && isDigit(equation.charAt(index))) {
        value = 10 * value + equation.codePointAt(index) - Util.ASCII_ZERO;
        ++index;
    }
    // If 'x' is not preceded by any value, then set the value to '1'.
    value = (index > 0 && isDigit(equation.charAt(index - 1))) ? value : 1;
    return [value, index];
}

function isDigit(current: string): boolean {
    return current >= '0' && current <= '9';
}

class CumulativeValues {
    constructor(public xValue: number, public intValue: number) {
    }
}

class Util {
    static UNKNOWN = 'x';
    static ASCII_ZERO = 48;
    static NO_SOLUTION = "No solution";
    static INFINITE_SOLUTIONS = "Infinite solutions";
}

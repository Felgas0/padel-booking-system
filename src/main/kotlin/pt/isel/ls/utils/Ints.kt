package pt.isel.ls.utils

/**
 * Returns the maximum integer between two integer operands.
 * @param a the first operand.
 * @param b the second operand.
 * @return the greatest of the two integer operands.
 */
fun max(
    a: Int,
    b: Int,
): Int = if (a >= b) a else b

/**
 * Looks for an integer in an sub-array and returns its index, if found.
 * Otherwise, returns -1;
 * @param a the array containing the sub-array to search.
 * @param fromIndex the first index of the sub-array.
 * @param toIndex the index after the last index of the sub-array.
 * @param n the integer to find.
 * @return the index of an occurrence of {@code n}, if {@code n} exists in the sub-array;
 * -1 otherwise.
 */
fun indexOfBinary(
    a: IntArray,
    fromIndex: Int,
    toIndex: Int,
    n: Int,
): Int {
    require(fromIndex <= toIndex) { "from($fromIndex) > to($toIndex)" }
    require(fromIndex >= 0) { "fromIndex ($fromIndex) must be non-negative" }
    require(toIndex <= a.size) { "toIndex ($toIndex) must not exceed array size (${a.size})" }

    // For a non-empty array, fromIndex must be less than a.size.
    if (a.isNotEmpty()) {
        require(fromIndex < a.size) { "fromIndex ($fromIndex) is out of bounds for array of size ${a.size}" }
    } else {
        require(fromIndex == 0) { "fromIndex ($fromIndex) must be 0 for an empty array" }
    }

    var low = fromIndex
    var high = toIndex - 1
    var mid: Int
    while (low <= high) {
        mid = low + (high - low) / 2
        when {
            n > a[mid] -> low = mid + 1
            n < a[mid] -> high = mid - 1
            else -> return mid
        }
    }
    return -1
}

/**
 * Loop condition change:
 * Using while (low <= high) ensures that even when there is a single element
 * (i.e. low == high), that element is examined.
 *
 * Mid-calculation:
 * The formula mid = low + (high - low) / 2 properly computes the midpoint
 * without any off-by-one errors.
 *
 * The requires were to check:
 * - That the sub-array range is valid, i.e., fromIndex is not greater than toIndex.
 * - That fromIndex is non-negative.
 * - That toIndex does not exceed the size of the array.
 * - For a non-empty array, that fromIndex is less than the array’s size.
 * - For an empty array, that fromIndex equals 0.
 */

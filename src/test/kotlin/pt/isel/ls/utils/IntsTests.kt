package pt.isel.ls.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IntsTests {
    @Test
    fun max_returns_greatest() {
        assertEquals(1, max(1, -2))
        assertEquals(1, max(-2, 1))
        assertEquals(-1, max(-1, -2))
        assertEquals(-1, max(-2, -1))
    }

    // Tests what happens when the number isn’t in the array.

    @Test
    fun indexOfBinary_returns_negative_if_not_found() {
        // Arrange
        val v = intArrayOf(1, 2, 3)

        // Act
        val ix: Int = indexOfBinary(v, 0, 2, 4)

        // Assert
        assertTrue(ix < 0)
    }

    // Tests if an invalid range (fromIndex > toIndex) throws an exception.

    @Test
    fun indexOfBinary_throws_IllegalArgumentException_if_indexes_are_not_valid() {
        assertFailsWith<IllegalArgumentException> {
            // Arrange
            val v = intArrayOf(1, 2, 3)

            // Act
            val ix: Int = indexOfBinary(v, 2, 1, 4)

            // Assert
            assertTrue(ix < 0)
        }
    }

    // Tests if the toIndex is exclusive.

    @Test
    fun indexOfBinary_right_bound_parameter_is_exclusive() {
        val v = intArrayOf(2, 2, 2)
        val ix: Int = indexOfBinary(v, 1, 1, 2)
        assertTrue(ix < 0)
    }

    /**
     * Missing:
     * - Test for an array with a single element
     * - Test for when n is actually found (a successful search)
     * - Test for an empty array
     * - Test for when fromIndex is negative
     * - Test for when fromIndex is equal to array size. It shouldn't be
     * (there's no valid sub-array to search).
     * - Test to check (toIndex - 1) should not exceed the last valid index.
     */

    // Test for an empty array
    @Test
    fun indexOfBinary_returns_negative_for_empty_array() {
        val v = intArrayOf()
        val ix: Int = indexOfBinary(v, 0, 0, 420)
        assertTrue(ix < 0) //
    }

    // Test with an array of 1 element
    @Test
    fun indexOfBinary_finds_element_in_single_element_array() {
        val v = intArrayOf(2)
        val ix: Int = indexOfBinary(v, 0, 1, 2)
        assertEquals(0, ix)
    }

    // Test for when n is actually found (a successful search)
    @Test
    fun indexOfBinary_finds_existing_element() {
        val v = intArrayOf(1, 3, 5, 7, 9)
        val ix: Int = indexOfBinary(v, 0, 4, 5)
        assertEquals(2, ix) //
    }

    // Test for when n is actually found in a sub-array
    @Test
    fun indexOfBinary_finds_existing_element_in_sub_array() {
        val v = intArrayOf(1, 3, 5, 7, 9)
        val ix: Int = indexOfBinary(v, 1, 3, 5)
        assertEquals(2, ix) //
    }

    // Test for when fromIndex is negative
    @Test
    fun indexOfBinary_throws_IllegalArgumentException_if_fromIndex_is_negative() {
        val v = intArrayOf(1, 2, 3, 4, 5)
        assertFailsWith<IllegalArgumentException> {
            indexOfBinary(v, -1, 3, 2)
        }
    }

    // Test for when toIndex exceeds size of array
    @Test
    fun indexOfBinary_throws_IllegalArgumentException_if_toIndex_exceeds_size() {
        val v = intArrayOf(1, 2, 3, 4, 5)
        assertFailsWith<IllegalArgumentException> {
            indexOfBinary(v, 1, v.size + 1, 3)
        }
    }

    // Test for when fromIndex is equal to array size.
    @Test
    fun indexOfBinary_throws_IllegalArgumentException_if_fromIndex_equals_size() {
        val v = intArrayOf(1, 2, 3, 4, 5)
        assertFailsWith<IllegalArgumentException> {
            indexOfBinary(v, v.size, v.size, 3)
        }
    }
}

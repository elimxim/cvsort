package com.github.elimxim

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test
import kotlin.test.assertContentEquals

class SortSearcherTest {
    @Test
    fun `empty filters`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = SortName.entries.filter { it != SortName.ALL }

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `nameTexts = 'BOGO'`() {
        val attrs = SearchAttributes(
                nameTexts = listOf("BOGO"),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)

        assertEquals(0, actual.size)
    }

    @Test
    fun `nameTexts = 'QUICK'`() {
        val attrs = SearchAttributes(
                nameTexts = listOf("QUICK"),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.HOARE_QUICK,
                SortName.LOMUTO_QUICK
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `nameTexts = 'quick'`() {
        val attrs = SearchAttributes(
                nameTexts = listOf("quick"),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.HOARE_QUICK,
                SortName.LOMUTO_QUICK
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `nameTexts = 'quick' & 'bu'`() {
        val attrs = SearchAttributes(
                nameTexts = listOf("quick", "bu"),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.BUBBLE,
                SortName.BUCKET,
                SortName.HOARE_QUICK,
                SortName.LOMUTO_QUICK
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `worstTimeComplexity = 'n'`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = listOf(Complexity.LINEAR),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.BUCKET,
                SortName.COUNTING,
                SortName.HOARE_QUICK,
                SortName.LOMUTO_QUICK,
                SortName.RADIX,
                SortName.STOOGE,
                SortName.TREE
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `worstSpaceComplexity = 'LINEAR'`() {

    }

    @Test
    fun `worstTimeComplexity = 'LINEAR' & worstSpaceComplexity = 'LINEAR'`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = listOf(Complexity.LINEAR),
                worstSpaceComplexity = listOf(Complexity.LINEAR),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.COUNTING,
                SortName.RADIX
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `methods = 'INSERTION'`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = listOf(SortMethod.INSERTION),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.INSERTION,
                SortName.SHELL,
                SortName.TREE
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `methods = 'INSERTION' & nameTexts = 'INS'`() {
        val attrs = SearchAttributes(
                nameTexts = listOf("INS"),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = listOf(SortMethod.INSERTION),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.INSERTION
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `recursive = 'Yes' & stable = 'Yes'`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = true.yesNo(),
                stable = true.yesNo(),
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.MERGE,
                SortName.TREE
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `recursive = 'Yes' & stable = 'No'`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = true.yesNo(),
                stable = false.yesNo(),
                inventionYears = emptyList(),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.HEAP,
                SortName.HOARE_QUICK,
                SortName.LOMUTO_QUICK,
                SortName.STOOGE
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `inventionYears = '2000'`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = listOf(2000),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.GNOME
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `inventionYears = '1970' & '1979`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = listOf(1970, 1979),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.ODD_EVEN,
                SortName.PANCAKE
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `inventionYears = '2001' & '2002' & '2003'`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = listOf(2001, 2002, 2003),
                authorTexts = emptyList()
        )

        val actual = SortSearcher.search(attrs)

        assertEquals(0, actual.size)
    }

    @Test
    fun `authorTexts = 'Harold'`() {
        val attrs = SearchAttributes(
                nameTexts = emptyList(),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = listOf("Harold")
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.BUCKET,
                SortName.COUNTING,
                SortName.RADIX
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }

    @Test
    fun `authorTexts = 'Harold' & nameTexts = 'RAD'`() {
        val attrs = SearchAttributes(
                nameTexts = listOf("RAD"),
                worstTimeComplexity = emptyList(),
                worstSpaceComplexity = emptyList(),
                methods = emptyList(),
                recursive = null,
                stable = null,
                inventionYears = emptyList(),
                authorTexts = listOf("Harold")
        )

        val actual = SortSearcher.search(attrs)
        val expected = listOf(
                SortName.RADIX
        )

        assertEquals(expected.size, actual.size)
        assertContentEquals(actual, expected)
    }
}
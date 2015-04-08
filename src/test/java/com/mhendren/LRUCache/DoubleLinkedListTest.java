package com.mhendren.LRUCache;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DoubleLinkedListTest {
    List<Integer> stdList = new DoubleLinkedList<Integer>();
    @Before
    public void beforeEach() throws Exception {
        stdList.add(4);
        stdList.add(5);
        stdList.add(6);
    }

    @Test
    public void testSize() throws Exception {
        List<Integer> list = new DoubleLinkedList<Integer>();
        assertThat(list.size(), is(equalTo(0)));
        assertThat(stdList.size(), is(equalTo(3)));
    }

    @Test
    public void testIsEmpty() throws Exception {
        List<Integer> list = new DoubleLinkedList<Integer>();
        assertThat(list.isEmpty(), is(true));
        assertThat(stdList.isEmpty(), is(false));
    }

    @Test
    public void testContains() throws Exception {
        assertThat(stdList.contains(2), is(false));
        assertThat(stdList.contains(6), is(true));
    }

    @Test
    public void testAdd() throws Exception {
        stdList.add(10);
        assertThat(stdList.contains(10), is(true));
    }

    @Test
    public void testRemove() throws Exception {
        assertThat(stdList.remove((Integer)5), is(true));
        assertThat(stdList.contains(6), is(true));
        assertThat(stdList.contains(5), is(false));
    }

    @Test
    public void testClear() throws Exception {
        stdList.clear();
        assertThat(stdList.size(), is(equalTo(0)));
    }

    @Test
    public void testGet() throws Exception {
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
        assertThat(stdList.get(2), is(equalTo(6)));
    }

    @Test
    public void testSet() throws Exception {
        Integer x = stdList.set(0, 19);
        assertThat(stdList.get(0), is(equalTo(19)));
        assertThat(x, is(equalTo(4)));
    }

    @Test
    public void testAdd1() throws Exception {
        stdList.add(1, 17);
        assertThat(stdList.get(1), is(equalTo(17)));
        assertThat(stdList.get(2), is(equalTo(5)));
    }

    @Test
    public void testRemove1() throws Exception {
        assertThat(stdList.remove(stdList.size() - 1), is(equalTo(6)));
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
    }

    @Test
    public void testIndexOf() throws Exception {
        assertThat(stdList.indexOf(6), is(equalTo(2)));
        assertThat(stdList.indexOf(5), is(equalTo(1)));
        assertThat(stdList.indexOf(4), is(equalTo(0)));
        assertThat(stdList.indexOf(100), is(equalTo(-1)));
    }

    @Test
    public void testLastIndexOf() throws Exception {
        stdList.add(5);
        assertThat(stdList.lastIndexOf(5), is(equalTo(3)));
        assertThat(stdList.lastIndexOf(4), is(equalTo(0)));
    }

    @Test
    public void testSubList() throws Exception {
        stdList.add(7);
        stdList.add(8);
        stdList.add(9);
        List<Integer> list = stdList.subList(1, 3);
        assertThat(list.size(), is(equalTo(3)));
        assertThat(list.get(0), is(equalTo(5)));
        assertThat(list.get(1), is(equalTo(6)));
        assertThat(list.get(2), is(equalTo(7)));
    }
}
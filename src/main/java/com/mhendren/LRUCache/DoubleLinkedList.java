package com.mhendren.LRUCache;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by mhendren on 4/8/2015.
 */
public class DoubleLinkedList<E> implements List<E> {
    private class DoubleLinkedListNode {
        DoubleLinkedListNode(E data) { this.data = data; }
        E data;
        DoubleLinkedListNode prev;
        DoubleLinkedListNode next;
    };

    DoubleLinkedListNode head;
    DoubleLinkedListNode tail;
    private int nodeCount = 0;

    @Override
    public int size() {
        return this.nodeCount;
    }

    @Override
    public boolean isEmpty() {
        return this.nodeCount == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data == null) return true;
            }
        } else {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data.equals(o)) return true;
            }
        }
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E o) {
        DoubleLinkedListNode newNode = new DoubleLinkedListNode(o);
        if (tail != null) { tail.next = newNode; }
        newNode.next = null;
        newNode.prev = tail;
        tail = newNode;
        if (head == null) head = newNode;
        nodeCount++;
        return true;
    }

    private void removeNode(DoubleLinkedListNode node) {
        if (node.prev != null) node.prev.next = node.next; else head = node.next;
        if (node.next != null) node.next.prev = node.prev; else tail = node.prev;
        nodeCount--;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data == null) {
                    removeNode(cur);
                    return true;
                }
            }
        } else {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data.equals(o)) {
                    removeNode(cur);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return false;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        nodeCount = 0;
    }

    private void checkIndex(int index, int adjust) throws IndexOutOfBoundsException {
        if (index >= (nodeCount + adjust) || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + nodeCount);
        }
    }

    private void checkIndex(int index) {
        checkIndex(index, 0);
    }

    private DoubleLinkedListNode findIndexByTail(int pos) {
        DoubleLinkedListNode cur = tail;
        for (int i = 0; i < pos; i++) {
            if (cur.prev != null) cur = cur.prev;
            else throw new IndexOutOfBoundsException("Index: " + (nodeCount - pos + i - 1) + ", No prev on Node");
        }
        return cur;
    }

    private DoubleLinkedListNode findIndex(int index) {
        DoubleLinkedListNode cur;
        if (index >= (nodeCount >> 1)) {
            cur = tail;
            for (int i = nodeCount; i > index; i--) {
                if(cur.prev != null) cur = cur.prev;
                else throw new IndexOutOfBoundsException("Index: " + index + ", No prev on Node");
            }
            return findIndexByTail(nodeCount - (index + 1));
        } else {
            cur = head;
            for (int i = 0; i < index; i++) {
                if (cur.next != null) cur = cur.next;
                else throw new IndexOutOfBoundsException("Index: " + index + ", No next on Node");
            }
        }
        return cur;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        DoubleLinkedListNode cur = findIndex(index);
        return cur.data;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        DoubleLinkedListNode cur = findIndex(index);
        E data = cur.data;
        cur.data = element;
        return data;
    }

    @Override
    public void add(int index, E element) {
        // can actually add at the current nodeCount position
        checkIndex(index, 1);

        if (index == nodeCount) add(element);
        else {
            DoubleLinkedListNode newNode = new DoubleLinkedListNode(element);
            DoubleLinkedListNode cur = findIndex(index);
            if (cur.prev != null) { cur.prev.next = newNode; } else { head = newNode; }
            newNode.next = cur;
            newNode.prev = cur.prev;
            cur.prev = newNode;
            nodeCount++;
        }
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        DoubleLinkedListNode cur = findIndex(index);
        if (cur.next != null) { cur.next.prev = cur.prev; } else { tail = cur.prev; }
        if (cur.prev != null) { cur.prev.next = cur.next; } else { head = cur.next; }
        nodeCount--;
        return cur.data;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data == null) return index;
                index++;
            }
        } else {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data.equals(o)) return index;
                index++;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
       int index = nodeCount - 1;
        if (o == null) {
            for(DoubleLinkedListNode cur = tail; cur != null; cur = cur.prev) {
                if (cur.data == null) return index;
                index--;
            }
        } else {
            for(DoubleLinkedListNode cur = tail; cur != null; cur = cur.prev) {
                if (cur.data.equals(o)) return index;
                index--;
            }
        }
        return -1;
    }

    @Override
    public ListIterator listIterator() {
        return null;
    }

    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    private List subListFromTail(int start, int stop) {
        DoubleLinkedListNode cur = tail;
        DoubleLinkedList<E> out = new DoubleLinkedList<E>();
        int index = 0;
        while (cur != null) {
            if (index >= start) {
                out.add(0, cur.data);
            }
            cur = cur.prev;
            index++;
            if (index > stop) break;
        }
        return out;
    }

    @Override
    public List subList(int fromIndex, int toIndex) throws IndexOutOfBoundsException {
        checkIndex(fromIndex);
        checkIndex(toIndex);
        if (toIndex < fromIndex) throw new IllegalArgumentException("fromIndex: " + fromIndex + " > toIndex: " + toIndex);
        if (nodeCount - toIndex <= fromIndex) return subListFromTail(nodeCount - toIndex - 1, nodeCount - fromIndex - 1);

        DoubleLinkedListNode cur = head;
        DoubleLinkedList<E> out = new DoubleLinkedList<E>();
        int index = 0;
        while(cur != null) {
            if (index >= fromIndex) {
                out.add(cur.data);
            }
            cur = cur.next;
            index++;
            if (index > toIndex) break;
        }
        return out;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

}

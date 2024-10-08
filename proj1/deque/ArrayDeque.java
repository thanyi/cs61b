package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private T[] items;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = items.length / 2;
        nextLast = nextFirst + 1;
    }

//    public ArrayDeque(T x){
//        size = 1;
//        items = (T[]) new Object[8];
//        nextFirst = items.length /2;
//        nextLast = nextFirst + 1;
//
//        items[nextFirst] = x;
//        nextFirst = (nextFirst - 1) % items.length;
//    }

    @Override
    public void addFirst(T item) {
        /* 判断是否ArrayDeque已满 */
        if (size != 0 && nextLast == (nextFirst + 1) % items.length) {
            resize(size * 2);
        }
        size += 1;

        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
    }

    @Override
    public void addLast(T item) {
        if (size != 0 && nextLast == (nextFirst + 1) % items.length) {
            resize(size * 2);
        }
        size += 1;
        items[nextLast] = item;
        nextLast = (nextLast + 1 + items.length) % items.length;

    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        /* 保证循环 */
        int start = (nextFirst + 1) % items.length;
        int end = (nextLast - 1 + items.length) % items.length;

        for (int i = start; i != end; i = (i + 1) % items.length) {
            System.out.print(items[i] + " ");
        }
        System.out.println(items[(nextLast - 1) % items.length]);
    }

    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        /* 重新构建一个从0开始的数组 */
        int k = 0;
        int first = (nextFirst + 1) % items.length;
        int last = (nextLast - 1 + items.length) % items.length;

        for (int i = first; i != last; i = (i + 1) % items.length) {
            if (items[i] != null) {
                newItems[k] = items[i];
            }
            k += 1;
        }
        newItems[k] = items[last];

        nextFirst = newItems.length - 1;
        nextLast = k + 1;

        items = newItems;

    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        if (size < (items.length / 2) && size >= 16) {
            resize((items.length / 2));
        }

        size -= 1;

        int first = (nextFirst + 1 + items.length) % items.length;

        T victim = items[first];
        items[first] = null;
        nextFirst = first;

        return victim;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        if (size <= items.length / 2 && size >= 16) {
            resize((items.length / 2));
        }

        size -= 1;
        int last = (nextLast - 1 + items.length) % items.length;


        T victim = items[last];
        items[last] = null;
        nextLast = last;

        return victim;
    }


    @Override
    public T get(int index) {
        int realIndex = (nextFirst + index + 1) % items.length;

        return items[realIndex];
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }


        if (other instanceof Deque) {
            Deque<T> otherList = (Deque) other;
            if (size != otherList.size()) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                T item = get(i);
                T itemOther = otherList.get(i);
                if (!item.equals(itemOther)) {
                    return false;
                }

            }

        } else {
            return false;
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }


    private class ArrayDequeIterator implements Iterator {
        int wizPos;

        ArrayDequeIterator() {
            wizPos = 0;
        }

        @Override
        public boolean hasNext() {
            return wizPos < size;
        }

        @Override
        public T next() {
            T item = get(wizPos);
            wizPos += 1;
            return item;
        }
    }
}

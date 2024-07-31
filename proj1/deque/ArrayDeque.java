package deque;

public class ArrayDeque<T> {
    private int size;
    private T[] items;
    int nextFirst;
    int nextLast;
    public ArrayDeque(){
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = items.length /2;
        nextLast = nextFirst + 1;
    }

    public void addFirst(T item){
        /* 判断是否ArrayDeque已满 */
        if (size != 0 && nextLast == nextFirst + 1){
            resize(size * 2);
        }
        size += 1;

        items[nextFirst] = item;
        nextFirst = (nextFirst - 1) % items.length;
    }


    public void addLast(T item){
        if (size != 0 && nextLast == nextFirst + 1){
            resize(size * 2);
        }
        size += 1;
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;

    }

    public boolean isEmpty(){
        if (size == 0){
            return true;
        }else
            return false;
    }


    public int size(){
        return size;
    }
    
    public void printDeque(){
        /* 保证循环 */
        for (int i = (nextFirst+1) % items.length; i != (nextLast-1) % items.length; i = (i+1) % items.length ) {
            System.out.print(items[i] + " ");
        }
        System.out.println(items[(nextLast-1) % items.length]);
    }

    public void resize(int capacity){
        T[] newItems = (T[])new Object[capacity];
//        for (int i = 0; i <= nextFirst; i++) {
//            newItems[i] = items[i];
//        }
//        /* 保存新数组长度，方便下标计算 */
//        int k = newItems.length -1;
//
//        for (int j = items.length -1 ; j >= nextLast ; j--) {
//            newItems[k] = items[j];
//            k-=1;
//        }
//        nextFirst = newItems.length - (items.length - nextFirst);
//        items = newItems;

        int oldSize = size();
        /* 重新构建一个从0开始的数组 */
        int k = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null){
                newItems[k] = items[i];
            }
            k += 1 ;
        }
        nextFirst = newItems.length - 1;
        nextLast  = k + 1;

        items = newItems;
    }

    public T removeFirst(){
        if(size == 0){
            return null;
        }

        if (size < items.length / 4 && size >= 16){
            resize((int)(items.length / 2));
        }

        size -= 1;

        int first = (nextFirst+1) % items.length;
        if (first < 0){
            first += items.length;
        }
        T victim = items[first];
        items[first] = null;
        nextFirst = first;

        return victim;
    }

    public T removeLast(){
        if(size == 0){
            return null;
        }

        if (size < items.length / 4 && size >= 16){
            resize((int)(items.length / 2));
        }

        size -= 1;
        int last = (nextLast - 1) % items.length;
        if (last < 0){
            last += items.length;
        }

        T victim = items[last];
        items[last] = null;
        nextLast = last;

        return victim;
    }

    public T get(int index){
        int realIndex = (nextFirst + index + 1 ) % items.length;

        return items[realIndex];
    }
}

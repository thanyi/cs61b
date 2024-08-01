package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private Comparator<T> comp;
    public MaxArrayDeque(Comparator<T> c){
        super();
        comp = c;
    }

    public MaxArrayDeque(Comparator<T> c,T x){
        super(x);
        comp = c;
    }



    public T max(){
        T max = get(0);
        for (int i = 1; i < size(); i++) {
            T item = get(i);
            if(comp.compare(max, item) <= 0){
                max = item;
            }
        }
        return max;
    }

    public T max(Comparator<T> c){
        T max = get(0);
        for (int i = 1; i < size(); i++) {
            T item = get(i);
            if(c.compare(max, item) <= 0){
                max = item;
            }
        }
        return max;
    }


}

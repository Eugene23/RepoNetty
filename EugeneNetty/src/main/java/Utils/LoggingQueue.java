package Utils;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Администратор on 27.09.2015.
 */
public final class LoggingQueue<E> implements Iterable<E> {
   private final ConcurrentLinkedDeque<E> queue = new ConcurrentLinkedDeque<E>();
    private int counter = 0;

    public Iterator<E> iterator() {
        return queue.iterator();
    }

    public synchronized boolean add(E e){
        counter++;
        if(counter==16){
            queue.remove();
            counter--;
        }
        return queue.add(e);
    }

}

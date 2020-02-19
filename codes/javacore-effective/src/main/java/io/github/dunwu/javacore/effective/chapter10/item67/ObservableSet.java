// Broken - invokes alien method from synchronized block! - Page 265
package io.github.dunwu.javacore.effective.chapter10.item67;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ObservableSet<E> extends ForwardingSet<E> {

    private final List<SetObserver<E>> observers = new ArrayList<SetObserver<E>>();

    public ObservableSet(Set<E> set) {
        super(set);
    }

    public void addObserver(SetObserver<E> observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver<E> observer) {
        synchronized (observers) {
            return observers.remove(observer);
        }
    }

    // This method is the culprit
    private void notifyElementAdded(E element) {
        synchronized (observers) {
            for (SetObserver<E> observer : observers) {
                observer.added(this, element);
            }
        }
    }

    // Alien method moved outside of synchronized block - open calls - Page 268
    // private void notifyElementAdded(E element) {
    // List<SetObserver<E>> snapshot = null;
    // synchronized(observers) {
    // snapshot = new ArrayList<SetObserver<E>>(observers);
    // }
    // for (SetObserver<E> observer : snapshot)
    // observer.added(this, element);
    // }

    // Thread-safe observable set with CopyOnWriteArrayList - Page 269
    //
    // private final List<SetObserver<E>> observers =
    // new CopyOnWriteArrayList<SetObserver<E>>();
    //
    // public void addObserver(SetObserver<E> observer) {
    // observers.add(observer);
    // }
    // public boolean removeObserver(SetObserver<E> observer) {
    // return observers.remove(observer);
    // }
    // private void notifyElementAdded(E element) {
    // for (SetObserver<E> observer : observers)
    // observer.added(this, element);
    // }

    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if (added) { notifyElementAdded(element); }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c) {
            result |= add(element); // calls notifyElementAdded
        }
        return result;
    }

}

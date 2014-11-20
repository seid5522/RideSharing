package com.ridesharing.ui.Inject;

/**
 * @Package com.ridesharing.ui.Inject
 * @Author wensheng
 * @Date 2014/11/18.
 */
import dagger.ObjectGraph;

public interface Injector {
    /**
     * Gets the object graph for this component.
     * @return the object graph
     */
    public ObjectGraph getObjectGraph();

    /**
     * Injects a target object using this component's object graph.
     * @param target the target object
     */
    public void inject(Object target);
}
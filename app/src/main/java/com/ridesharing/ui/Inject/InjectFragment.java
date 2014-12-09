/*
 * Copyright (C) 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ridesharing.ui.Inject;

import android.app.Activity;
import android.support.v4.app.Fragment;
import dagger.ObjectGraph;

import java.util.ArrayList;
import java.util.List;
import static com.ridesharing.ui.Inject.Preconditions.checkState;
/**
 * @Package com.ridesharing.ui.login
 * @Author wensheng
 * @Date 2014/11/18.
 */
public class InjectFragment
        extends Fragment
        implements Injector {
    private ObjectGraph mObjectGraph;
    private boolean mFirstAttach = true;

    /**
     * Creates an object graph for this Fragment by extending the hosting Activity's object
     * graph with the modules returned by {@link #getModules()}, then injects this Fragment with the created graph.
     */
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        // expand the activity graph with the fragment-specific module(s)
        ObjectGraph appGraph = ((Injector) activity).getObjectGraph();
        List<Object> fragmentModules = getModules();
        //if(fragmentModules != null){
         mObjectGraph = appGraph.plus(fragmentModules.toArray());
        //}

        // make sure it's the first time through; we don't want to re-inject a retained fragment that is going
        // through a detach/attach sequence.
        if (mFirstAttach) {
            inject(this);
            mFirstAttach = false;
        }
    }

    @Override
    public void onDestroy() {
        // Eagerly clear the reference to the object graph to allow it to be garbage collected as
        // soon as possible.
        mObjectGraph = null;

        super.onDestroy();
    }

    // implement Injector interface

    /**
     * Gets this Fragment's object graph.
     *
     * @return the object graph
     */
    @Override
    public final ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    /**
     * Injects a target object using this Fragment's object graph.
     *
     * @param target the target object
     */
    @Override
    public void inject(Object target) {
        checkState(mObjectGraph != null, "object graph must be assigned prior to calling inject");
        mObjectGraph.inject(target);
    }

    /**
     * Returns the list of dagger modules to be included in this Fragment's object graph.  Subclasses that override
     * this method should add to the list returned by super.getModules().
     *
     * @return the list of modules
     */
    protected List<Object> getModules() {
        List<Object> result = new ArrayList<Object>();
        result.add(new InjectingFragmentModule(this, this));
        return result;
    }
}

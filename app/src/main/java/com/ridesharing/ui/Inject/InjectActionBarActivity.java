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

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.inputmethod.InputMethodManager;

import com.ridesharing.App;
import com.ridesharing.Service.ServiceModule;
import com.ridesharing.ui.Inject.Injector;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * @Package com.ridesharing.ui
 * @Author wensheng
 * @Date 2014/10/26.
 */
public class InjectActionBarActivity extends ActionBarActivity implements Injector {
    private ObjectGraph activityGraph;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the activity graph by .plus-ing our modules onto the application graph.
        App application = (App) getApplication();
        //activityGraph = application.getApplicationGraph().plus(getModules().toArray());
        activityGraph = application.getApplicationGraph();
        // Inject ourselves so subclasses will have dependencies fulfilled when this method returns.
        activityGraph.inject(this);
    }

    @Override protected void onDestroy() {
        // Eagerly clear the reference to the activity graph to allow it to be garbage collected as
        // soon as possible.
        activityGraph = null;

        super.onDestroy();
    }

    @Override
    public final ObjectGraph getObjectGraph() {
        return activityGraph;
    }

    /**
     * A list of modules to use for the individual activity graph. Subclasses can override this
     * method to provide additional modules provided they call and include the modules returned by
     * calling {@code super.getModules()}.
     */
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new ServiceModule());
    }

    /** Inject the supplied {@code object} using the activity-specific graph. */
    @Override
    public void inject(Object object) {
        activityGraph.inject(object);
    }

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}

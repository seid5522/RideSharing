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

/**
 * @Package com.ridesharing.ui.Inject
 * @Author wensheng
 * @Date 2014/11/18.
 */
import dagger.Module;
import dagger.Provides;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * The dagger module associated with {@link InjectFragment} and {@link InjectFragment}.
 */
@Module(library=true)
public class InjectingFragmentModule {
    private android.support.v4.app.Fragment mFragment;
    private Injector mInjector;

    /**
     * Class constructor.
     *
     * @param fragment the Fragment with which this module is associated.
     */
    public InjectingFragmentModule(android.support.v4.app.Fragment fragment, Injector injector) {
        mFragment = fragment;
        mInjector = injector;
    }

    /**
     * Provides the Fragment
     *
     * @return the Fragment
     */
    @Provides
    public android.support.v4.app.Fragment provideFragment() {
        return mFragment;
    }

    /**
     * Provides the Injector for the Fragment-scope graph
     *
     * @return the Injector
     */
    @Provides
    @Fragment
    public Injector provideFragmentInjector() {
        return mInjector;
    }

    /**
     * Defines an qualifier annotation which can be used in conjunction with a type to identify dependencies within
     * the object graph.
     * @see <a href="http://square.github.io/dagger/">the dagger documentation</a>
     */
    @Qualifier
    @Target({FIELD, PARAMETER, METHOD})
    @Documented
    @Retention(RUNTIME)
    public @interface Fragment {
    }
}

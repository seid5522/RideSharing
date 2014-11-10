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
package com.ridesharing.ui.main;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharing.R;

import java.util.List;

/**
 * @Package com.ridesharing.ui.main
 * @Author wensheng
 * @Date 2014/11/8.
 */
public class SearchPlaceAdapter extends CursorAdapter {
    private List<String> items;
    private Context currentContext;
    private TextView text;
    private SearchView searchView;

    public SearchPlaceAdapter(Context context, Cursor c, List<String> items, SearchView view) {
        super(context, c);
        this.items = items;
        currentContext = context;
        this.searchView = view;
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_place, parent, false);

        text = (TextView) view.findViewById(R.id.item);


        text.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                TextView textView = (TextView)view.findViewById(R.id.item);
                searchView.setQuery(textView.getText(), false);
            }
        });
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        text.setText(items.get(cursor.getPosition()));
    }
}

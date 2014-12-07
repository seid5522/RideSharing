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
import android.os.Bundle;

import com.ridesharing.R;
import com.ridesharing.ui.Inject.InjectFragment;
import com.ridesharing.ui.cards.RequestCard;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * @Package com.ridesharing.ui.main
 * @Author wensheng
 * @Date 2014/12/5.
 */
@EFragment(R.layout.fragment_request)
public class RequestFragment extends InjectFragment {
    private static final String POSITION = "position";
    private static final String NAME = "name";

    private int position;
    private String name;
    MainActivity mainActivity;

    @ViewById(R.id.request_card_listview)
    CardListView cardListView;

    public static RequestFragment newInstance(int position, String name) {
        RequestFragment fragment = new RequestFragment_();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public RequestFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION);
            name = getArguments().getString(NAME);
        }
    }

    @AfterViews
    public void initial(){
        ArrayList<Card> cards = new ArrayList<Card>();
        //cards.add(new RequestCard(getActivity()));
        //cards.add(new RequestCard(getActivity()));
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        cardListView.setAdapter(mCardArrayAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
        mainActivity.onSectionAttached(
                getArguments().getInt(POSITION));


    }
}

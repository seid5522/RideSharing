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
import android.widget.Toast;

import com.ridesharing.Entity.Request;
import com.ridesharing.Entity.User;
import com.ridesharing.Entity.Wish;
import com.ridesharing.R;
import com.ridesharing.Service.RequestService;
import com.ridesharing.Service.UserService;
import com.ridesharing.Service.WishService;
import com.ridesharing.ui.Inject.InjectFragment;
import com.ridesharing.ui.cards.RequestCard;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import javax.inject.Inject;

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

    @Inject
    RequestService requestServer;
    @Inject
    WishService wishService;
    @Inject
    UserService userService;

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
        getAllRequests();
    }

    @UiThread
    public void showNoResult(){
        Toast.makeText(getActivity(), getText(R.string.noResult), Toast.LENGTH_SHORT).show();
    }

    @Background
    public void getAllRequests(){
        ArrayList<Request> requests = requestServer.fetchAll();
        if(requests == null || requests.size() == 0){
            showNoResult();
            return;
        }
        ArrayList<Card> cards = new ArrayList<Card>();
        for(Request request: requests){
            int wishId = request.getWid();
            //request user id
            int userId = request.getUid();
            //ask user id;
            int askuserId = request.getAskuid();
            User currentUser = userService.getUser();
            boolean isAskedUser = false;
            if(askuserId == currentUser.getId()){
                isAskedUser = true;
            }
            Wish wish;
            if(isAskedUser){
                if(wishService.getWishHashtable().containsKey(currentUser.getUsername())){
                    wish = wishService.getWishHashtable().get(currentUser.getUsername());
                }else{
                    wish = wishService.getSpecificWish(wishId);
                    wishService.getWishHashtable().put(currentUser.getUsername(), wish);
                }

            }else{
                User user;
                if(!userService.getUserTables().containsKey(askuserId)){
                    user = userService.fetchOtherInfo(askuserId);
                    userService.getUserTables().put(user.getId(), user);
                }else{
                    user = userService.getUserTables().get(askuserId);
                }
                if(wishService.getWishHashtable().containsKey(user.getUsername())){
                    wish = wishService.getWishHashtable().get(user.getUsername());
                }else{
                    wish = wishService.getSpecificWish(wishId);
                    wishService.getWishHashtable().put(user.getUsername(), wish);
                }
            }
            cards.add(new RequestCard(getActivity(), request, wish, isAskedUser));

        }
        renderCards(cards);
    }

    @UiThread
    public void renderCards(ArrayList<Card> cards){
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

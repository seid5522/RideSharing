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
package com.ridesharing.ui.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridesharing.Entity.Request;
import com.ridesharing.Entity.Wish;
import com.ridesharing.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * @Package com.ridesharing.ui.cards
 * @Author wensheng
 * @Date 2014/12/5.
 */
public class RequestCard  extends Card {
    private Request request;
    private Wish wish;
    public RequestCard(Context context, Request request, Wish wish) {
        super(context, R.layout.request_card_main);
        this.request = request;
        this.wish = wish;
        init();
    }

    private void init(){
        RequestCardHeader header = new RequestCardHeader(getContext(), R.layout.request_card_header, wish.getImageURL());
        header.setButtonExpandVisible(false);
        addCardHeader(header);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView textFrom = (TextView)view.findViewById(R.id.request_card_from_text);
        textFrom.setText("hello");
    }

    class RequestCardHeader extends CardHeader{
        String url;
        public RequestCardHeader(Context context, int innerLayout, String url) {
            super(context, innerLayout);
            this.url = url;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            int loader = R.drawable.loading;
            ImageView mapImage = (ImageView) view.findViewById(R.id.mapImage);

        }
    }
}

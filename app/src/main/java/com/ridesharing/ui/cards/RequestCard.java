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
import android.graphics.Bitmap;
import android.location.Address;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharing.Entity.Request;
import com.ridesharing.Entity.RequestStatusType;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultType;
import com.ridesharing.Entity.Wish;
import com.ridesharing.R;
import com.ridesharing.Service.LocationServiceImpl_;
import com.ridesharing.Service.RequestService;
import com.ridesharing.Service.RequestServiceImpl;
import com.ridesharing.Utility.ImageLoader;

import org.w3c.dom.Text;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * @Package com.ridesharing.ui.cards
 * @Author wensheng
 * @Date 2014/12/5.
 */
public class RequestCard  extends Card {
    private Request request;
    private Wish wish;
    private boolean askedUser;

    public RequestCard(Context context, Request request, Wish wish, boolean askedUser) {
        super(context, R.layout.request_card_main);
        this.request = request;
        this.wish = wish;
        this.askedUser = askedUser;
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
        String fromAddr = wish.getFromAddr();
        if( fromAddr == null || fromAddr.equals("")){
            Address address = LocationServiceImpl_.getLocationFromLatLng(this.getContext(), wish.getFromlat(), wish.getFromlng());
            if(address == null){
                textFrom.setText(this.getContext().getText(R.string.not_available));
            }else{
                textFrom.setText(address.getAddressLine(0));
            }
        }else{
            textFrom.setText(fromAddr);
        }
        TextView textTo = (TextView)view.findViewById(R.id.request_card_to_text);
        String toAddr = wish.getToAddr();
        if(toAddr == null || toAddr.equals("")){
            Address address = LocationServiceImpl_.getLocationFromLatLng(this.getContext(), wish.getTolat(), wish.getTolng());
            if(address == null){
                textTo.setText(this.getContext().getText(R.string.not_available));
            }else{
                textTo.setText(address.getAddressLine(0));
            }
        }else{
            textTo.setText(toAddr);
        }
        TextView textTime = (TextView)view.findViewById(R.id.request_card_time_text);
        textTime.setText(wish.getStartTime().toString());
        final TextView textStatus = (TextView)view.findViewById(R.id.request_card_status_text);
        switch(request.getStatus()){
            case STATUS_NOT_CONFIRM:
                textStatus.setText(this.getContext().getText(R.string.not_available));
                break;
            case STATUS_AGREE:
                textStatus.setText(this.getContext().getText(R.string.accept));
                break;
            case STATUS_DISAGREE:
                textStatus.setText(this.getContext().getText(R.string.decline));
                break;
        }
        if(askedUser && request.getStatus() == RequestStatusType.STATUS_NOT_CONFIRM){
            final Context context = this.getContext();
            final LinearLayout acceptAndDeclineLayout = (LinearLayout)view.findViewById(R.id.acceptAndDeclineLayout);
            acceptAndDeclineLayout.setVisibility(View.VISIBLE);
            Button acceptBtn = (Button)view.findViewById(R.id.acceptBtn);
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask<Void, Void, Boolean>(){

                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            request.setStatus(RequestStatusType.STATUS_AGREE);
                            RequestService requestService = new RequestServiceImpl();
                            Result result = requestService.update(request);
                            return (result.getType() == ResultType.Success);
                        }

                        @Override
                        protected void onPostExecute(final Boolean success) {
                            if(success){
                                textStatus.setText(context.getText(R.string.accept));
                                acceptAndDeclineLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                    .execute();
                }
            });
            Button declineBtn = (Button)view.findViewById(R.id.declineBtn);
            declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask<Void, Void, Boolean>(){

                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            request.setStatus(RequestStatusType.STATUS_DISAGREE);
                            RequestService requestService = new RequestServiceImpl();
                            Result result = requestService.update(request);
                            return (result.getType() == ResultType.Success);
                        }

                        @Override
                        protected void onPostExecute(final Boolean success) {
                            if(success){
                                textStatus.setText(context.getText(R.string.decline));
                                acceptAndDeclineLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                    .execute();
                }
            });
        }
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
            ImageLoader imgLoader = new ImageLoader(this.getContext());
            imgLoader.DisplayImage(url, loader, mapImage);

        }
    }
}

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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharing.Entity.Driver;
import com.ridesharing.Entity.Request;
import com.ridesharing.Entity.RequestStatusType;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultType;
import com.ridesharing.Entity.RoleType;
import com.ridesharing.Entity.User;
import com.ridesharing.Entity.Wish;
import com.ridesharing.Entity.WishType;
import com.ridesharing.R;
import com.ridesharing.Service.LocationServiceImpl;
import com.ridesharing.Service.RequestService;
import com.ridesharing.Service.RequestServiceImpl;

import org.w3c.dom.Text;

import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * @Package com.ridesharing.ui.cards
 * @Author wensheng
 * @Date 2014/11/25.
 */
public class CustomerDetailCard extends Card {
    private Wish wishInfo;
    private User userInfo;
    private User currentUserInfo;
    private boolean isDriver;
    private PopupWindow mPopupWindow;

    public CustomerDetailCard(Context context, Wish wishInfo, User userInfo, User currentUserInfo, boolean isDriver, PopupWindow mPopupWindow) {
        super(context, R.layout.customer_detail_card_main);
        this.wishInfo = wishInfo;
        this.userInfo = userInfo;
        this.currentUserInfo = currentUserInfo;
        this.isDriver = isDriver;
        this.mPopupWindow = mPopupWindow;
        init();
    }

    public CustomerDetailCard(Context context, int innerLayout, Wish wishInfo, User userInfo, User currentUserInfo, boolean isDriver, PopupWindow mPopupWindow) {
        super(context, innerLayout);
        this.wishInfo = wishInfo;
        this.userInfo = userInfo;
        this.currentUserInfo = currentUserInfo;
        this.isDriver = isDriver;
        this.mPopupWindow = mPopupWindow;
        init();
    }

    private void init(){
        CustomerDetailCardHeader header = new CustomerDetailCardHeader(getContext(), R.layout.customer_detail_card_header);
        header.setButtonExpandVisible(true);
        header.mName = userInfo.getFirstname() + " " + userInfo.getLastname();
        WishType type = wishInfo.getType();
        header.mSubName = (type == WishType.Request)? getContext().getString(R.string.rider) : getContext().getString(R.string.driver);
        RoleType roleType = RoleType.PASSENGER;
        if(wishInfo.getType() == WishType.Request && isDriver){
            roleType = RoleType.DRIVER;
        }
        header.request = new Request(currentUserInfo.getId(), userInfo.getId(), wishInfo.getId(), roleType, new Date(System.currentTimeMillis()), RequestStatusType.STATUS_NOT_CONFIRM);
        addCardHeader(header);

        //Add Thumbnail
        CustomerDetailCardThumb thumbnail = new CustomerDetailCardThumb(getContext());
        float density = getContext().getResources().getDisplayMetrics().density;
        int size= (int)(125*density);
        thumbnail.setUrlResource("https://lh5.googleusercontent.com/-squZd7FxR8Q/UyN5UrsfkqI/AAAAAAAAbAo/VoDHSYAhC_E/s"+size+"/new%2520profile%2520%25282%2529.jpg");
        thumbnail.setErrorResource(R.drawable.ic_ic_error_loading);
        addCardThumbnail(thumbnail);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView time = (TextView)view.findViewById(R.id.card_time);
        TextView fromadd1 = (TextView)view.findViewById(R.id.card_fromAddr1);
        TextView fromadd2 = (TextView)view.findViewById(R.id.card_fromAddr2);
        TextView toadd1 = (TextView)view.findViewById(R.id.card_toAddr1);
        TextView toadd2 = (TextView)view.findViewById(R.id.card_toAddr2);
        time.setText(wishInfo.getStartTime().toString());

        String fromAddr = wishInfo.getFromAddr();
        String fromAddr1 = "";
        if(fromAddr == null || fromAddr.equals("")){
            Address address = LocationServiceImpl.getLocationFromLatLng(this.getContext(), wishInfo.getFromlat(), wishInfo.getFromlng());
            fromAddr = address.getAddressLine(0);
            fromAddr1 = address.getAddressLine(1);
        }
        fromadd1.setText(fromAddr);
        fromadd2.setText(fromAddr1);
        String toAddr = wishInfo.getToAddr();
        String toAddr1 = "";
        if(toAddr == null || toAddr.equals("")){
            Address address = LocationServiceImpl.getLocationFromLatLng(this.getContext(), wishInfo.getTolat(), wishInfo.getTolng());
            toAddr = address.getAddressLine(0) + ", ";
            toAddr1 = address.getAddressLine(1);
        }
        toadd1.setText(toAddr);
        toadd2.setText(toAddr1);
    }

    class CustomerDetailCardThumb extends CardThumbnail {

        public CustomerDetailCardThumb(Context context) {
            super(context);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View viewImage) {
            /*
            viewImage.getLayoutParams().width = 250;
            viewImage.getLayoutParams().height = 250;
            */
        }

        @Override
        public boolean applyBitmap(View imageView, Bitmap bitmap) {
            return false;
        }
    }

    class CustomerDetailCardHeader extends CardHeader{
        String mName;
        String mSubName;
        Request request;

        public CustomerDetailCardHeader(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            final Context context = this.getContext();
            TextView txName = (TextView) view.findViewById(R.id.text_birth1);
            TextView txSubName = (TextView) view.findViewById(R.id.text_birth2);

            txName.setText(mName);
            txSubName.setText(mSubName);

            Button btnJoin = (Button)view.findViewById(R.id.btnJoin);
            btnJoin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    new AsyncTask<Void, Void, Boolean>(){

                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            RequestService requestService = new RequestServiceImpl();
                            Result result = requestService.add(request);
                            return (result.getType() == ResultType.Success);
                        }

                        protected void onPostExecute(final Boolean success) {
                            if(success){
                                Toast.makeText(context, context.getText(R.string.send_request_success), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, context.getText(R.string.send_request_fail), Toast.LENGTH_SHORT).show();
                            }
                            mPopupWindow.dismiss();
                        }
                    }
                    .execute();
                }
            });
        }
    }
}

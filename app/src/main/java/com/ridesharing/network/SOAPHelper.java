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
package com.ridesharing.network;

import java.io.IOException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;


/**
 * @author wensheng
 *
 */
public class SOAPHelper {
	private static final String NAMESPACE = "http://ridesharing.com/";
	private static final String BASE_URL = "http://maxwell.sju.edu/~wy590204/RideSharing/";
	private String url;
	private String method;
	private SoapObject parameter;
	
	public SOAPHelper(String url, String method) {
		this.url = url;
		this.method = method;
		parameter = new SoapObject(NAMESPACE, method);
	}
	
	public void addProperty(String name, Object value){
		parameter.addProperty(name, value);
	}
	
	public String run(){
		// 1. Create SOAP Envelope 
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// 2. Set the request parameters
		envelope.setOutputSoapObject(parameter);

		// 3. Create an HTTP Transport object to send the web service request
		HttpTransportSE httpTransport = new HttpTransportSE(BASE_URL + url);
		httpTransport.debug = true; // allows capture of raw request/respose in Logcat

		// 4. Make the web service invocation
		try {
			httpTransport.call(NAMESPACE + method, envelope);
		} catch (IOException | XmlPullParserException e) {
			Log.e("Error", e.getMessage());
		}

		// Logging the raw request and response (for debugging purposes)
		Log.d("TAG", "HTTP REQUEST:\n" + httpTransport.requestDump);
		Log.d("TAG", "HTTP RESPONSE:\n" + httpTransport.responseDump);

		// 5. Process the web service response
		String result = "";
		if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
		    SoapObject soapObject = (SoapObject) envelope.bodyIn;
		    result = (String)soapObject.getProperty(0);
		    Log.d("TAG", result);
		} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
		    SoapFault soapFault = (SoapFault) envelope.bodyIn;
		    Log.e("Error", soapFault.getMessage());
		}
		return result;
	}
}

package com.example.objserial;

import com.example.domain.CommunicationShareData;
import java.io.Serializable;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class CommunicationSerial  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private CommunicationShareData commucationObj;

	public CommunicationShareData getCommucationObj() {
		return commucationObj;
	}

	public void setCommucationObj(CommunicationShareData commucationObj) {
		this.commucationObj = commucationObj;
	}

	public static CommunicationShareData getCommunication(String json) {
		Gson gson = new GsonBuilder().setFieldNamingPolicy(
				FieldNamingPolicy.UPPER_CAMEL_CASE).create();

		CommunicationSerial response = gson.fromJson(json, CommunicationSerial.class);
		return response.commucationObj;
	}


}

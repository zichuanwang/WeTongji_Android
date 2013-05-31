package com.wetongji_android.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InformationList implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Information> informations = new ArrayList<Information>();

	public List<Information> getInformations() {
		return informations;
	}

	public void setInformations(List<Information> informations) {
		this.informations = informations;
	}
	
}

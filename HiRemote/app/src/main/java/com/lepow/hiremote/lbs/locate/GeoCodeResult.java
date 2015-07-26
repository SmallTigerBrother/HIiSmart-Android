package com.lepow.hiremote.lbs.locate;

import java.util.List;

public class GeoCodeResult
{
	private String status;

	private List<AddressResult> results;

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public List<AddressResult> getResults()
	{
		return results;
	}

	public void setResults(List<AddressResult> results)
	{
		this.results = results;
	}

	@Override
	public String toString()
	{
		return "TestResult [results=" + results + ", status=" + status + "]";
	}
}

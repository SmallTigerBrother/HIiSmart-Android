package com.mn.tiger.location;

public class GoogleGeoCodeResult
{
	public static final String STATUS_OK = "OK";

	private String status;

	private GoogleAddressResult[] results;

	public GoogleAddressResult[] getResults()
	{
		return results;
	}

	public void setResults(GoogleAddressResult[] results)
	{
		this.results = results;
	}

	public  boolean isStatusOK()
	{
		return STATUS_OK.equals(status);
	}

	@Override
	public String toString()
	{
		return "TestResult [results=" + results + ", status=" + status + "]";
	}
}

package com.mn.tiger.location;

public class GeoCodeResult
{
	public static final String STATUS_OK = "OK";

	private String status;

	private AddressResult[] results;

	public AddressResult[] getResults()
	{
		return results;
	}

	public void setResults(AddressResult[] results)
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

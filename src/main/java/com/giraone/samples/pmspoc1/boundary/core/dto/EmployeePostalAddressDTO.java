package com.giraone.samples.pmspoc1.boundary.core.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmployeePostalAddressDTO implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected long oid;
	protected int versionNumber;
	protected int ranking;
	protected String countryCode;
	protected String postalCode;
	protected String city;
	protected String secondaryAddressLine;
	protected String street;
	protected String houseNumber;
	protected String poBoxNumber;
	protected Long employeeId;

	public EmployeePostalAddressDTO()
	{
		this.oid = 0L;				// A value of 0L indicates: not from the database!
		this.versionNumber = -1;	// A value of -1 indicates: not from the database!
	}
	
	public long getOid()
	{
		return this.oid;
	}

	public void setOid(long oid)
	{
		this.oid = oid;
	}

	public int getVersionNumber()
	{
		return this.versionNumber;
	}

	public void setVersionNumber(int versionNumber)
	{
		this.versionNumber = versionNumber;
	}
		
	public int getRanking()
	{
		return ranking;
	}

	public void setRanking(int ranking)
	{
		this.ranking = ranking;
	}

	public String getCountryCode()
	{
		return countryCode;
	}

	public void setCountryCode(String countryCode)
	{
		this.countryCode = countryCode;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getSecondaryAddressLine()
	{
		return secondaryAddressLine;
	}

	public void setSecondaryAddressLine(String secondaryAddressLine)
	{
		this.secondaryAddressLine = secondaryAddressLine;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getHouseNumber()
	{
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber)
	{
		this.houseNumber = houseNumber;
	}

	public String getPoBoxNumber()
	{
		return poBoxNumber;
	}

	public void setPoBoxNumber(String poBoxNumber)
	{
		this.poBoxNumber = poBoxNumber;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}
}
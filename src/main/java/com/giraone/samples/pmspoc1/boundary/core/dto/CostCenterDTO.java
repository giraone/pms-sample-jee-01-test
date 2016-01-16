package com.giraone.samples.pmspoc1.boundary.core.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CostCenterDTO implements Serializable
{
	private static final long serialVersionUID = 1L;
    
	private long oid;
	private int versionNumber;
	private String identification;
	private String description;

	public CostCenterDTO()
	{
		this.oid = 0L;				// A value of 0L indicates: not from the database!
		this.versionNumber = -1;	// A value of -1 indicates: not from the database!
	}

	public long getOid()
	{
		return oid;
	}

	public void setOid(long oid)
	{
		this.oid = oid;
	}

	public int getVersionNumber()
	{
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber)
	{
		this.versionNumber = versionNumber;
	}

	public String getIdentification()
	{
		return identification;
	}

	public void setIdentification(String identification)
	{
		this.identification = identification;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
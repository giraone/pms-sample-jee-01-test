package com.giraone.samples.pmspoc1.boundary.test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;

import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.giraone.samples.pmspoc1.boundary.core.dto.CostCenterDTO_;
import com.giraone.samples.pmspoc1.boundary.core.dto.EmployeeDTO;
import com.giraone.samples.pmspoc1.boundary.core.dto.EmployeeDTO_;
import com.giraone.samples.pmspoc1.boundary.core.dto.EmployeePostalAddressDTO;
import com.giraone.samples.pmspoc1.boundary.core.dto.EmployeePostalAddressDTO_;
import com.giraone.samples.pmspoc1.boundary.core.enums.EnumGender;
import com.giraone.samples.pmspoc1.boundary.test.loader.SimpleTestDataGenerator;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPmsCoreApi_LoadTest extends TestPmsCoreApi
{	
	private static final int NR_OF_COST_CENTERS = 50;
	private static final int NR_OF_EMPLOYEES = 200;
	
	@BeforeClass
	public static void setupConnection()
	{
		TestPmsCoreApi.setupConnection();
	}

	@Before
	public void setup() throws Exception
	{
		super.setup();
	}

	//------------------------------------------------------------------------------------------

	@Test
	public void t_100_createManyCostCenters() throws Exception
	{	
		for (int i = 0; i < NR_OF_COST_CENTERS; i++)
		{
			this.createCostCenter(i);
		}
	}
	
	@Test
	public void t_200_createManyEmployees() throws Exception
	{
		JsonArray allCostCenters = this.loadAllCostCenters();
		int start = this.readNextFreeEmployeeId();
		for (int i = start; i < start + NR_OF_EMPLOYEES; i++)
		{
			this.createEmployee(i, allCostCenters);
		}
	}
	
	private void createCostCenter(int i) throws Exception
	{	
		String identification = String.format("K%05d", i);
		String description = SimpleTestDataGenerator.randomDepartment() + " " + identification;
		
		String jsonPayload = Json.createObjectBuilder()
		    .add(CostCenterDTO_.DTO_NAME_identification, identification)
		    .add(CostCenterDTO_.DTO_NAME_description, description)
		    .build()
		    .toString();
		
		given()
	        .spec(requestSpecBuilder.build())
	        .body(jsonPayload)
	    .when()
	        .post(TestPmsCoreApi_CostCenter.PATH_TO_RESOURCE)
	    .then()
	        .statusCode(HttpURLConnection.HTTP_CREATED);
	}
	
	private void createEmployee(int i, JsonArray allCostCenters) throws Exception
	{
		JsonObject costCenter = (JsonObject) allCostCenters.get(i % allCostCenters.size());
		String personnelNumber = String.format("%05d", i);
		EnumGender gender = SimpleTestDataGenerator.randomGender();
		String nationality = SimpleTestDataGenerator.randomNationalityCode();
		String country = SimpleTestDataGenerator.randomCountryCode();
		String firstName = SimpleTestDataGenerator.randomFirstName(gender);
		String lastName = SimpleTestDataGenerator.randomLastName();
		String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + SimpleTestDataGenerator.randomMailProvider();
		EmployeePostalAddressDTO p = new EmployeePostalAddressDTO();
		SimpleTestDataGenerator.fillRandomAddress(p);
		
		String jsonPayload = Json.createObjectBuilder()
			.add(EmployeeDTO_.DTO_NAME_costCenter, Json.createObjectBuilder()
				.add(CostCenterDTO_.DTO_NAME_oid, costCenter.getInt(CostCenterDTO_.DTO_NAME_oid)))
			.add(EmployeeDTO_.DTO_NAME_personnelNumber, personnelNumber)
			.add(EmployeeDTO_.DTO_NAME_lastName, lastName)
			.add(EmployeeDTO_.DTO_NAME_firstName, firstName)
			.add(EmployeeDTO_.DTO_NAME_dateOfBirth, SimpleTestDataGenerator.randomDateOfBirth().getTime().getTime())
			.add(EmployeeDTO_.DTO_NAME_gender, gender.toString())
			.add(EmployeeDTO_.DTO_NAME_dateOfEntry, SimpleTestDataGenerator.randomDateOfEntry().getTime().getTime())
			.add(EmployeeDTO_.DTO_NAME_nationalityCode, nationality)
			.add(EmployeeDTO_.DTO_NAME_countryOfBirth, country)
			.add(EmployeeDTO_.DTO_NAME_contactEmailAddress1, email)
			.add(EmployeeDTO_.DTO_NAME_maritalStatus, SimpleTestDataGenerator.randomMaritalStatus())
			.add(EmployeeDTO_.DTO_NAME_numberOfChildren, SimpleTestDataGenerator.random().nextInt(3))
			.add(EmployeeDTO_.DTO_NAME_postalAddresses, Json.createArrayBuilder().add(Json.createObjectBuilder()
		    	.add(EmployeePostalAddressDTO_.DTO_NAME_ranking, 1)
		    	.add(EmployeePostalAddressDTO_.DTO_NAME_countryCode, p.getCountryCode())
				.add(EmployeePostalAddressDTO_.DTO_NAME_postalCode, p.getPostalCode())
				.add(EmployeePostalAddressDTO_.DTO_NAME_city, p.getCity())
				.add(EmployeePostalAddressDTO_.DTO_NAME_street, p.getStreet())
				.add(EmployeePostalAddressDTO_.DTO_NAME_houseNumber, p.getHouseNumber())
		    ))
		    .build()
		    .toString();
		
		System.err.println(jsonPayload);
		
		given()
	        .spec(requestSpecBuilder.build())
	        .body(jsonPayload)
	    .when()
	        .post(TestPmsCoreApi_Employee.PATH_TO_RESOURCE)
	    .then()
	        .statusCode(HttpURLConnection.HTTP_CREATED);
	}
	
	private JsonArray loadAllCostCenters() throws Exception
	{	
		Response response = given()
	        .spec(requestSpecBuilder.build())
	        .queryParam("top", NR_OF_COST_CENTERS)
			.get(TestPmsCoreApi_CostCenter.PATH_TO_RESOURCE);
				
		JsonReader reader = Json.createReader(new StringReader(response.asString()));      
        return reader.readArray();
	}
	
	private int readNextFreeEmployeeId()
	{
		Response response = given()
	        .spec(requestSpecBuilder.build())
	        .queryParam("orderby", EmployeeDTO_.DTO_NAME_personnelNumber + " desc")
	        .queryParam("top", 100)
			.get(TestPmsCoreApi_Employee.PATH_TO_RESOURCE);
			
		String jsonString = response.body().asString();
		//System.out.println(jsonString);
		JsonPath jsonPath = from(jsonString);
		List<EmployeeDTO> employeeList = jsonPath.getList("blockItems");
		if (employeeList.size() == 0) return 0;
		
		for (int i = 0; i < employeeList.size(); i++)
		{
			String personnelNumber = jsonPath.getString("blockItems[" + i + "].personnelNumber");
			System.err.println("personnelNumber = " + personnelNumber);
			try
			{
				if (personnelNumber.matches("[0-9][0-9][0-9][0-9][0-9]"))
				{
					int last = Integer.parseInt(personnelNumber, 10);
					System.err.println("lastEmployeeId = " + last);
					return last + 1;
				}
			}
			catch (Exception e)
			{				
			}
		}
		
        return 0;
	}
}
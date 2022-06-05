package apiChaining;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {

	Response response;
	String BaseURI = "http://54.83.230.9:8088/employees/";
	
	@Test
	public void test9() {
		
		
		response = GetMethodAll();
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Status Code: " + response.getStatusCode());
		
		response = PostMethod("Donald", "Kura", "20000", "donald@email.com");
		Assert.assertEquals(response.getStatusCode(), 201);
		System.out.println("Status Code: " + response.getStatusCode());
		
		JsonPath jpath = response.jsonPath();
		
		int EmpId = jpath.get("id");
		
		System.out.println("id:- " + EmpId);
		
		response = PutMethod(EmpId, "Tindo", "Many", "3000", "tindo@email.com");
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Status Code: " + response.getStatusCode());
		
		jpath = response.jsonPath();			
		String EmpName = jpath.get("firstName");
		Assert.assertEquals(EmpName, "Tindo");
		System.out.println("firstName:- " + EmpName);
		
		response = DeleteMethod(EmpId);
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Status Code: " + response.getStatusCode());
		Assert.assertEquals(response.getBody().asString(), "");
		System.out.println("Response Body: " + response.getBody().asString());
		
		response = GetMethodEmp(EmpId);
		Assert.assertEquals(response.getStatusCode(), 400);
		System.out.println("Status Code: " + response.getStatusCode());
		Assert.assertEquals(response.jsonPath().get("message"), "Entity Not Found");
		System.out.println("Response Body: " + response.getBody().asString());
	}

	public Response GetMethodAll() {

		RestAssured.baseURI = BaseURI;

		RequestSpecification request = RestAssured.given();

		Response response = request.get();
		
		return response;
	}
	
	public Response PostMethod(String FirstName, String LastName, String Salary, String Email) {
		
		RestAssured.baseURI = BaseURI;
		
		JSONObject jobj = new JSONObject();
		
		jobj.put("firstName", FirstName);
		jobj.put("lastName", LastName);
		jobj.put("salary", Salary);
		jobj.put("email", Email);
		
		RequestSpecification request = RestAssured.given();
		
		Response response = request.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.body(jobj.toString())
								.post();
		
		return response;
	}
	
	public Response PutMethod(int EmpId, String FirstName, String LastName, String Salary, String Email) {
		
		RestAssured.baseURI = BaseURI;
		
		JSONObject jobj = new JSONObject();
		
		jobj.put("firstName", FirstName);
		jobj.put("lastName", LastName);
		jobj.put("salary", Salary);
		jobj.put("email", Email);
		
		RequestSpecification request = RestAssured.given();
		
		Response response = request.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.body(jobj.toString())
								.put("/" + EmpId);
		
		return response;
	}
	
	public Response DeleteMethod(int EmpId) {
		
		RestAssured.baseURI = BaseURI;
		
		RequestSpecification request = RestAssured.given();
		
		Response response = request.delete("/" + EmpId);
		
		return response;
	}
	
	public Response GetMethodEmp(int EmpId) {

		RestAssured.baseURI = BaseURI;

		RequestSpecification request = RestAssured.given();

		Response response = request.get("/" + EmpId);
		
		return response;
	}
}

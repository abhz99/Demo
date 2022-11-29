package com.methods;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class JiraAPI {
	
	//@Test
	public String getSessionId()								//	generating session id using cookie based authentication
	{
		RestAssured.baseURI = "http://localhost:8080/";			// pre-requisite
		
		Response response = RestAssured.given()
					.body("{ \"username\": \"abhznij99\", \r\n"
							+ "\"password\": \"naiga22maA$\" }")
					.headers("Content-Type","application/json")
					.when()
					.post("rest/auth/1/session");
		
		System.out.println(response.statusCode());
		System.out.println(response.body().asPrettyString());
		
		JsonPath path = new JsonPath(response.body().asPrettyString());	// pass the body to the constructor of the JsonPath() and extract the path
																		// from json validator website
		//System.out.println(path.get("session.name"));
		//System.out.println(path.get("loginInfo.loginCount"));
		//System.out.println(path.get("loginInfo.previousLoginTime"));
		return path.get("session.value");		
	}
	
	@Test
	public void getComments()
	{
		RestAssured.baseURI = "http://localhost:8080/rest/api/2/issue/";		// server address
		
		Response response = RestAssured.given()									// given
					.headers("Cookie","JSESSIONID ="+getSessionId())
					.when()
					.get("AA-2/comment");									// when
		
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody().asPrettyString());

		JsonPath path = new JsonPath(response.getBody().asPrettyString());
		String body = path.get("comments[0].body");		// there are multiple comments[+], so traverse accordingly through json path
		
		Assert.assertEquals(body, "story 2 comment");
		Assert.assertEquals(response.statusCode(), 200);
	}
	
	@Test
	public void addComments()
	{
		RestAssured.baseURI = "http://localhost:8080/rest/api/2/issue/";				// server address
		
		Response response = RestAssured.given()																// given
					.headers("Cookie","JSESSIONID ="+getSessionId())					// for post add content-type in header
					.header("Content-Type", "application/json")
					.body("{\r\n"
							+ "    \"body\": \"comment added from test case\",\r\n"
							+ "    \"visibility\": {\r\n"
							+ "        \"type\": \"role\",\r\n"
							+ "        \"value\": \"Administrators\"\r\n"
							+ "    }\r\n"
							+ "}")
					.when()																// when
					.post("AA-2/comment");
		
		JsonPath path = new JsonPath(response.body().asPrettyString());
		String body = path.get("comments[2].body");
		
		Assert.assertEquals(body,"comment added from test case");
		Assert.assertEquals(response.statusCode(), 201);				
	}
	
	@Test
	public void updateComment()
	{
		RestAssured.baseURI = "http://localhost:8080/rest/api/2/issue/";		// server address
		
		Response response = RestAssured.given()								// given
					.headers("Cookie","JSESSIONID ="+getSessionId())
					.header("Content-Type", "application/json")					
					.body("{\r\n"
							+ "    \"body\": \"comment added from test case - updated\",\r\n"
							+ "    \"visibility\": {\r\n"
							+ "        \"type\": \"role\",\r\n"
							+ "        \"value\": \"Administrators\"\r\n"
							+ "    }\r\n"
							+ "}")
					.when()														// when
					.put("AA-2/comment/10005");
		
		JsonPath path = new JsonPath(response.body().asPrettyString());
		String updatedBody = path.get("comments[2].body");
		
		Assert.assertEquals(updatedBody, "comment added from test case - updated");
		Assert.assertEquals(response.statusCode(), 200);
	}
	
	@Test
	public void deleteComment()
	{
		RestAssured.baseURI = "http://localhost:8080/rest/api/2/issue/";	// server address
		
		Response response = RestAssured.given()
					.headers("Cookie","JSESSIONID ="+getSessionId())
					.when()
					.delete("AA-2/comment/10001");
					
		Assert.assertEquals(response.statusCode(), 204);			
	}

}

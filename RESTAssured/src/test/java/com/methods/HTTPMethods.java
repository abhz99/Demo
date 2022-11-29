package com.methods;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public class HTTPMethods {
	
// Test to get resource from server
	
	@Test
	public static void getMethod() 
	{
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com/";	// baseURI = server-address used in URI
		
		// write code in Given-When-Then format
		
		Response response = RestAssured.given()		// pre-requisites --- body, headers, authorization, params only these 4
										.headers("Content-type", "application/json; charset=UTF-8")
										.when()							// action -- get/put/patch/delete
										.get("posts/1");				// resource-name/path param or query param
		
		System.out.println("status code : "+ response.getStatusCode());
		System.out.println("reponse body "+ response.getBody().asString());		// here format is not followed
		System.out.println("reponse body "+ response.getBody().asPrettyString());	//pretty string prints body in a proper key-value format
		
		// with Assert we check whether API is working properly 		
		Assert.assertEquals(response.getStatusCode(), 200);	// response verification
		
		/* if response --- body, status code and other response parameters do not match 
		 * expected results given in the Jira document, it means API is not working properly
		 */
	}
	
	@Test
	public static void postMethod() 
	{
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com/";		// server address
		
		Response response = RestAssured.given()															// pre-requisite
										.body("{\r\n"
												+ "    \"title\" : \"foo\",\r\n"
												+ "    \"body\" : \"bar\",\r\n"
												+ "    \"userId\" : \"1\"\r\n"
												+ "  }")
										.header("Content-Type", "application/json; charset=UTF-8")
										.when()															// action
										.post("posts");
							
		System.out.println("status code : "+response.statusCode());
		System.out.println("response body "+ response.body().asPrettyString());
		System.out.println("response time : "+ response.getTime());
		
		Headers headers = response.getHeaders();	// headers ----> [multiple Header objects] Headers class object
		for (Header header : headers) 			// to iterate multiple Header objects we use foreach loop
		{
			System.out.println(header.getName() +" "+ header.getValue());
		}
		
		Assert.assertEquals(response.statusCode(), 201); // Assert can also check response body and expected body
	}
	
	@Test
	public static void putMethod() 
	{
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com/";
		
		Response response = RestAssured.given()
					.body("{\r\n"
							+ "    \"id\": \"1\",\r\n"
							+ "    \"title\" : \"API testing\",\r\n"
							+ "    \"body\" : \"bar\",\r\n"
							+ "    \"userId\" : \"1\"\r\n"
							+ "  }")
					.header("Content-Type", "application/json; charset=UTF-8")
					.when()
					.put("posts/1");
		
		System.out.println("status code : "+response.statusCode());
		System.out.println("response body : "+ response.body().asPrettyString());
		
		Assert.assertEquals(response.statusCode(), 200);
	}
	
	@Test
	public static void deleteMethod() 
	{
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com/";
		
		Response response = RestAssured.when()	// if given param(body, header,etc) are not required then start with when()
					.delete("posts/1");
		
		System.out.println("status code : "+ response.statusCode());
		System.out.println("response body : "+ response.body().asPrettyString());
		
		Assert.assertEquals(response.statusCode(), 200);
	}
}

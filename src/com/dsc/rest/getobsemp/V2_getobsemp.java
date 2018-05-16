package com.dsc.rest.getobsemp;
import java.sql.Timestamp;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
import org.codehaus.jettison.json.JSONObject;

import com.dsc.util.APIEvent;
import com.dsc.util.ldap;

@Path("/v2/getobsemp")
public class V2_getobsemp {

//  **************** Version 2 of User authentication API
    @Path("/DSCAuthenticationSrv")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_dscldap(JSONObject inputJsonObj) throws Exception {
    	//  System.out.println("Json Object sent by user is "+ inputJsonObj);
    	java.util.Date date= new java.util.Date();
    	java.util.Date sdate=new Timestamp(date.getTime());
    	 Response rb = null;
    	 if((!inputJsonObj.has("username"))||(inputJsonObj.get("username")==null)||(inputJsonObj.get("username").equals("")))
 		{				
 			JSONObject retJson = new JSONObject();
 			retJson.put("result", "FAILED");
 			retJson.put("resultCode", "200");
 			retJson.put("message", "username is required");
 			rb = Response.ok(retJson.toString()).build();			

 		}
    	else if(!inputJsonObj.has("password")||(inputJsonObj.get("password")==null)||(inputJsonObj.get("password").equals("")))
 		{				
 			JSONObject retJson = new JSONObject();
 			retJson.put("result", "FAILED");
 			retJson.put("resultCode", "200");
 			retJson.put("message", "password is required");
 			rb = Response.ok(retJson.toString()).build();			

 		}
 		else
 		{
 			String domain = null;
 			if(inputJsonObj.has("domain"))
 			{
 				domain = inputJsonObj.getString("domain");
 			}
 	    	ldap vr = new ldap();
 			rb = Response.ok(vr.authenticateLDAPUser(inputJsonObj.getString("username"), inputJsonObj.getString("password"),domain).toString()).build();
 		}

       	// API EVENT LOG Start Here 
   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput",inputJsonObj);
  		 result.put("resname","DSCAuthenticationSrv");
  	//	 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here 
         
     //    System.out.println("**** DSCAuthenticationSrv result json:"+ rb.getEntity().toString());  	 
    	 
    	  return rb;
    	  
  	}
}

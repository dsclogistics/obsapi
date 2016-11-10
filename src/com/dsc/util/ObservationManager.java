package com.dsc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import java.sql.SQLException;
import java.sql.Statement;



import com.dsc.dao.obs;

public class ObservationManager {

	public Response completeReview(JSONObject inputJsonObj) throws JSONException
	{
		Response rb = null;
		JSONObject retJson = new JSONObject();
		Connection conn = null;
		PreparedStatement  updatePS = null;
		int obsInstId = -1;

		String SQL = "update OBS_INST set  obs_inst_status = 'REVIEWED' where obs_inst_id = ?";
		if(!inputJsonObj.has("obs_inst_id")||inputJsonObj.getString("obs_inst_id")==null||inputJsonObj.getString("obs_inst_id").equals(""))
		{
			retJson.put("result", "FAILED");
			retJson.put("resultCode", "200");
			retJson.put("message", "obs_inst_id parameter is required");
			rb = Response.ok(retJson.toString()).build();			
			return rb;
		}
		else
		{
			obsInstId = inputJsonObj.getInt("obs_inst_id");
		}
		try 
		{
			conn= obs.obsConn().getConnection();			
		} 
		catch (Exception e) {
			e.printStackTrace();
			retJson.put("result", "FAILED");
			retJson.put("resultCode", "200");
			retJson.put("message", "DB Connection Failed");
			rb = Response.ok(retJson.toString()).build();			
			return rb;
		}
		
		try
		{
			conn.setAutoCommit(false);
			updatePS = conn.prepareStatement(SQL);
			updatePS.setInt(1, obsInstId);
			updatePS.executeUpdate();			
			conn.commit();
			retJson.put("result", "Success");
			retJson.put("resultCode", "100");
			retJson.put("message", "Record for instance id "+obsInstId+" was successfully updated");
			rb = Response.ok(retJson.toString()).build();	
			
		}
		catch(Exception e)
		{
			try
			{
				conn.rollback();
			}
			catch(Exception e1){e1.printStackTrace();}			
			e.printStackTrace();
			retJson.put("result", "FAILED");
			retJson.put("resultCode", "200");
			retJson.put("message", "Error:"+e.getMessage());
			rb = Response.ok(retJson.toString()).build();		
		}//end of catch
		finally
		{
			if(updatePS!=null)
			{
				try
				{
					updatePS.close();
				}catch(Exception e1){e1.printStackTrace();}
			}
			if(conn!=null)
			{
				try
				{
					conn.close();
				}catch(Exception e1){e1.printStackTrace();}
			}
		}//end of finally
		
		
		return rb;
	}
	
}

package com.pandora.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.pandora.RequirementHistoryTO;
import com.pandora.RequirementStatusTO;
import com.pandora.TransferObject;
import com.pandora.UserTO;
import com.pandora.exception.DataAccessException;
import com.pandora.helper.DateUtil;

/**
 * This class contain all methods to handle data related with Requirement History entity into data base.
 */
public class RequirementHistoryDAO extends DataAccess {

    
    /**
     * Insert a new Requirement History object into data base.
     */
    public void insert(TransferObject to, Connection c) throws DataAccessException {
		PreparedStatement pstmt = null; 
		try {
		    RequirementHistoryTO rhto = (RequirementHistoryTO)to;

			pstmt = c.prepareStatement("insert into requeriment_history (requeriment_id, " +
									   "requeriment_status_id, creation_date, user_id, comment, iteration) " +
									   "values (?,?,?,?,?,?)");
			pstmt.setString(1, rhto.getRequirementId());
			pstmt.setString(2, rhto.getStatus().getId());
			pstmt.setTimestamp(3, DateUtil.getNow());
			pstmt.setString(4, rhto.getResource().getId());
			pstmt.setString(5, rhto.getComment());
			pstmt.setString(6, rhto.getIteration());
			pstmt.executeUpdate();
												
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			super.closeStatement(null, pstmt);
		}       
    }

    
    /**
     * Get a list of Requirement History objects from data base, base on requirement id.
     */    
    public Vector<RequirementHistoryTO> getListByRequirement(String reqId) throws DataAccessException{
        Vector<RequirementHistoryTO> response = null;
        Connection c = null;
		try {
			c = getConnection();
			response = this.getListByRequirement(reqId, c);
		} catch(Exception e){
			throw new DataAccessException(e);
		} finally{
			this.closeConnection(c);
		}
        return response;
    }    
    

    public Vector getIterationList(String requirementId) throws DataAccessException{
        Vector response = new Vector();
        Connection c = null;
		try {
			c = getConnection();
			response = this.getIterationList(requirementId, c);
		} catch(Exception e){
			throw new DataAccessException(e);
		} finally{
			this.closeConnection(c);
		}
		return response;
	}    
    

    private Vector getIterationList(String reqId, Connection c) throws DataAccessException{
        Vector response = new Vector();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
		    String sql = "select distinct rh.iteration from requeriment_history rh " +
		    			 "where rh.requeriment_id=?";
			pstmt = c.prepareStatement(sql);
			pstmt.setString(1, reqId);
			rs = pstmt.executeQuery();
			while (rs.next()){
				String iteration = getString(rs, "iteration");
    			if (iteration!=null && !iteration.trim().equals("") && !iteration.trim().equals("-1")) {
    				response.addElement(iteration);	
    			}
			} 						
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			super.closeStatement(rs, pstmt);
		}	 
		return response;
    }

    
    private Vector<RequirementHistoryTO> getListByRequirement(String reqId, Connection c) throws DataAccessException{
        Vector<RequirementHistoryTO> response= new Vector<RequirementHistoryTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
		    String sql = "select rs.name as status_name, rh.creation_date, rh.requeriment_id, rh.requeriment_status_id, " +
		    		     "rh.user_id, rh.comment, rh.iteration, u.name " +
		    			 "from requeriment_history rh, requeriment_status rs, tool_user u " +
		    			 "where rh.requeriment_status_id = rs.id " +
		    			   "and rh.user_id = u.id " +
		    			   "and rh.requeriment_id=? " +
		    			 "order by rh.creation_date";
			pstmt = c.prepareStatement(sql);
			pstmt.setString(1, reqId);
			rs = pstmt.executeQuery();
						
			while (rs.next()){
			    RequirementHistoryTO  rhto = this.populateByResultSet(rs);

			    //get remaining field...
			    RequirementStatusTO rsto = rhto.getStatus();
			    rsto.setName(getString(rs, "STATUS_NAME"));

			    UserTO uto = rhto.getResource();
		        uto.setName(getString(rs, "NAME"));    
			    
				response.addElement(rhto);
			} 
						
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			super.closeStatement(rs, pstmt);
		}	 
		return response;
    }

    
    
    /**
     * Create a new TO object based on data into result set.
     */
    protected RequirementHistoryTO populateByResultSet(ResultSet rs) throws DataAccessException{
        RequirementHistoryTO response = new RequirementHistoryTO();
        RequirementStatusTO rsto = new RequirementStatusTO();
        
        response.setDate(getTimestamp(rs, "creation_date"));
        response.setRequirementId(getString(rs, "requeriment_id"));
        response.setComment(getString(rs, "comment"));
        response.setIteration(getString(rs, "iteration"));
        
        UserTO uto = new UserTO(getString(rs, "user_id"));
        response.setResource(uto);
        
        rsto.setId(getString(rs, "requeriment_status_id"));
        response.setStatus(rsto);
        
        return response;
    }


}

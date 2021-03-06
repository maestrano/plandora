package com.pandora.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.pandora.AreaTO;
import com.pandora.TransferObject;
import com.pandora.exception.DataAccessException;
import com.pandora.helper.LogUtil;

/**
 * This class contain the methods to access information about 
 * Area entity into data base.
 */
public class AreaDAO extends DataAccess {
    
    /**
     * Get a list of all Area TOs from data base.
     */
    public Vector getList(Connection c) throws DataAccessException {
		Vector<AreaTO> response= new Vector<AreaTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null; 
		try {
		    
		    //get a list of objects from data base
			pstmt = c.prepareStatement("select id, name, description from area");
			rs = pstmt.executeQuery();
						
			//Prepare data to return
			while (rs.next()){
			    AreaTO ato = this.populateBeanByResultSet(rs);
			    response.addElement(ato);
			} 
						
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			//Close the current result set and statement
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException ec) {
			    LogUtil.log(this, LogUtil.LOG_ERROR, "DB Closing statement error", ec);
			} 		
		}	 
		return response;
    }

    /**
     * Get a specific Area TO from data base, based on id.
     */
    public TransferObject getObject(TransferObject to, Connection c) throws DataAccessException {
		AreaTO response= null;
		ResultSet rs = null;
		PreparedStatement pstmt = null; 
		try {
		    
		    //select a area from database
			AreaTO filter = (AreaTO)to;
			pstmt = c.prepareStatement("select id, name, description from area where id = ?");
			pstmt.setString(1, filter.getId());
			rs = pstmt.executeQuery();
						
			//Prepare data to return
			if (rs.next()){
				response = this.populateAreaByResultSet(rs);
			} 
						
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			//Close the current result set and statement
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException ec) {
			    LogUtil.log(this, LogUtil.LOG_ERROR, "DB Closing statement error", ec);
			} 		
		}	 
		return response;
    }
    
    /**
     * Insert a new area into data base.
     */
    public void insert(TransferObject to, Connection c) throws DataAccessException {
		PreparedStatement pstmt = null; 
		try {
		    
		    AreaTO uto = (AreaTO)to;
			pstmt = c.prepareStatement("insert into area (id, name, description) values (?,?,?)");
			pstmt.setString(1, this.getNewId());
			pstmt.setString(2, uto.getName());
			pstmt.setString(3, uto.getDescription());
			pstmt.executeUpdate();
												
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			try {
				if(pstmt != null) pstmt.close();
			} catch (SQLException ec) {
			    LogUtil.log(this, LogUtil.LOG_ERROR, "DB Closing statement error", ec);
			} 		
		}       
    }
    
    /**
     * Update information of user into data base.
     */    
    public void update(TransferObject to, Connection c) throws DataAccessException {
		PreparedStatement pstmt = null; 
		try {
		    
		    AreaTO uto = (AreaTO)to;
			pstmt = c.prepareStatement("update area set name=?, description=? where id=?");
			pstmt.setString(1, uto.getName());
			pstmt.setString(2, uto.getDescription());
			pstmt.executeUpdate();
												
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			try {
				if(pstmt != null) pstmt.close();
			} catch (SQLException ec) {
			    LogUtil.log(this, LogUtil.LOG_ERROR, "DB Closing statement error", ec);
			} 		
		}
    }

    /**
     * Remove an user of data base.
     */    
    public void remove(TransferObject to, Connection c) throws DataAccessException {
		PreparedStatement pstmt = null; 
		try {
		    
		    AreaTO uto = (AreaTO)to;
			pstmt = c.prepareStatement("delete from area where id = ?");
			LogUtil.log(this, LogUtil.LOG_DEBUG, "id:" + uto.getId());
			pstmt.setString(1, uto.getId());			
			pstmt.executeUpdate();
												
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			try {
				if(pstmt != null) pstmt.close();
			} catch (SQLException ec) {
			    LogUtil.log(this, LogUtil.LOG_ERROR, "DB Closing statement error", ec);
			} 		
		}
    }
    
    /**
     * This method get a User TO from BD based on username.
     * @param uto
     * @return
     * @throws DataAccessException
     */
    public AreaTO getObjectByName(AreaTO uto) throws DataAccessException{
        AreaTO response = null;
		try {
			Connection c = getConnection();
			response = this.getObjectByName(uto, c);
		} catch(Exception e){
			throw new DataAccessException(e);
		}
        return response;
    }

    /**
     * This method get a User TO from BD based on username.
     */
    private AreaTO getObjectByName(AreaTO uto, Connection c) throws DataAccessException{
		AreaTO response= null;
		ResultSet rs = null;
		PreparedStatement pstmt = null; 
		try {
		    
		    //select a user from database
			pstmt = c.prepareStatement("select id, name, description from area where name = ?");
			pstmt.setString(1, uto.getName());
			rs = pstmt.executeQuery();
						
			//Prepare data to return
			if (rs.next()){
				response = this.populateAreaByResultSet(rs);
			} 
						
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			//Close the current result set and statement
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException ec) {
			    LogUtil.log(this, LogUtil.LOG_ERROR, "DB Closing statement error", ec);
			} 		
		}	 
		return response;
    }
    
    
    /**
     * Create a new TO object based on data into result set.
     */
    protected AreaTO populateAreaByResultSet(ResultSet rs) throws DataAccessException{
        AreaTO response = new AreaTO();
       
        response.setId(getString(rs, "ID"));
        response.setName(getString(rs, "NAME"));
        response.setDescription(getString(rs, "DESCRIPTION"));
        
        return response;
    }
    
    /**
     * Return an AreaTO filled with data from ResultSet. 
     */
    private AreaTO populateBeanByResultSet(ResultSet rs) throws DataAccessException{
        AreaTO response = new AreaTO();
        
        response.setId(getString(rs, "ID"));
        response.setName(getString(rs, "NAME"));
        response.setDescription(getString(rs, "DESCRIPTION"));
        
        return response;
    }
    
}

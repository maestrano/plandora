package com.pandora.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.pandora.AdditionalFieldTO;
import com.pandora.CustomFormTO;
import com.pandora.MetaFormTO;
import com.pandora.PlanningTO;
import com.pandora.TransferObject;
import com.pandora.exception.DataAccessException;
import com.pandora.helper.DateUtil;

/**
*/
public class CustomFormDAO extends PlanningDAO {
    
    AdditionalFieldDAO afdao = new AdditionalFieldDAO();
    
    public Vector<CustomFormTO> getRecords(String metaFormId, Timestamp iniRange) throws DataAccessException {
        Vector<CustomFormTO> response = null;
        Connection c = null;
		try {
			c = getConnection();
			response = this.getRecords(metaFormId, iniRange, c);
		} catch(Exception e) {
			throw new DataAccessException(e);
		} finally {
			this.closeConnection(c);
		}
        return response;
    }
    
    
    private Vector<CustomFormTO> getRecords(String metaFormId, Timestamp iniRange, Connection c) throws DataAccessException {
		Vector<CustomFormTO> response= new Vector<CustomFormTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = c.prepareStatement("select id, meta_form_id from custom_form where meta_form_id=?");
			pstmt.setString(1, metaFormId);
			rs = pstmt.executeQuery();			
			while (rs.next()){
			    CustomFormTO cfto = this.populateObjectByResultSet(rs, iniRange, true, c);
			    if (cfto!=null) {
			    	response.addElement(cfto);	
			    }
			}
						
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			super.closeStatement(rs, pstmt);
		}	 
		return response;
    }
    
    
    public TransferObject getObject(TransferObject to, Connection c) throws DataAccessException {
    	CustomFormTO response= null;
		ResultSet rs = null;
		PreparedStatement pstmt = null; 
		try {
			CustomFormTO filter = (CustomFormTO)to;
		    pstmt = c.prepareStatement("select id, meta_form_id from custom_form where id=?");		
			pstmt.setString(1, filter.getId());
			rs = pstmt.executeQuery();						
			if (rs.next()){
				response = this.populateObjectByResultSet(rs, null, false, c);
			} 
						
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			super.closeStatement(rs, pstmt);
		}	 
		return response;
    }
    
    public void update(TransferObject to, Connection c)  throws DataAccessException {
		PreparedStatement pstmt = null; 
		try {
		    
			CustomFormTO cto = (CustomFormTO)to;
		    super.update(cto, c);
		    
		    pstmt = c.prepareStatement("update custom_form set meta_form_id=? where id=?");
			pstmt.setString(1, cto.getMetaForm().getId());
			pstmt.setString(2, cto.getId());
			pstmt.executeUpdate();
								
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			super.closeStatement(null, pstmt);
		}        
    }
    

    public void insert(TransferObject to, Connection c) throws DataAccessException {
		PreparedStatement pstmt = null;
		try {
		    
			CustomFormTO cto = (CustomFormTO)to;
			cto.setId(this.getNewId());
			cto.setCreationDate(DateUtil.getNow());

		    //insert data into parent entity (PlanningDAO)
		    super.insert(cto, c);
		    
		    pstmt = c.prepareStatement("insert into custom_form (id, meta_form_id) values (?, ?)");
			pstmt.setString(1, cto.getId());
			pstmt.setString(2, cto.getMetaForm().getId());
			pstmt.executeUpdate();
						
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			super.closeStatement(null, pstmt);
		}               
    }
    

    public void remove(TransferObject to, Connection c)  throws DataAccessException {
		PreparedStatement pstmt = null;
		AdditionalFieldDAO adao = new AdditionalFieldDAO();
		
		try {
		    
			CustomFormTO cto = (CustomFormTO)to;
			pstmt = c.prepareStatement("delete from custom_form where id=?");
			pstmt.setString(1, cto.getId());
			pstmt.executeUpdate();
			
			//remove data related to the meta_field and current meta_form
			adao.removeByPlanning(new PlanningTO(cto.getId()), c);
			

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally{
			super.closeStatement(null, pstmt);
		}        
    }
    
    
    private CustomFormTO populateObjectByResultSet(ResultSet rs, Timestamp iniRange, boolean filterCols, Connection c) throws DataAccessException{
    	MetaFormDAO mfdao = new MetaFormDAO();
        CustomFormTO response = new CustomFormTO();
        response.setId(getString(rs, "id"));
        
        MetaFormTO mfto = new MetaFormTO(getString(rs, "meta_form_id"));
        mfto = (MetaFormTO) mfdao.getObject(mfto, c);
        response.setMetaForm(mfto);

	    //get the additional fields
        Vector<AdditionalFieldTO> list = afdao.getListByPlanning(response, iniRange, c);
    	if (list!=null && list.size()>0) {
    		
    		Vector<AdditionalFieldTO> newList = new Vector<AdditionalFieldTO>();
    		if (mfto.getViewableCols()!=null && filterCols && !mfto.getViewableCols().trim().equals("")) {
    			
    			String[] viewable = mfto.getViewableCols().split(";");
    			for(int i=0; i<list.size(); i++) {
    				if (this.contain(i, viewable)) {
    					newList.add(list.elementAt(i));
    				}
				}
    		} else {
    			newList = list;
    		}
    		
    	    response.setAdditionalFields(newList);
    	} else {
    		response = null;
    	}
    	        
        
        return response;
    }


	private boolean contain(int i, String[] viewable) {
		boolean response = false;
		if (viewable!=null) {
			for (String col : viewable) {
				try {
					if (Integer.parseInt(col.trim())==(i+1)){
						response = true;
						break;
					}					
				}catch(Exception e) {
					e.printStackTrace();
					response = false;
				}
			}
		}
		return response;
	}
    
}

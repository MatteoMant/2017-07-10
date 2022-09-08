package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects(Map<Integer, ArtObject> idMap) {
		
		String sql = "SELECT * FROM objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
				
				if (!idMap.containsKey(artObj.getId())) {
					idMap.put(artObj.getId(), artObj);
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAllAdiacenze(Map<Integer, ArtObject> idMap) {
		
		String sql = "SELECT eo1.object_id AS o1, eo2.object_id AS o2, COUNT(*) AS peso "
				+ "FROM exhibition_objects eo1, exhibition_objects eo2 "
				+ "WHERE eo1.exhibition_id = eo2.exhibition_id AND eo1.object_id > eo2.object_id "
				+ "GROUP BY eo1.object_id, eo2.object_id";
		
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject o1 = idMap.get(res.getInt("o1"));
				ArtObject o2 = idMap.get(res.getInt("o2"));
				
				result.add(new Adiacenza(o1, o2, res.getInt("peso")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

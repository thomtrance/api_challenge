package com.disney.studios;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
@Controller
@EnableAutoConfiguration
public class DogPic {

	@Autowired
    DataSource dataSource;
	
	Connection c;
	
	
	//PetLoader pl = new PetLoader();
	
	
	@RequestMapping("/dogpic")
    @ResponseBody
    ArrayList dogs() {
		
		/* 
		 * Set up datasource and query
		 * get list of dogs sorted by favorite value
		 * build array list
		 * 
		 * */
				
		ArrayList<Dog> al = new ArrayList<Dog>();
		try{
			
			String dbstr="Select breed, pic_url,favorite_count, id  from dog order by breed,favorite_count desc";
			c = dataSource.getConnection();
			
			Statement s = c.createStatement();
	         
	         ResultSet rs = s.executeQuery(dbstr);
	       
	        while(rs.next()){
	        	
	        	Dog d = new Dog();
	        	d.setBreed(rs.getString(1));
	        	d.setPic_url(rs.getString(2));
	        	d.setFavorite_count(rs.getInt(3));
	        	d.setId(rs.getInt(4));
	        	
	        	al.add(d);
	        }
			
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
    			c.close();
    			}catch(Exception clex){
    				clex.printStackTrace();
    			}
		}
			
		
		return al;
    }

	/* 
	 * get query parm
	 * set up datasource and query
	 * build arraylist
	 * */
	@RequestMapping("/dogpic/{breed}")
    @ResponseBody
    ArrayList dogbreed(@PathVariable("breed") String breed) {
		ArrayList<Dog> al = new ArrayList<Dog>();
		String theBreed = unNull(breed).toLowerCase();
		try{
			
			String dbstr="Select breed, pic_url,favorite_count, id  from dog where lower(breed)='"+theBreed+"' order by favorite_count desc";
			c = dataSource.getConnection();
			
			Statement s = c.createStatement();
	         
	         ResultSet rs = s.executeQuery(dbstr);
	       
	        while(rs.next()){
	        	
	        	Dog d = new Dog();
	        	d.setBreed(rs.getString(1));
	        	d.setPic_url(rs.getString(2));
	        	d.setFavorite_count(rs.getInt(3));
	        	d.setId(rs.getInt(4));
	        	
	        	al.add(d);
	        }
			
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
    			c.close();
    			}catch(Exception clex){
    				clex.printStackTrace();
    			}
		}
			
		
		return al;
    }
	
	
	
	@RequestMapping(value="/upvote/{id}", method=RequestMethod.PUT)
    @ResponseBody
    int upvote(@PathVariable("id") int id) {
		
		int retVal=0;
		try{
			String dbstr="UPDATE dog set favorite_count = favorite_count+1 where id="+id;
			c = dataSource.getConnection();
			Statement s = c.createStatement();
			s.execute(dbstr);
			retVal = s.getUpdateCount();
		}catch(Exception e1){
			e1.printStackTrace();
		}
		finally{
			try{
    			c.close();
    			}catch(Exception clex){
    				clex.printStackTrace();
    			}
		}
		return retVal; 
	}
	
	
	@RequestMapping(value="/downvote/{id}", method=RequestMethod.PUT)
    @ResponseBody
    int downvote(@PathVariable("id") int id) {
		int retVal = 0;
		try{
		String dbstr="UPDATE dog set favorite_count = favorite_count-1 where id="+id;
		c = dataSource.getConnection();
		Statement s = c.createStatement();
		s.execute(dbstr);
		retVal = s.getUpdateCount();
		}catch(Exception e2){
			e2.printStackTrace();
		}finally{
			try{
    			c.close();
    			}catch(Exception clex){
    				clex.printStackTrace();
    			}
		}
		return retVal; 
	}
	
	@RequestMapping("/dog/{id}")
    @ResponseBody
    Dog dog(@PathVariable("id") int id) {
		
		Dog d = new Dog();
		
		try{
			
			String dbstr="Select breed, pic_url,favorite_count, id  from dog where id="+id; 
			c = dataSource.getConnection();
			
			Statement s = c.createStatement();
	         
	         ResultSet rs = s.executeQuery(dbstr);
	         if(rs.first()){
	           	d = new Dog();
	        	d.setBreed(rs.getString(1));
	        	d.setPic_url(rs.getString(2));
	        	d.setFavorite_count(rs.getInt(3));
	        	d.setId(rs.getInt(4));
	        }
			
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
    			c.close();
    			}catch(Exception clex){
    				clex.printStackTrace();
    			}
		}
			
		
		return d;
    }
	
	
	
private String unNull(String s){
	String newStr="";
	if(s!=null){
		newStr=s;
	}
	return newStr;
}
	 
	 
}

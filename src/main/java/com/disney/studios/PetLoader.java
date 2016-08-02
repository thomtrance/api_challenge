package com.disney.studios;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.disney.studios.Dog;

import javax.sql.DataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Loads stored objects from the file system and builds up
 * the appropriate objects to add to the data source.
 *
 * Created by fredjean on 9/21/15.
 */
@Component
public class PetLoader implements InitializingBean {
    // Resources to the different files we need to load.
    @Value("classpath:data/labrador.txt")
    private Resource labradors;

    @Value("classpath:data/pug.txt")
    private Resource pugs;

    @Value("classpath:data/retriever.txt")
    private Resource retrievers;

    @Value("classpath:data/yorkie.txt")
    private Resource yorkies;

    @Autowired
    DataSource dataSource;

    Connection conn; 
    
    int id=0;
    /**
     * Load the different breeds into the data source after
     * the application is ready.
     *
     * @throws Exception In case something goes wrong while we load the breeds.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
    	createTable();
        loadBreed("Labrador", labradors);
        loadBreed("Pug", pugs);
        loadBreed("Retriever", retrievers);
        loadBreed("Yorkie", yorkies);
    }

    /**
     * Reads the list of dogs in a category and (eventually) add
     * them to the data source.
     * @param breed The breed that we are loading.
     * @param source The file holding the breeds.
     * @throws IOException In case things go horribly, horribly wrong.
     */
    private void loadBreed(String breed, Resource source) throws IOException {
        try ( BufferedReader br = new BufferedReader(new InputStreamReader(source.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
           
               
                /* TODO: Create appropriate objects and save them to
                 *       the datasource.
                 */
                
                Dog d = new Dog();
                d.setBreed(breed);
                d.setPic_url(line);
                d.setFavorite_count(0);
                d.setId(id++);
                
                loadDog(d);
            }
        }
    }
    
    private void createTable(){
    	
    	
    	try{
    	conn = dataSource.getConnection();
    	String dogTable = "CREATE TABLE dog(" +
    	"breed varchar(100),"+
    	"pic_url varchar(300),"+
    	"favorite_count int, "+
    	"id int)";

    	Statement s = conn.createStatement();
    	s.execute(dogTable);
    	}catch(Exception cex){
    		cex.printStackTrace();
    	}
    	finally{
    		if(conn!=null){
    			try{
    			conn.close();
    			}catch(Exception clex1){clex1.printStackTrace();}
    		}
    	}
    }
    
    private void loadDog(Dog d){
    	
    	
    	try{
    		
    		conn = dataSource.getConnection();
    		
    		String load = "Insert into dog (breed, pic_url,favorite_count,id) values('"+d.getBreed()+"','"+d.getPic_url()+"',"+d.getFavorite_count()+","+d.getId()+")";
   	
    		Statement s = conn.createStatement();
    	
    		s.execute(load);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		if(conn!=null){
    			try{
    			conn.close();
    			}catch(Exception clex2){
    				clex2.printStackTrace();
    			}
    		}
    	}
    }
}

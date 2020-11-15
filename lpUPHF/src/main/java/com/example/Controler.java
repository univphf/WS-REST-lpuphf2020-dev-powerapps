package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tondeur-h
 */

@RestController
public class Controler {
    
    @Autowired
   JdbcTemplate jdbctemplate=new JdbcTemplate(); 


   /**************************
    * Get contact actifs
    * @return 
    **************************/ 
  @GetMapping (value="/getContacts", produces="application/json")
    public ResponseEntity<List<contacts>> getContacts()
    {
    List<contacts> etds=new ArrayList<>();
        
        String sql="SELECT [id],ISNULL([Nom],'NC'),ISNULL([Prenom],'NC'),ISNULL([Tel],'NC'),ISNULL([Email],'NC') FROM [lpuphf2020].[dbo].[contacts] where actif=1;";
        
       List<Map<String, Object>> rows = jdbctemplate.queryForList(sql);
       
       for (Map row : rows) {
            contacts ctx = new contacts();
            
            ctx.setId(row.get("id").toString());
            ctx.setNom(row.get("Nom").toString());
            ctx.setPrenom(row.get("Prenom").toString());
            ctx.setTel(row.get("Tel").toString());
            ctx.setMail(row.get("Email").toString());
            
            etds.add(ctx);
        }
 
       return  new ResponseEntity<>(etds,HttpStatus.OK); 
    }

    /**********************
     * Get contact non actifs
     * @return 
     *********************/
     @GetMapping (value="/getNonActifContacts", produces="application/json")
    public ResponseEntity<List<contacts>> getNonActifContacts()
    {
    List<contacts> etds=new ArrayList<>();
        
        String sql="SELECT [id],ISNULL([Nom],'NC'),ISNULL([Prenom],'NC'),ISNULL([Tel],'NC'),ISNULL([Email],'NC') FROM [lpuphf2020].[dbo].[contacts] where actif=0;";
        
       List<Map<String, Object>> rows = jdbctemplate.queryForList(sql);
       
       for (Map row : rows) {
            contacts ctx = new contacts();
            
            ctx.setId(row.get("id").toString());
            ctx.setNom(row.get("Nom").toString());
            ctx.setPrenom(row.get("Prenom").toString());
            ctx.setTel(row.get("Tel").toString());
            ctx.setMail(row.get("Email").toString());
            
            etds.add(ctx);
        }
 
       return  new ResponseEntity<>(etds,HttpStatus.OK); 
    }

    /**********************
     * Get contact non actifs/ actifs
     * @return 
     *********************/
     @GetMapping (value="/getAllContacts", produces="application/json")
    public ResponseEntity<List<contacts>> getAllContacts()
    {
    List<contacts> etds=new ArrayList<>();
        
        String sql="SELECT [id],ISNULL([Nom],'NC'),ISNULL([Prenom],'NC'),ISNULL([Tel],'NC'),ISNULL([Email],'NC') FROM [lpuphf2020].[dbo].[contacts]";
        
       List<Map<String, Object>> rows = jdbctemplate.queryForList(sql);
       
       for (Map row : rows) {
            contacts ctx = new contacts();
            
            ctx.setId(row.get("id").toString());
            ctx.setNom(row.get("Nom").toString());
            ctx.setPrenom(row.get("Prenom").toString());
            ctx.setTel(row.get("Tel").toString());
            ctx.setMail(row.get("Email").toString());
            
            etds.add(ctx);
        }
 
       return  new ResponseEntity<>(etds,HttpStatus.OK); 
    }
    
    /****************
     * get Contact by Id
     * @param numero
     * @return 
     **************/
   @GetMapping (value="/getContact", produces="application/json")
    public ResponseEntity<contacts> getEtudiant(@RequestParam("id") int numero)
    {
        
       String sql="SELECT [id],ISNULL([Nom],'NC'),ISNULL([Prenom],'NC'),ISNULL([Tel],'NC'),ISNULL([Email],'NC') FROM [lpuphf2020].[dbo].[contacts] where id=?";
      
        contacts ctx = new contacts();
        
       try{
       Map<String, Object> map= jdbctemplate.queryForMap(sql,numero);
       
       if (!map.isEmpty())
       {
            ctx.setId(map.get("id").toString());
            ctx.setNom(map.get("nom").toString());
            ctx.setPrenom(map.get("prenom").toString());
            ctx.setTel(map.get("Tel").toString());
            ctx.setMail(map.get("Email").toString());
       }
       } catch(DataAccessException e) {
       //defaut NOT FOUND
       return new ResponseEntity<>(ctx,HttpStatus.NOT_FOUND);
       }
       
          return  new ResponseEntity<>(ctx,HttpStatus.OK);
    }


    /*************************************************
     * Inserer un nouveau contact
     * @param ctx
     * @return 
     *************************************************/
    @PutMapping(value="/putContact",consumes="application/json")
    public ResponseEntity putContact(@RequestBody contacts ctx)
    {
        String sql="INSERT INTO [dbo].[contacts]([id],[Nom],[Prenom],[Tel],[Email]) VALUES (?,?,?,?,?)";
        if (jdbctemplate.update(sql,ctx.getId(),ctx.getNom(),ctx.getPrenom(),ctx.getTel(),ctx.getMail())==-1)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    
    /*************************************************
     * Supprimer un contact
     * @param id
     * @return 
     *************************************************/
    @DeleteMapping(value="/delContact")
    public ResponseEntity deleteContact(@RequestParam("id") int id)
    {
        String sql="delete from [dbo].[contacts] where id=?";
        if (jdbctemplate.update(sql,id)==0)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    
    /*************************************************
     * Supprimer un contact
     * @param id
     * @return 
     *************************************************/
    @DeleteMapping(value="/delContact2/{id}")
    public ResponseEntity delete2Contacts(@PathVariable("id") int id)
    {
        String sql="delete from [dbo].[contacts] where id=?";
          if (jdbctemplate.update(sql,id)==0)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    
    /*************************************************
     * Maj contact
     * @param ctx
     * @return
     *************************************************/
    @PatchMapping(value="/updateContact", consumes="application/json")
    public ResponseEntity updateEtudiant(@RequestBody contacts ctx)
    {
        String sql="UPDATE [dbo].[contacts] SET [Nom]=?,[Prenom]=?,[Tel]=?,[Email]=?,[actif]=1 WHERE id=?";

        System.out.println(ctx.toString());
        
        try{
        jdbctemplate.update(sql, ctx.getNom(),ctx.getPrenom(),ctx.getTel(),ctx.getMail(),ctx.getId());
        } catch (DataAccessException e )
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
         return new ResponseEntity(HttpStatus.CREATED);
    }

    
}

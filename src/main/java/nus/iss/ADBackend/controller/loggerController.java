package nus.iss.ADBackend.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import nus.iss.ADBackend.Service.DietRecordService;
import nus.iss.ADBackend.Service.HealthRecordService;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.model.*;
import nus.iss.ADBackend.model.User;

@RestController
@RequestMapping(value= "/user", produces = "application/json")
public class loggerController {

    @Autowired
    DietRecordService dietRecordService;

    @Autowired
    UserService userService;

    @Autowired
    HealthRecordService hrService;
    

    @RequestMapping("/gethealthrecords")
    public HealthRecord getHealthRecord(@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        String dateString = response.getAsString("date");
        LocalDate date = LocalDate.parse(dateString);

        User curr = userService.findUserByUsername(username); 
        HealthRecord myHr = hrService.createHealthRecordIfAbsent(curr.getId(), date);
        System.out.println(myHr);
        return myHr;
    }

    @RequestMapping("/getdietrecords")
    public List<DietRecord> getDietRecords(@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        String dateString = response.getAsString("date");
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateString);
        // System.out.println((username));
        

        User curr = userService.findUserByUsername(username);
        List<DietRecord> dList = new ArrayList<>();
        dList = dietRecordService.findByUserIdAndDate(curr.getId(), date);
        
        return dList;
    }

    @RequestMapping("/adddietrecord")
    @ResponseStatus(HttpStatus.OK)
    public void addDietRecord (@RequestBody JSONObject response) throws IOException, ParseException{
        System.out.println("in add diet record");

        //TO DO: receive an object here
        String username = response.getAsString("username");
        User user = userService.findUserByUsername(username);
        String dateString = response.getAsString("date");
        LocalDate date = LocalDate.parse(dateString);
        String mealName = response.getAsString("mealName");
        String mealType = response.getAsString("mealType");
        double mealCals = Double.valueOf(response.getAsString("calories"));
        double weight = Double.valueOf(response.getAsString("weight"));

        DietRecord myDr = new DietRecord(date, user, mealName, MealType.valueOf(mealType), mealCals, weight);
        
        dietRecordService.createDietRecord(myDr);
        
        
    }
    

}

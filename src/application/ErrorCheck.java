package application;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


public class ErrorCheck {

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static PopUpWindow popUpObject = new PopUpWindow();

	public boolean checkForErrors(String subj, String loc, LocalDate stDate, String stHour, String stMin, String stTimePer, LocalDate endDate, String endHour, String endMin, String endTimePer) throws IOException, ParseException{

		boolean errorsExist = true;
		

		try {
			errorsExist = wrongTimeValues(stHour, stMin, endHour, endMin);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		if(errorsExist){
			return true;
		}

		errorsExist = checkForDateErrors(stDate, stHour, stMin, stTimePer, endDate, endHour, endMin, endTimePer);
		
		if(errorsExist){
			return true;
		}
		return false;

	}


	//Checking for syntax errors on hour and minute values
	private boolean wrongTimeValues(String sh, String sm, String eh, String em) throws IOException{
		if(Integer.parseInt(sh) > 12 || Integer.parseInt(eh) > 12){
			popUpObject.popUp("The hour value cannot be above 12");
			return true;
		}else if(Integer.parseInt(sh) < 1 || Integer.parseInt(eh) < 1){
			popUpObject.popUp("The hour value cannot be below 1");
			return true;
		}else if(Integer.parseInt(sm) > 59 || Integer.parseInt(em) >59){
			popUpObject.popUp("The minute value cannot be above 59");
			return true;
		}else if(Integer.parseInt(sm) < 0 || Integer.parseInt(em) < 0){
			popUpObject.popUp("The minute value cannot be below 0");
			return true;
		}else{
			return false;
		}

	}

	//Checking if there are logical mistakes on the date and time values
	private boolean checkForDateErrors(LocalDate stDate, String stHour, String stMin, String stTimePer, LocalDate endDate, String endHour, String endMin, String endTimePer) throws ParseException, IOException{
		Date d = new Date();
		String cdt = dateFormat.format(d);
		Date currentDateTime = dateFormat.parse(cdt);


		//if the hour value is equal to '12', it needs to be changed to '00' in the 24-hour clock
		if(stHour.equals("12")){
			stHour = "00";
		}

		//convert from 12-hour clock to 24-hour clock in order to compare with the current time
		String stTime = "";
		if(stTimePer.equals("am")){
			stTime = (stHour + ":" + stMin);
		}else{
			stTime = (Integer.parseInt(stHour)  + 12)  + ":" + stMin;
		}
		Date startingDateTime = dateFormat.parse(stDate.toString() + " " + stTime);
		String sdt = dateFormat.format(startingDateTime);


		//if the hour value is equal to '12', it needs to be changed to '00' in the 24-hour clock
		if(endHour.equals("12")){
			endHour = "00";
		}

		//convert from 12-hour clock to 24-hour clock in order to compare with the current time
		String endTime = "";
		if(endTimePer.equals("am")){
			endTime = (endHour + ":" + endMin);
		}else{
			endTime = (Integer.parseInt(endHour) + 12) + ":" + endMin;
		}
		Date endingDateTime = dateFormat.parse(endDate.toString() + " " + endTime);
		String edt = dateFormat.format(endingDateTime);

		System.out.println(cdt);
		System.out.println(sdt);
		System.out.println(edt);



		if(startingDateTime.before(currentDateTime)){
			popUpObject.popUp("The starting date/time of the event cannot be before the current date/time");
			return true;
		}else if(startingDateTime.after(endingDateTime)){
			popUpObject.popUp("The ending date/time of the event cannot be before the starting date/time");
			return true;
		}else{
			return false;
		}

	}




}
